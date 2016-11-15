/* Austin Pilz */
/* Alisha Forrest */
/* Team 19 */
/* Database Schema Creation Script */

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
  Constraint flight_FK2 foreign key (Airline_ID) references Airline(Airline_ID) initially deferred deferrable,
  CONSTRAINT flight_CS CHECK (Departure_Time < Arrival_Time));
  
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
  Constraint reservation_detail_FK1 foreign key (Reservation_Number) references Reservation(Reservation_number) initially deferred deferrable,
  Constraint reservation_detail_FK2 foreign key (Flight_Number) references Flight(Flight_Number) initially deferred deferrable);


/* TABLE: Date */
/* Our date */
/* Date( c date ) */

CREATE TABLE PDate (
  C_Date date,
  Constraint date_PK primary key (C_Date) deferrable);

/* Error when executing, "Date" is an invalid table name - thus we named it PDate, for Panos Date */



/* --- TRIGGERS --- */

/* Trigger 1 - adjustTicket */
/* Adjusts the cost of a reservation when the price of one of its legs changes before the ticket is issued */

/*
  Create a function that will go through a reservation's flights and calculate it's total price.
  For this trigger, when one of the price entries is updated, just call the method on the reservation(s) affected
  
  
  Price (Arrival City, Departure City) -> Flight (Arrival City, Departure City) -> Flight # -> Reservation_Detail to see if it's a leg? -> Reservation Cost

*/





/* Trigger 2 - planeUpgrade */
/* changes the plane (type) of a flight to an immediately higher capacity plane (type), if it exists, 
when a new reservation is made on that flight and there are no available seats (i.e., the flight is fully booked). 
A change of plane will fail only if the currently assigned plane for the flight is the one with the biggest capacity. 
For simplicity, we assume that there are always available planes for a switch to succeed. */

/* Trigger 3 - cancelReservation */
/* cancels(deletes)all non-ticketed reservations for a flight, 12 hours prior the flight 
(i.e., 12 hours before the flight is scheduled to depart) and if the number of ticketed passengers fits in a smaller capacity plane, 
then the plane for that flight should be switched to the smaller-capacity plane. */


/* --- Functions --- */
/* 1 - Erase the database - Usage: EXECUTE eraseDatabase;*/
CREATE OR REPLACE PROCEDURE eraseDatabase IS
BEGIN
  DELETE FROM Reservation_Detail;
  DELETE FROM Reservation;
  DELETE FROM Price;
  DELETE FROM Customer;
  DELETE FROM Flight;
  DELETE FROM Plane;
  DELETE FROM Airline;
  DELETE FROM PDate;
  COMMIT;
END;
/

/* 2 - Load airline information */

/* 3 - Load schedule information */

/* 4 - Load pricing information */

/* 5 - Load plane information */

/* 6 - Passenger Manifest */
/* For a specific flight on a specific day. 
Input is flight number and date.
Search the database to locate those passengers who bought tickets for given flight for the given date. 
Print the passenger list (salutation, first name, last name).*/


/* 7 - Add Customer */

/* 8 - Customer Info */
/* Given customer name, display customer information */

/* 9 - Flight Price */
/* Find price for flight between two cities */

/* 10 - City Routes */
/* Find all routes between two cities */

/* 11 - Airline City Routes */
/* Find all routes between two cities on a given airline */

/* 12 - Open Routes */
/* Find all routes with available seats between two cities on given date */

/* 13 - Airline Open Routes */
/* For given airline, find all routes with available seats between two cities on given date */

/* 14 - Add Resveration */

/* 15 - Reservation Info */
/* Given reservation number, display reservation information */

/* 16 - Buy Ticket */
/* Purchase a ticken from an existing reservation, given reservation number */