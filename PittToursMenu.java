/*
*Alisha Forrest - ahf5
*Austin Pilz - anp147
*/
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.*;
import java.io.*;

public class PittToursMenu
{
	//GLOBAL VARIABLES
	public static Scanner keyboard = new Scanner(System.in);

		
	//BELOW: the db setup variable from recitation 8 (TranDemo1.java)
	private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
		
	public PittToursMenu()
	{
		int clientOrAdmin;
		System.out.print("Welcome to Pitt Tours!\nWould you like to see the menu for:\n\t1) Admin\n\t2) Client\nEnter the menu item number to continue: ");
		clientOrAdmin = Integer.parseInt(keyboard.nextLine());
		
		if(clientOrAdmin == 1)
			AdminPrivileges();
		else
			ClientPrivileges();
	}
			
	//Function: AdminPrivileges
	//inputs: none
	//outputs: none
	//Description: The admin sub-menu for the overall application
	//Based on the action the user wants to do, it routes to the correct method
	//Will loop until user indicates exit
	public void AdminPrivileges()
	{
		int selection;
		boolean exit = false;
		
		while(!exit)
		{
			System.out.print("Admin Menu:\n\t1) Erase the Database\n\t2) Load airline information\n\t3) Load schedule information\n\t4) Load pricing information\n\t5) Load plane information\n\t6) Generate passenger manifest for specific flight on given day\n\t7) Exit\nEnter the menu item number to continue: ");
			selection = Integer.parseInt(keyboard.nextLine());
			switch(selection)
			{
				case 1:
					eraseDatabase();
					break;
				case 2:
					loadAirline();
					break;
				case 3:
					loadSchedule();
					break;
				case 4:
					loadPricing();
					break;
				case 5:
					loadPlaneInfo();
					break;
				case 6: 
					generateManifest();
					break;
				case 7:
					exit = true;
					break;
			}
		}
	}
	
	//Function: eraseDatabase
	//inputs: none
	//outputs: none
	//Description: Truncates the tables in order to erase the database
	public void eraseDatabase()
	{
		String selection = "";
		System.out.print("Are you sure? (y/n): ");
		selection = keyboard.nextLine();
		
		if(selection.toLowerCase().equals("y"))
		{
			try
			{
				query = "{call eraseDatabase}";
				prepStatement = connection.prepareCall(query);
				statement = connection.createStatement();
				prepStatement.executeQuery();
				prepStatement.close();
			}
			catch(Exception e)
			{
				System.out.println("Encountered an unexpected error while attempting to erase the database:");
				System.out.print(e);
			}
			finally
			{
				try
				{
					if (statement != null) statement.close();
					if (prepStatement != null) prepStatement.close();
				}
				catch (SQLException e)
				{
					System.out.println("Cannot close Statement. Machine error: "+e.toString());
				}
			}

		}
		else if(selection.toLowerCase().equals("n"))
		{
			System.out.println("The database will remain intact.\nReturning to menu...");
		}
		else
		{
			System.out.println("ERROR: You did not enter \'y\' or \'n\'. Therefore the database will remain intact.\nReturning to menu...");
		}
	}
	
