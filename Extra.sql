/* Non-Milestone Functions */


/* DROP TABLE COMMANDS */

DROP TABLE Reservation_Detail;
DROP TABLE Reservation;
DROP TABLE Flight;
DROP TABLE Price;
DROP TABLE Customer;
DROP TABLE PDate;
DROP TABLE Plane;
DROP TABLE AIRLINE;


/* Display all planes with their airline name */
SELECT Plane.Plane_Type, Plane.Manufacture, Airline.Airline_Name FROM PLANE
INNER JOIN Airline
ON Plane.Owner_ID = Airline.Airline_ID;

/* Verify example # airlines matches project requirements (10) */
SELECT COUNT(*) FROM AIRLINE;

/* Verify example # planes matches project requirements (30) */
SELECT COUNT(*) FROM PLANE;

/* Verify example customer # matches project requirements (200) */
SELECT COUNT(*) FROM CUSTOMER;


