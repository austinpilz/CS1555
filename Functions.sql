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