	//Function: loadAirline
	//inputs: none
	//outputs: none
	//Description: Asks user for .csv file which has ariline info
	//inserts it into the database
	public void loadAirline()
	{
		String selection = "";
		System.out.print("Please enter the location of the .csv file: ");
		selection = keyboard.nextLine();
				
		try
		{
			query = "insert into airline values (?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			BufferedReader br = new BufferedReader(new FileReader(selection));
			
			while(br.ready())
			{
				String[] line = br.readLine().split(",");
				prepStatement.setString(1, line[0]); 
				prepStatement.setString(2, line[1]);
				prepStatement.setString(3, line[2]);
				prepStatement.setInt(4, Integer.parseInt(line[3]));
				prepStatement.executeUpdate();
			}
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from airline");
			System.out.println("\nAfter the insert, data is...\n");
			while(resultSet.next()) {
			System.out.println(
				resultSet.getString(1) + ", " +
				resultSet.getString(2) + ", " +
				resultSet.getString(3) + ", " +
				resultSet.getInt(4));
			}
			resultSet.close();
		}
		catch(Exception e)
		{
			System.out.print(e);
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	//Function: loadSchedule
	//inputs: none
	//outputs: none
	//Description: Asks user for .csv file which has schedule info
	//inserts it into the database
	public void loadSchedule()
	{
		String selection = "";
		System.out.print("Please enter the location of the .csv file: ");
		selection = keyboard.nextLine();
		
		try
		{
			query = "insert into flight values (?,?,?,?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			BufferedReader br = new BufferedReader(new FileReader(selection));
			
			while(br.ready())
			{
				String[] line = br.readLine().split(",");
				prepStatement.setString(1, line[0]); 
				prepStatement.setString(2, line[1]);
				prepStatement.setString(3, line[2]);
				prepStatement.setString(4, line[3]);
				prepStatement.setString(5, line[4]);
				prepStatement.setString(6, line[5]);
				prepStatement.setString(7, line[6]);
				prepStatement.setString(8, line[7]);
				prepStatement.executeUpdate();
			}
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from flight");
			System.out.println("\nAfter the insert, data is...\n");
			while(resultSet.next()) {
			System.out.println(
				resultSet.getString(1) + ", " +
				resultSet.getString(2) + ", " +
				resultSet.getString(3) + ", " +
				resultSet.getString(4) + ", " +
				resultSet.getString(5) + ", " +
				resultSet.getString(6) + ", " +
				resultSet.getString(7) + ", " +
				resultSet.getString(8));
			}
			resultSet.close();
		}
		catch(Exception e)
		{
			System.out.print(e);
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	//Function: loadPricing
	//inputs: none
	//outputs: none
	//Description: Asks user for .csv file which has pricing info
	//inserts it into the database
	public void loadPricing()
	{
		String selection = "";
		System.out.print("Please enter the location of the .csv file: ");
		selection = keyboard.nextLine();
		
		try
		{
			query = "insert into price values (?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			BufferedReader br = new BufferedReader(new FileReader(selection));
			
			while(br.ready())
			{
				String[] line = br.readLine().split(",");
				prepStatement.setString(1, line[0]); 
				prepStatement.setString(2, line[1]);
				prepStatement.setString(3, line[2]);
				prepStatement.setInt(4, Integer.parseInt(line[3]));
				prepStatement.setInt(5, Integer.parseInt(line[4]));
				prepStatement.executeUpdate();
			}
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from price");
			System.out.println("\nAfter the insert, data is...\n");
			while(resultSet.next()) {
			System.out.println(
				resultSet.getString(1) + ", " +
				resultSet.getString(2) + ", " +
				resultSet.getString(3) + ", " +
				resultSet.getInt(4) + ", " +
				resultSet.getInt(5));
			}
			resultSet.close();
		}
		catch(Exception e)
		{
			System.out.print(e);
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	//Function: loadPlaneInfo
	//inputs: none
	//outputs: none
	//Description: Asks user for .csv file which has plane info
	//inserts it into the database
	public void loadPlaneInfo()
	{
		String selection = "";
		System.out.print("Please enter the location of the .csv file: ");
		selection = keyboard.nextLine();
		
		try
		{
			query = "insert into plane values (?,?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			BufferedReader br = new BufferedReader(new FileReader(selection));
			
			while(br.ready())
			{
				String[] line = br.readLine().split(",");
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date date_reg = new java.sql.Date (df.parse(line[3]).getTime());
				
				prepStatement.setString(1, line[0]); 
				prepStatement.setString(2, line[1]);
				prepStatement.setInt(3, Integer.parseInt(line[2]));
				prepStatement.setDate(4, date_reg);
				prepStatement.setInt(5, Integer.parseInt(line[4]));
				prepStatement.setString(6, line[5]);
				prepStatement.executeUpdate();
			}
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from price");
			System.out.println("\nAfter the insert, data is...\n");
			while(resultSet.next()) {
			System.out.println(
				resultSet.getString(1) + ", " +
				resultSet.getString(2) + ", " +
				resultSet.getInt(3) + ", " +
				resultSet.getInt(4) + ", " +
				resultSet.getInt(5));
			}
			resultSet.close();
		}
		catch(Exception e)
		{
			System.out.print(e);
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	//Function: generateManifest
	//inputs: none
	//outputs: none
	//Description: 
	public void generateManifest()
	{
	}
	
	//Function: AdminPrivileges
	//inputs: none
	//outputs: none
	//Description: The admin sub-menu for the overall application
	//Based on the action the user wants to do, it routes to the correct method
	//Will loop until user indicates exit
	public void ClientPrivileges()
	{
		int selection;
		boolean exit = false;
		
		while(!exit)
		{
			System.out.print("Client Menu:\n\t1) Add customer\n\t2) Show customer info, given customer name\n\t3) Find Prices for flights between two cities\n\t4) Find all routes between two cities\n\t5) Find all routes between two cities of a given airline\n\t6) Find all routes with available seats \n\tbetween two cities on a given day\n\t7) For a given airline, find all routes with available seats\n\tbetween two cities on given day\n\t8) Add reservation\n\t9) Show reservation info, given reservation number\n\t10) Buy ticket from existing reservation\n\t11) Exit\nEnter the menu item number to continue: ");
			selection = Integer.parseInt(keyboard.nextLine());
			switch(selection)
			{
				case 1:
					addCustomer();
					break;
				case 2:
					showCustomerInfo();
					break;
				case 3:
					findPriceByRoute();
					break;
				case 4:
					findAllRoutes();
					break;
				case 5:
					findAllRoutesByAirline();
					break;
				case 6:
					findAvailableSeatsByRoute();
					break;
				case 7: 
					findAvailableSeatsByAirline();
					break;
				case 8:
					addReservation();
					break;
				case 9:
					showReservationByNumber();
					break;
				case 10:
					buyTicket();
					break;
				case 11:
					exit = true;
					break;
			}
		}
	}
	
	//Function: addCustomer
	//inputs: none
	//outputs: none
	/*Description:
	Ask the user to supply all the necessary fields for the new customer: salutation (Mr/Mrs/Ms),
	first name, last name, address (street, city, state), phone number, email address, credit card
	number, credit card expiration date. Your program must print the appropriate prompts so
	that the user supplies the information one field at a time.
	Produce an error message if a customer with the same last and first name already exists.
	Assign a unique PittRewards number (i.e., Cid) for the new user.
	Insert all the supplied information and the PittRewards number into the database.
	Display the PittRewards number as a confirmation of successfully adding the new customer
	in the database*/
	public void addCustomer()
	{
	}
	
	//Function: showCustomerInfo
	//inputs: none
	//outputs: none
	/*Description:
	Show customer info, given customer name
	Ask the user to supply the customer name.
	Query the database and print all the information stored for the customer (do not display the
	information on reservations), including the PittRewards number i.e. the cid.*/
	public void showCustomerInfo()
	{
	}
	
	//Function: findPriceByRoute
	//inputs: none
	//outputs: none
	/*Description:
	Ask the user to supply the two cities (city A and city B).
	Print the high and low prices for a one-way ticket from city A to city B, the prices for a
	one-way ticket from city B to city A, and the prices for a round-trip ticket between city A
	and city B.*/
	public void findPriceByRoute()
	{
		String city1, city2;
		System.out.print("Please enter the three-letter airport code of the city: ");
		city1 = keyboard.nextLine();
		System.out.print("Please enter the three-letter airport code of the city: ");
		city2 = keyboard.nextLine();
		
		try
		{
			statement = connection.createStatement();
			query = "select departure_city, arrival_city, high_price, low_price, airline_id, airline_name from price natural join airline where (departure_city = \'"+city1+"\' and arrival_city = \'"+city2+"\') OR (departure_city = \'"+city2+"\' and arrival_city = \'"+city1+"\') order by airline_id";
			resultSet = statement.executeQuery(query);
			
			int tempHighPrice = 0, tempLowPrice = 0, tempAirline = -1, numRows = 0;
			while(resultSet.next()) {
				int currentAirline = resultSet.getInt(5);
				int currentLow = resultSet.getInt(4);
				int currentHigh = resultSet.getInt(3);
				
				System.out.println("DEPART FROM "+resultSet.getString(1)+" ARRIVE TO "+resultSet.getString(2)+" ON "+resultSet.getString(6));
				System.out.println("\tHIGH PRICE = $"+currentHigh+"\n\tLOW PRICE = $"+currentLow);
				
				if(currentAirline == tempAirline)
				{
					System.out.println("ROUND TRIP BETWEEN "+resultSet.getString(1)+" AND "+resultSet.getString(2)+" ON "+resultSet.getString(6));
					System.out.println("\tHIGH PRICE = $"+(currentHigh+tempHighPrice)+"\n\tLOW PRICE = $"+(currentLow+tempLowPrice));
				}
			
				tempHighPrice = currentHigh;
				tempLowPrice = currentLow;
				tempAirline = currentAirline;
				numRows++;
			}
			resultSet.close();
			if(numRows == 0)
			{
				System.out.println("There are no results for prices between: "+city1+","+city2);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	//Function: findAllRoutes
	//inputs: none
	//outputs: none
	/*Description:
	Ask the user to supply the departure city and the arrival city. Query the schedule database
	and find all possible one-way routes between the given city combination. Print a list of
	flight number, departure, city, departure time, and arrival time for all routes.
	Direct routes are trivial. In addition to direct routes, we also allow routes with only one
	connection (i.e. two flights in the route). However, for a connection between two flights to
	be valid, both flights must be operating the same day at least once a week (when looking at
	their weekly schedules) and, also, the arrival time of the first flight must be at least one hour
	before the departure time of the second flight.
	Hint: For simplicity you may split this into two queries: one that finds and prints the direct
	routes, and one that finds and prints the routes with one connection.*/
	public void findAllRoutes()
	{
	}
	
	//Function: findAllRoutesByAirline
	//inputs: none
	//outputs: none
	/*Description:
	Find all routes between two cities of a given airline
	Ask the user to supply the departure city, the arrival city and the name of the airline. Query
	the schedule database and find all possible one-way routes between the given city combination.
	Print a list of airline id, flight number, departure, city, departure time, and arrival time
	for all routes.

	Direct routes are trivial. In addition to direct routes, we also allow routes with only one
	connection (i.e. two flights in the route). However, for a connection between two flights to
	be valid, both flights must be operating the same day at least once a week (when looking at
	their weekly schedules) and, also, the arrival time of the first flight must be at least one hour
	before the departure time of the second flight.
	Hint: For simplicity you may split this into two queries: one that finds and prints the direct
	routes, and one that finds and prints the routes with one connection.*/
	public void findAllRoutesByAirline()
	{
	}
	
	//Function: findAvailableSeatsByRoute
	//inputs: none
	//outputs: none
	/*Description:
	 Find all routes with available seats between two cities on given date
	Ask the user to supply the departure city, the arrival city, and the date. Same with the previous
	task, print a list of flight number, departure, city, departure time, and arrival time for
	all available routes.
	Note that this might be the most difficult query of the project. You need to build upon
	the previous task. You need to be careful for the case where we have a non-direct, oneconnection
	route and one of the two flights has available seats, while the other one does not.
	*/
	public void findAvailableSeatsByRoute()
	{
	}
	
	//Function: findAvailableSeatsByAirline
	//inputs: none
	//outputs: none
	/*Description:
	For a given airline, find all routes with available seats between two cities on given date
	Ask the user to supply the departure city, the arrival city, the date and the name of the airline.
	Same with the previous task, print a list of airline id, flight number, departure, city,
	departure time, and arrival time for all available routes.
	Note that this might be the most difficult query of the project. You need to build upon
	the previous task. You need to be careful for the case where we have a non-direct, oneconnection
	route and one of the two flights has available seats, while the other one does not*/
	public void findAvailableSeatsByAirline()
	{
	}
	
	//Function: addReservation
	//inputs: none
	//outputs: none
	/*Description:
	Ask the user to supply the information for all the flights that are part of his/her reservation.
	For example, for each leg of the reservation you should be asking for the flight number
	and the departure date. There can be a minimum of one leg (one-way ticket, direct route)
	and a maximum of four legs (round-trip ticket, with one connection each way). A simple
	way to do this is to ask the user to supply the flight number first, and then the date for each
	leg, and if they put a flight number of 0 assume that this is the end of the input.	
	After getting all the information from the user, your program must verify that there are still
	available seats in the said flights. If there are seats available on all flights, generate a unique
	reservation number and print this back to the user, along with a confirmation message. Otherwise,
	print an error message*/
	public void addReservation()
	{
	}
	
	//Function: showReservationByNumber
	//inputs: none
	//outputs: none
	/*Description:
	Ask the user to supply the reservation number.
	Query the database to get all the flights for the given reservation and print this to the user.
	Print an error message in case of an non-existent reservation number.*/
	public void showReservationByNumber()
	{
	}
	
	//Function: buyTicket
	//inputs: none
	//outputs: none
	/*Description:
	Ask the user to supply the reservation number.
	Mark the fact that the reservation was converted into a purchased ticket*/
	public void buyTicket()
	{
	}

	public static void main(String[] args) throws SQLException
	{		
		/*NOTE: the majority of the database setup code is from recitation 8 TranDemo1*/
		
		String username,password;
		username = "username"; //MUST EDIT THIS BEFORE RUNNING -- put in your pitt username/password
		password = "password";
		
		try{
			// Register the oracle driver.  
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

			//This is the location of the database.  This is the database in oracle
			//provided to the class
			String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass"; 
	    
			//create a connection to DB on class3.cs.pitt.edu
			connection = DriverManager.getConnection(url, username, password); 
			
			//BELOW: our code
			PittToursMenu menu = new PittToursMenu();
		}
		catch(Exception Ex)  {
			System.out.println("Error connecting to database.  Machine Error: " +
			       Ex.toString());
		}
		finally
		{
			connection.close();
		}
	}
}