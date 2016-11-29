/* --- Functions --- */
/* 1 (DONE) - Erase the database (Austin) */
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

/* 6 - Passenger Manifest (Austin) */
/* For a specific flight on a specific day. 
Input is flight number and date.
Search the database to locate those passengers who bought tickets for given flight for the given date. 
Print the passenger list (salutation, first name, last name). */

CREATE OR REPLACE PROCEDURE passengerManifest (flightNumber IN varchar2, flightDate IN DATE)
IS  
  BEGIN
    SELECT * FROM Reservation_Detail rd
    LEFT JOIN Reservation r
    ON rd.reservation_number = r.reservation_number
    WHERE rd.fligh_number = flightNumber AND rd.flightDate = flightDate AND r.ticketed = "Y";
  END;
/

/* View should have Flight #, Flight Date, Passenger Salutation, First, Last*/





/* -- User Side -- */

/* 7 - Add Customer (Austin) */
/* Assumptions - This doesn't need to be a procedure because is a simple insert statement where we should have all of the data already, no additional lookup
is required*/

/* 8 - Customer Info (Austin) */
/* Given customer name, display customer information */

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

/* 15 - Reservation Info (Austin) */
/* Given reservation number, display reservation information */

/* 16 - Buy Ticket (Austin) (DONE) */
/* Ask the user to supply the reservation number. Mark the fact that the reservation was converted into a purchased ticket. */

/* Assumptions: It never says that we are required to retireve payment details, only MARK the fact that it was converted, thus the procedure only
updated the ticketed field to Y. */

/* TODO: Should we check to see if the reservation is already marked as purchased by checking the number of rows affected in Java? */

/* To Test: call purchaseTicket('284'); */

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




