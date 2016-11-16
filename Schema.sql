/* Austin Pilz */
/* Alisha Forrest */
/* Team 19 */
/* Database Schema Creation Script */


/* TODO: 
When adding a Frequent flier airline ID to customer, it should ensure that the ID is a valid airline ID

Credit card # needs to be exactly 16 characters long

*/




/* TABLE: Airline */
/* Airline(airline id, airline name, airline abbreviation, year founded) */

CREATE TABLE Airline (
  Airline_ID varchar2(5),
  Airline_Name varchar2(50),
  Airline_Abbreviation varchar2(10),
  Year_Founded int,
  Constraint airline_PK primary key (Airline_ID) deferrable,
  CONSTRAINT airline_UQ UNIQUE (Airline_Abbreviation));
  
  /* Assumptions: The airline abbreviation should be unique */
  
  
  
/* TABLE: Plane */
/* Plane(plane type, manufacture, plane capacity, last service date, year, owner id) */

CREATE TABLE Plane (
  Plane_Type varchar2(4),
  Manufacture varchar2(10),
  Plane_Capacity int,
  Last_Service DATE,
  Year int,
  Owner_ID varchar2(5),
  Constraint plane_PK primary key (Plane_Type, Owner_ID) deferrable,
  Constraint plane_FK foreign key (Owner_ID) references Airline( Airline_ID ) initially deferred deferrable);
  
  /* The "Plane_Type" field is essentially the plane's name (ex B712), hence PK */
  
  
  
/* TABLE: Flight */
/* Flight Schedule information */
/* Flight(flight number, airline id, plane type, departure city, arrival city,departure time, arrival time, weekly schedule) */

CREATE TABLE Flight (
  Flight_Number varchar2(3),
  Airline_ID varchar2(5),
  Plane_Type varchar2(4),
  Departure_City varchar2(3),
  Arrival_City varchar2(3),
  Departure_Time varchar2(4),
  Arrival_Time varchar2(4),
  Weekly_Schedule varchar2(7),
  Constraint flight_PK primary key (Flight_Number) deferrable,
  Constraint flight_FK1 foreign key (Plane_Type, Airline_ID) references Plane(Plane_Type, Owner_ID) initially deferred deferrable,
  Constraint flight_FK2 foreign key (Airline_ID) references Airline(Airline_ID) initially deferred deferrable);
  
  /* The handout says "plane_type" of this table should be a char instead of varchar, but you get an error when you reference that field from Plane that has another data type - assuming it should be matching data type*/
  
  /* Assumptions: Check that the departure time is less than the arrival time */
  

/* TABLE: Price */
/* Flight pricing information */
/* Price(departure city, arrival city, airline id, high price, low price) */

CREATE TABLE Price (
  Departure_City varchar2(3),
  Arrival_City varchar2(3),
  Airline_ID varchar2(5),
  High_Price int,
  Low_Price int,
  Constraint price_PK primary key (Departure_City, Arrival_City) deferrable,
  Constraint price_FK foreign key (Airline_ID) references Airline(Airline_ID) initially deferred deferrable,
  Constraint price_CS CHECK (Departure_City <> Arrival_City));
  
  /* Assumptions: Verify that Arrival city and departure city are not the same */
  
  
/* TABLE: Customer */
/* Customer Information */
/* Customer(cid, salutation, first name, last name, credit card num, street, credit card expire, city, state, phone, email) */

CREATE TABLE Customer (
  CID varchar2(9),
  Salutation varchar2(3),
  First_Name varchar2(30),
  Last_Name varchar2(30),
  Credit_Card_Num varchar2(16),
  Credit_Card_Expire date,
  Street varchar2(30),
  City varchar2(30),
  State varchar2(2),
  Phone varchar2(10),
  Email varchar2(30),
  Frequent_Miles varchar2(5),
  Constraint customer_PK primary key (cid) deferrable);

/* Assumptions: When adding a Frequent flier airline ID to customer, it should ensure that the ID is a valid airline ID
The credit card expiration date can be previous to when we are entering row in the case of data restores or record keeping purposes, thus no constraint.*/
  
  
/* TABLE: Reservation */
/* Main Reservation Info */
/* Reservation(reservation number, cid, cost, credit card num, reservation date, ticketed) */

CREATE TABLE Reservation (
  Reservation_Number varchar2(5),
  Start_City varchar2(3),
  End_City varchar2(3),
  CID varchar2(9),
  cost int,
  Credit_Card_Num varchar2(16),
  Reservation_Date date,
  Ticketed varchar(1),
  Constraint reservation_PK primary key (Reservation_Number) deferrable,
  Constraint reservation_FK foreign key (CID) references Customer(CID) initially deferred deferrable);


/* TABLE: Reservation_Detail */
/* Reservation detail(reservation number, flight number, flight date, leg) */

CREATE TABLE Reservation_Detail (
  Reservation_Number varchar2(5),
  Flight_Number varchar2(3),
  Flight_Date date,
  Leg int,
  Constraint reservation_detail_PK primary key (Reservation_Number) deferrable,
  Constraint reservation_detail_FK1 foreign key (Reservation_Number) references Reservation(Reservation_number) on delete cascade initially deferred deferrable,
  Constraint reservation_detail_FK2 foreign key (Flight_Number) references Flight(Flight_Number) initially deferred deferrable);


/* TABLE: Date */
/* Our date */
/* Date( c date ) */

CREATE TABLE PDate (
  C_Date date,
  Constraint date_PK primary key (C_Date) deferrable);

/* Error when executing, "Date" is an invalid table name - thus we named it PDate, for Panos Date */



