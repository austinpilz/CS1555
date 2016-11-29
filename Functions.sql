/* --- Functions --- */
/* 1 - Erase the database (Austin) (DONE)*/
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
/* In Java */

/* 3 - Load schedule information */
/* In Java */

/* 4 - Load pricing information */
/* In Java */

/* 5 - Load plane information */
/* In Java */

/* 6 - Passenger Manifest (Austin) (DONE) */
/* For a specific flight on a specific day. 
Assumptions: Create a view that already has all of the passengers for all flights, across all dates, who have purchased a ticket. Then Java code is
eaiser to read and maintain */

CREATE OR REPLACE VIEW passengerManifest AS
SELECT r.flight_number, r.Flight_Date, Salutation, First_Name, Last_Name FROM Customer
RIGHT JOIN
 (SELECT r.CID, rd.Flight_Number, rd.Flight_Date FROM Reservation_Detail rd
    RIGHT JOIN Reservation r
    ON rd.reservation_number = r.reservation_number
    WHERE r.ticketed = 'Y'
    ) r
    ON Customer.CID = r.CID
    ORDER BY Customer.LAST_NAME, Customer.First_NAME;
    
/* Usage: SELECT * FROM passengerManifest WHERE flight_number = '1' AND flight_date = '18-NOV-16'; */
    
    
    
/* -- User Side -- */

/* 7 - Add Customer (Austin) */
/* Assumptions - This doesn't need to be a procedure because is a simple insert statement where we should have all of the data already, no additional lookup
is required*/

CREATE OR REPLACE PROCEDURE generateCustomerID(ID OUT INT)
AS
BEGIN
  BEGIN
      select dbms_random.value(100000000, 999999999) num into ID from dual;
    END;
END;
/


/* 8 - Customer Info (Austin) */
/* Given customer name, display customer information */
/* In JAVA */

/* 9 - Flight Price */
/* Find price for flight between two cities */

/* 10 - City Routes (Austin) */
/* Find all routes between two cities */

/* 11 - Airline City Routes (Austin) */
/* Find all routes between two cities on a given airline */

/* 12 - Open Routes */
/* Find all routes with available seats between two cities on given date */

/* 13 - Airline Open Routes */
/* For given airline, find all routes with available seats between two cities on given date */

/* 14 - Add Resveration */

/* 15 - Reservation Info (Austin) (DONE) */
/* Given reservation number, display reservation information */
/* Query the database to get all the flights for the given reservation and print this to the user. Print an error message in case of an non-existent reservation number. */
CREATE OR REPLACE VIEW reservationFlightInfo AS
Select rd.Reservation_Number, f.Flight_Number, f.Departure_City, f.Departure_Time, f.Arrival_City, f.Arrival_Time FROM Reservation_Detail rd
RIGHT JOIN Flight f
ON rd.FLIGHT_NUMBER = f.FLIGHT_NUMBER;

/* To Use: SELECT * FROM ReservationFlightInfo WHERE Reservation_Number = '9999'; */


/* 16 - Buy Ticket (Austin) (DONE) */
/* Ask the user to supply the reservation number. Mark the fact that the reservation was converted into a purchased ticket. */

/* Assumptions: It never says that we are required to retireve payment details, only MARK the fact that it was converted, thus the procedure only
updated the ticketed field to Y. */

/* Note: It will return a number of rows that were affected by the update to verify that an update did occur. This will not check if the reservation
has already been puchased, however, it's an easier way to send UI verification that the reservation number did indeed exist. */

CREATE OR REPLACE PROCEDURE purchaseTicket(reservationNumber IN varchar2, updatedRows OUT INT)
AS
BEGIN 
  declare
  i INT;
  
  BEGIN
    UPDATE Reservation SET Ticketed = 'Y' WHERE Reservation_Number = reservationNumber;
    i := sql%rowcount;
    updatedRows := i;
  END;
END;
/

/*
--- Testing --- 
variable rowsU number;
exec purchaseTicket('999', :rowsU);
print rowsU; */