/* Trigger 1 - adjustTicket */
/* Adjusts the cost of a reservation when the price of one of its legs changes before the ticket is issued */

/*
  Create a function that will go through a reservation's flights and calculate it's total price.
  For this trigger, when one of the price entries is updated, just call the method on the reservation(s) affected
*/



/* Trigger 2 - planeUpgrade */

--function for finding a bigger plane
CREATE OR REPLACE FUNCTION findPlane(numReserved in int,planeOwner in varchar2) 
return varchar2 is
anyPlanes int;
newPlane varchar2(4);
begin
	--find how many planes are bigger than the current plane
	select count(plane_type) into anyPlanes from plane where owner_id = planeOwner and plane_capacity > numReserved;
	if anyPlanes = 0 then 
		return NULL; --if there aren't any more planes
	else
		--get the next biggest plane that is owned by the airline offering the flight
		select plane_type into newPlane from plane where owner_id = planeOwner and plane_capacity = (select min(plane_capacity) from plane where owner_id = planeOwner and plane_capacity > numReserved);
		return (newPlane);
	end if;
end;
/

--stored procedure that updates the flight with a new plane
CREATE OR REPLACE PROCEDURE updatePlane(flightNum in varchar2, planeType in varchar2)
AS
BEGIN
	update Flight set Plane_Type = planeType where Flight_Number = flightNum;
END;
/

/* changes the plane (type) of a flight to an immediately higher capacity plane (type), if it exists, 
when a new reservation is made on that flight and there are no available seats (i.e., the flight is fully booked). 
A change of plane will fail only if the currently assigned plane for the flight is the one with the biggest capacity. 
For simplicity, we assume that there are always available planes for a switch to succeed. */

/*ASSUMPTIONS WE MADE:
	1. When a reservation is made, if a seat is available it is saved for that user
		-Even if they haven't been ticketed, the seat is saved
	2. If there are no seats left on the current plane, it looks for a bigger plane
		a. If there is a bigger plane, it updates the plane size
		b. If there are no bigger planes, the reservation is set to not ticketed
			-Assumes that anyone who has an actual seat reserved 
			(i.e. the first X number of people to reserve based on reservation date) 
			get tickets before anyone else
*/

CREATE OR REPLACE TRIGGER planeUpgrade
BEFORE INSERT ON Reservation_Detail
FOR EACH ROW
DECLARE numReserved int;
		planeCap int;
		planeType varchar(4);
		planeOwner varchar2(5);
BEGIN
	--count the number of reserved seats for the current flight
	select count(reservation_number) into numReserved from reservation_detail where flight_number = (:new.Flight_Number);
	numReserved := numReserved + 1; --add one because it is BEFORE insert, count does not account for the row we are adding
	--get the capacity and owner of the plane
	select plane_capacity, owner_id into planeCap, planeOwner from (flight natural join plane) where flight_number = :new.Flight_Number;
	if (numReserved > planeCap) then --if there are more reservations than space on the flight
		planeType := findPlane(numReserved,planeOwner); --see if a bigger plane can be found
		if planeType IS NULL then
			update reservation set Ticketed = 'N' where reservation_number = :new.reservation_number; --if there is not a bigger plane then the person does not get a ticket
		else
			updatePlane(:new.flight_number, planeType); --update the plane to the larger size
		end if;
	end if;
END;
/


/* Trigger 3 - cancelReservation */
--stored procedure for deleting non-ticketed reservations
--Inputs: current time
--Description
--Subtract the current time from the time of the flight (number of days)
--	multiply the time by 24 to get the number of hours
--If the flight is less than or equal to 12 hours away, select that reservation number
--If the reservation is not ticketed and it's reservation number 
--	is in the reservation numbers that are selected, delete it
CREATE OR REPLACE PROCEDURE deleteReservations(currentDate in DATE)
AS
BEGIN
	DELETE FROM Reservation 
	WHERE Ticketed = 'N' AND Reservation_Number IN (select Reservation_Number from Reservation_Detail where (24*(Flight_Date - currentDate)) <= 12); 
END;
/

--stored procedure for downsizing the plane
--input: the flight number
--description
--	count the number of reservations
--	find the owner and plane type of the plane
--	use findPlane to see if there is a smaller plane
--	if there is a smaller plane != to current plane, update plane
CREATE OR REPLACE PROCEDURE planeDownsize(flightNum in varchar)
AS
	numReserved int;
	planeType varchar2(4);
	currentPlane varchar2(4);
	planeOwner varchar2(5);
BEGIN
	select count(reservation_number) into numReserved from reservation_detail where flightNum = flight_number;
	select owner_id, plane_type into planeOwner, currentPlane from (flight natural join plane) where flight_number = flightNum;
	planeType := findPlane(numReserved,planeOwner);
	if planeType IS NOT NULL AND planeType <> currentPlane then
		updatePlane(flightNum, planeType);
	end if;
END;
/

--TRIGGER 3
/* cancels(deletes)all non-ticketed reservations for a flight, 12 hours prior the flight 
(i.e., 12 hours before the flight is scheduled to depart) and if the number of ticketed passengers fits in a smaller capacity plane, 
then the plane for that flight should be switched to the smaller-capacity plane. */
CREATE OR REPLACE TRIGGER cancelReservation 
BEFORE UPDATE ON PDate
FOR EACH ROW
BEGIN
	deleteReservations(:new.C_Date);
	--loop through the distinct flights to calculate whether plane should downsize or not
	for i in (select distinct flight_number from reservation_detail)
	loop
		planeDownsize(i.flight_number);
	end loop;
END;
/
