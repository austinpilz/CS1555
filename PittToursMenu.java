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
	private CallableStatement callStatement; //used to get return values
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
		
	public PittToursMenu()
	{
		int clientOrAdmin;
		System.out.print("\nWould you like to see the menu for:\n\t1) Admin\n\t2) Client\n\t3) Test Driver\nEnter the menu item number to continue: ");
		clientOrAdmin = Integer.parseInt(keyboard.nextLine());
		
		if(clientOrAdmin == 1)
			AdminPrivileges();
		else if (clientOrAdmin == 2)
			ClientPrivileges();
		else
			testDriver();
	}

	/**
	 * Performs required stress testing.
	 */
	public void testDriver()
	{
		System.out.println("\n\n---------- PittTours Test Driver ----------\n");
		System.out.println("Each function will be called 10 times.");

		String airlineFile = "";
		System.out.print("Please enter the location of the AIRLINE .csv file: ");
		airlineFile = keyboard.nextLine();

		String planeFile = "";
		System.out.print("Please enter the location of the PLANE .csv file: ");
		planeFile = keyboard.nextLine();

		String scheduleFile = "";
		System.out.print("Please enter the location of the SCHEDULE .csv file: ");
		scheduleFile = keyboard.nextLine();

		String pricingFile = "";
		System.out.print("Please enter the location of the PRICING .csv file: ");
		pricingFile = keyboard.nextLine();


		// Erase & Loading
		System.out.println("1 - 5.) Erase Database & Loading");
		for (int i = 0; i < 10; i++)
		{
			this.eraseDatabase();
			this.loadAirline(airlineFile);
			this.loadPlaneInfo(planeFile);
			this.loadSchedule(scheduleFile);
			this.loadPricing(pricingFile);
		}

		// Manifest
		this.generateManifest("fightNumber", "flightDate");

		// Add Customer
		System.out.println("7.) Add Customer");
		this.addCustomer("Mr", "Austin", "Pilz", "austinpilz@gmail.com", "4127154340", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");
		this.addCustomer("Mrs", "Ethan", "Pilz", "ethanppilz@gmail.com", "4127154340", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");
		this.addCustomer("Ms", "Alisha", "Forrest", "ah5@gmail.com", "4124291294", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");
		this.addCustomer("Mr", "Test1", "Test1", "test1@gmail.com", "4127154340", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");
		this.addCustomer("Mr", "Test2", "Test2", "test2@gmail.com", "4127154340", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");
		this.addCustomer("Mr", "Test3", "Test3", "test3@gmail.com", "4127154340", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");
		this.addCustomer("Mr", "Test4", "Test4", "test4@gmail.com", "4127154340", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");
		this.addCustomer("Mr", "Test5", "Test5", "test5@gmail.com", "4127154340", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");
		this.addCustomer("Mr", "Test6", "Test6", "test6@gmail.com", "4127154340", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");
		this.addCustomer("Mr", "Test7", "Test7", "test7@gmail.com", "4127154340", "12345", "18-NOV-16", "111 Charles Dr", "Carnegie", "PA", "1");

		// Get Customer Info
		System.out.println("8.) Show Customer Info");
		this.showCustomerInfo("Austin", "");
		this.showCustomerInfo("Austin", "P");
		this.showCustomerInfo("Austin", "Pi");
		this.showCustomerInfo("Austin", "Pil");
		this.showCustomerInfo("Austin", "Pilz");
		this.showCustomerInfo("Austin", "Pil");
		this.showCustomerInfo("Austin", "Pi");
		this.showCustomerInfo("Austin", "P");
		this.showCustomerInfo("Austin", "Pi");
		this.showCustomerInfo("Austin", "Pilz");

		// Find flight price

		// Find all routes between two cities

		// Find all routes between two cities (airline)

		// Find all routes between two cities (with open seats)

		// Find all routes between two cities (airline)

		// Add reservation

		// Show reservation info
		System.out.println("15.) Show Reservation Info");
		for (int i = 0; i < 10; i++)
		{
			this.showReservationByNumber(i+"");
		}

		// Buy ticket from reservation
		System.out.println("16.) Show Reservation Info");
		for (int i = 0; i < 10; i++)
		{
			this.buyTicket(i+"");
		}
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
		String sel;
		boolean exit = false;
		
		while(!exit)
		{
			System.out.print("Admin Menu:\n\t1) Erase the Database\n\t2) Load airline information\n\t3) Load schedule information\n\t4) Load pricing information\n\t5) Load plane information\n\t6) Generate passenger manifest for specific flight on given day\n\t7) Exit\nEnter the menu item number to continue: ");
			selection = Integer.parseInt(keyboard.nextLine());
			switch(selection)
			{
				case 1:
					sel = "";
					System.out.print("Are you sure? (y/n): ");
					sel = keyboard.nextLine();
		
					if(sel.toLowerCase().equals("y"))
					{
						eraseDatabase();
					}
					else if(sel.toLowerCase().equals("n"))
					{
						System.out.println("The database will remain intact.\nReturning to menu...");
					}
					else
					{
						System.out.println("ERROR: You did not enter \'y\' or \'n\'. Therefore the database will remain intact.\nReturning to menu...");
					}
					break;
				case 2:
					sel = "";
					System.out.print("Please enter the location of the .csv file: ");
					sel = keyboard.nextLine();
					loadAirline(sel);
					break;
				case 3:
					sel = "";
					System.out.print("Please enter the location of the .csv file: ");
					sel = keyboard.nextLine();
					loadSchedule(sel);
					break;
				case 4:
					sel = "";
					String choice = "";
					System.out.print("Would you like to Load Pricing Info or Change the Price of a route? (L/C): ");
					choice = keyboard.nextLine();
					if(choice.equals("L"))
					{
						System.out.print("Please enter the location of the .csv file: ");
						sel = keyboard.nextLine();
						loadPricing(sel);
					}
					else if(choice.equals("C"))
					{
						String d,a,hp,lp,airline;
						System.out.print("Please enter the departure city: ");
						d = keyboard.nextLine();
						System.out.print("Please enter the arrival city: ");
						a = keyboard.nextLine();
						System.out.print("Please enter the high price: ");
						hp = keyboard.nextLine();
						System.out.print("Please enter the low price: ");
						lp = keyboard.nextLine();
						//We need the airline name because we allowed multiple airlines to service one route, and assumed that different airlines can have different prices for a route
						System.out.print("Please enter the airline name: ");
						airline = keyboard.nextLine();
						System.out.println("here");
						changePricing(d,a,Integer.parseInt(hp),Integer.parseInt(lp),airline);
					}
					else
					{
						System.out.println("ERROR: You did not enter \'L\' or \'C\'. \nReturning to menu...");
					}
					break;
				case 5:
					sel = "";
					System.out.print("Please enter the location of the .csv file: ");
					sel = keyboard.nextLine();
					loadPlaneInfo(sel);
					break;
				case 6: 
					String flightNumber = "";
					String flightDate = "";

					System.out.print("Please enter the flight number to generate manifest for: ");
					flightNumber = keyboard.nextLine();

					System.out.print("Please enter the flight date to generate manifest in the format (DD-MM-YY): ");
					flightDate = keyboard.nextLine();
					generateManifest(flightNumber, flightDate);
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
	
	//Function: loadAirline
	//inputs: String selection -> the file location
	//outputs: none
	//Description: Asks user for .csv file which has ariline info
	//inserts it into the database
	public void loadAirline(String selection)
	{			
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
	//inputs: String selection -> the file location
	//outputs: none
	//Description: Asks user for .csv file which has schedule info
	//inserts it into the database
	public void loadSchedule(String selection)
	{	
		try
		{
			query = "insert into flight values (?,?,?,?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			BufferedReader br = new BufferedReader(new FileReader(selection));
			
			while(br.ready())
			{
				String[] line = br.readLine().split(",");
				prepStatement.setInt(1, Integer.parseInt(line[0])); 
				prepStatement.setInt(2, Integer.parseInt(line[1]));
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
				resultSet.getInt(1) + ", " +
				resultSet.getInt(2) + ", " +
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
	//inputs: String selection
	//outputs: none
	//Description: Asks user for .csv file which has pricing info
	//inserts it into the database
	public void loadPricing(String selection)
	{
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
	
	//Function: changePricing
	//inputs: departure city, arrival city, high price, low price, and the airline
	//outputs: none
	//Description: Changes price of the specified route if it exists --> had to add airline
	//because we allowed multiple airlines to service a route, and they might have different prices
	public void changePricing(String dept_city, String arr_city, int high_price, int low_price, String airline)
	{
		try
		{
			//find airline id
			int airline_id = 0;
			statement = connection.createStatement();
			query = "select airline_id from airline where airline_name = \'"+airline+"\'";
			resultSet = statement.executeQuery(query);
			
			if(resultSet.next()) {
				airline_id = resultSet.getInt(1);
			}
			resultSet.close();
		
			query = "update price set high_price = ?, low_price = ? where departure_city = ? and arrival_city = ? and airline_id = ?";
			prepStatement = connection.prepareStatement(query);
			
			prepStatement.setInt(1, high_price); 
			prepStatement.setInt(2, low_price);
			prepStatement.setString(3, dept_city);
			prepStatement.setString(4, arr_city);
			prepStatement.setInt(5, airline_id);
			prepStatement.executeUpdate();
			
			resultSet = statement.executeQuery("select * from price");
			System.out.println("\nAfter the update, data is...\n");
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
	public void loadPlaneInfo(String selection)
	{
		try
		{
			query = "insert into plane values (?,?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			BufferedReader br = new BufferedReader(new FileReader(selection));
			
			while(br.ready())
			{
				String[] line = br.readLine().split(",");
				if(line.length == 6)
				{
					java.sql.Date date_reg = java.sql.Date.valueOf(line[3]);
					
					prepStatement.setString(1, line[0]); 
					prepStatement.setString(2, line[1]);
					prepStatement.setInt(3, Integer.parseInt(line[2]));
					prepStatement.setDate(4, date_reg);
					prepStatement.setInt(5, Integer.parseInt(line[4]));
					prepStatement.setString(6, line[5]);

					prepStatement.executeUpdate();
				}
			}
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from plane");
			System.out.println("\nAfter the insert, data is...\n");
			while(resultSet.next()) {
			System.out.println(
				resultSet.getString(1) + ", " +
				resultSet.getString(2) + ", " +
				resultSet.getInt(3) + ", " +
				resultSet.getDate(4) + ", " + 
				resultSet.getInt(5) + ", " +
				resultSet.getString(6));
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
	public void generateManifest(String flightNumber, String flightDate)
	{
		//Perform data input checking
		if (flightNumber.length() > 0 && flightDate.length() > 0)
		{

			System.out.println("\nFetching manifest for flight # " + flightNumber + " on " + flightDate + " ...");
			try
			{
				query = "SELECT * FROM passengerManifest WHERE flight_number = ? AND flight_date = ?";
				prepStatement = connection.prepareStatement(query);
				prepStatement.setString(1, flightNumber+"");
				prepStatement.setString(2,flightDate+"");
				resultSet = prepStatement.executeQuery();

				System.out.format("%15s%15s%15s\n", new String[]{"Salutation", "First Name", "Last Name"});

				while (resultSet.next())
				{
					System.out.format("%15s%15s%15s\n", new String[]{resultSet.getString("Salutation"), resultSet.getString("First_Name"), resultSet.getString("Last_Name")});
				}

				System.out.println("\n\n\n");

				resultSet.close();

			} catch (Exception e) {
				System.out.print(e);
			} finally {
				try {
					if (statement != null) statement.close();
					if (prepStatement != null) prepStatement.close();
				} catch (SQLException e) {
					System.out.println("Cannot close Statement. Machine error: " + e.toString());
				}
			}
		}
		else
		{
			System.out.println("ERROR: Invalid entry of flight number or flight date. Aborting manifest generation\n\n\n");
		}
	}
	
	//Function: Client Privileges
	//inputs: none
	//outputs: none
	//Description: The client sub-menu for the overall application
	//Based on the action the user wants to do, it routes to the correct method
	//Will loop until user indicates exit
	public void ClientPrivileges()
	{
		int selection;
		boolean exit = false;
		String dc, ac, date1, airline;
		int dayOfWeek = 0;
		
		while(!exit)
		{
			System.out.print("Client Menu:\n\t1) Add customer\n\t2) Show customer info, given customer name\n\t3) Find Prices for flights between two cities\n\t4) Find all routes between two cities\n\t5) Find all routes between two cities of a given airline\n\t6) Find all routes with available seats \n\tbetween two cities on a given day\n\t7) For a given airline, find all routes with available seats\n\tbetween two cities on given day\n\t8) Add reservation\n\t9) Show reservation info, given reservation number\n\t10) Buy ticket from existing reservation\n\t11) Exit\nEnter the menu item number to continue: ");
			selection = Integer.parseInt(keyboard.nextLine());
			switch(selection)
			{
				case 1:
					String salutation, firstName, lastName, email, phone, ccnum, ccexpire, street, city, state, freqMiles;

					System.out.print("Please enter the customers salutation (Mr/Ms/Mrs): ");
					salutation = keyboard.nextLine();

					System.out.print("Please enter the customers first name: ");
					firstName = keyboard.nextLine();

					System.out.print("Please enter the customers last name: ");
					lastName = keyboard.nextLine();

					System.out.print("Please enter the customers email address: ");
					email = keyboard.nextLine();

					System.out.print("Please enter the customers phone number (no dashes): ");
					phone = keyboard.nextLine();

					System.out.print("Please enter the customers street address: ");
					street = keyboard.nextLine();

					System.out.print("Please enter the customers city: ");
					city = keyboard.nextLine();

					System.out.print("Please enter the customers state: ");
					state = keyboard.nextLine();

					System.out.print("Please enter the customers Credit Card number (no dashes) : ");
					ccnum = keyboard.nextLine();

					System.out.print("Please enter the customers Credit Card expiration date (ex 18-NOV-16): ");
					ccexpire = keyboard.nextLine();

					System.out.print("Please enter the airline ID of the customers frequent miles program: ");
					freqMiles = keyboard.nextLine();
					addCustomer(salutation, firstName, lastName, email, phone, ccnum, ccexpire, street, city, state, freqMiles);
					break;
				case 2:
					System.out.print("Please enter the customers first name: ");
					firstName = keyboard.nextLine();

					System.out.print("Please enter the customers last name: ");
					lastName = keyboard.nextLine();
					showCustomerInfo(firstName, lastName);
					break;
				case 3:
					String city1, city2;
					System.out.print("Please enter the three-letter airport code of the city: ");
					city1 = keyboard.nextLine().toUpperCase();
					System.out.print("Please enter the three-letter airport code of the city: ");
					city2 = keyboard.nextLine().toUpperCase();
					findPriceByRoute(city1, city2);
					break;
				case 4:
					System.out.print("Please enter the three-letter airport code of the DEPARTURE city: ");
					dc = keyboard.nextLine();
					System.out.print("Please enter the three-letter airport code of the ARRIVAL city: ");
					ac = keyboard.nextLine();
					findAllRoutes(dc,ac);
					break;
				case 5:
					System.out.print("Please enter the three-letter airport code of the DEPARTURE city: ");
					dc = keyboard.nextLine();
					System.out.print("Please enter the three-letter airport code of the ARRIVAL city: ");
					ac = keyboard.nextLine();
					System.out.print("Please enter the airline name: ");
					airline = keyboard.nextLine();
					findAllRoutesByAirline(dc,ac,airline);
					break;
				case 6:		
					System.out.print("Please enter the three-letter airport code of the DEPARTURE city: ");
					dc = keyboard.nextLine();
					System.out.print("Please enter the three-letter airport code of the ARRIVAL city: ");
					ac = keyboard.nextLine();
					System.out.print("Please enter the date you wish to fly in the format yyyy/MM/dd: ");
					date1 = keyboard.nextLine();
					try{
						java.util.Date date = new java.text.SimpleDateFormat("yyyy/MM/dd").parse(date1);
						dayOfWeek = date.getDay();
					}
					catch(Exception e){
						System.out.println("Date not entered in the correct format.");}
					findAvailableSeats(dc, ac, dayOfWeek, date1);
					break;
				case 7: 
					System.out.print("Please enter the three-letter airport code of the DEPARTURE city: ");
					dc = keyboard.nextLine();
					System.out.print("Please enter the three-letter airport code of the ARRIVAL city: ");
					ac = keyboard.nextLine();
					System.out.print("Please enter the date you wish to fly in the format yyyy/MM/dd: ");
					date1 = keyboard.nextLine();
					System.out.print("Please enter the name of the airline you wish to fly with: ");
					airline = keyboard.nextLine();
					try{
						java.util.Date date = new java.text.SimpleDateFormat("yyyy/MM/dd").parse(date1);
						dayOfWeek = date.getDay();
					}
					catch(Exception e){
						System.out.println("Date not entered in the correct format.");}
					findAvailableSeatsByAirline(dc, ac, dayOfWeek, date1,airline);
					break;
				case 8:
					int numFlights = 0, flightNum = -1;
					dayOfWeek = -1;
					String cid = "";
					ArrayList<Integer> flights = new ArrayList<Integer>();
					ArrayList<String> dates = new ArrayList<String>();
					ArrayList<Integer> dayOfWeeks = new ArrayList<Integer>();
					
					System.out.println("Please enter your customer id: ");
					cid = keyboard.nextLine();
					while(numFlights < 3 && flightNum != 0)
					{
						System.out.println("Please enter the flight number you wish to book: ");
						flightNum = Integer.parseInt(keyboard.nextLine());
						if(flightNum == 0){break;}
						flights.add(flightNum);
						System.out.println("Please enter the date you wish to fly in the format yyyy/MM/dd: ");
						date1 = keyboard.nextLine();
						try{
							java.util.Date date = new java.text.SimpleDateFormat("yyyy/MM/dd").parse(date1);
							dates.add(date1);
							dayOfWeek = date.getDay();
							dayOfWeeks.add(dayOfWeek);
						}
						catch(Exception e){
							System.out.println("Date not entered in the correct format.");}		
						numFlights++;
					}
					addReservation(cid, flights, dates, dayOfWeeks);
					break;
				case 9:
					String reservationNumber = "";
					System.out.print("Please enter the reservation number to generate manifest for: ");
					reservationNumber = keyboard.nextLine();
					showReservationByNumber(reservationNumber);
					break;
				case 10:
					String sel = "";
					System.out.print("Please enter the reservation number: ");
					sel = keyboard.nextLine();
					buyTicket(sel);
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
	public void addCustomer(String salutation, String firstName, String lastName, String email, String phone, String ccnum, String ccexpire, String street, String city, String  state, String freqMiles)
	{
		//Check to make sure there's not already a customer with that first and last name
		boolean nameExists = false;

		try
		{
            query = "SELECT * FROM Customer WHERE FIRST_NAME = ? AND LAST_NAME = ?";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setString(1, firstName+"");
			prepStatement.setString(2, lastName+"");
			resultSet = prepStatement.executeQuery();

			int i = 0;
			while (resultSet.next())
			{
			    if (i++ > 0)
				    nameExists = true;
			}
		} catch (Exception e) {
			System.out.print(e);
		} finally {
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: " + e.toString());
			}
		}

		if (nameExists)
		{
			System.out.println("ERROR! A customer with that first and last name already exist.\n\n\n");
		}
		else
		{
			int customerID = 0;

			try {
				callStatement = connection.prepareCall("{call generateCustomerID(?)}");
				callStatement.registerOutParameter(1, Types.INTEGER);
				callStatement.execute();
				customerID = callStatement.getInt(1);
				callStatement.close();

				System.out.println("Generated CID #" + customerID);
			}
		 	catch (Exception e)
			{
				System.out.print(e);
			} finally {
				try {
					if (statement != null) statement.close();
					if (callStatement != null) callStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: " + e.toString());
				}
			}

			//Insert
			try {
				query = "insert into Customer values (?,?,?,?,?,?,?,?,?,?,?,?)";
				prepStatement = connection.prepareStatement(query);

				prepStatement.setString(1, Integer.toString(customerID));
				prepStatement.setString(2, salutation);
				prepStatement.setString(3, firstName);
				prepStatement.setString(4, lastName);
				prepStatement.setString(5, ccnum);
				prepStatement.setString(6, ccexpire);
				prepStatement.setString(7, street);
				prepStatement.setString(8, city);
				prepStatement.setString(9, state);
				prepStatement.setString(10, phone);
				prepStatement.setString(11, email);
				prepStatement.setString(12, freqMiles);
				prepStatement.executeUpdate();
			}
			catch (Exception e)
				{
					System.out.print(e);
				} finally {
					try {
						if (statement != null) statement.close();
						if (callStatement != null) callStatement.close();
					} catch (SQLException e) {
						System.out.println("Cannot close Statement. Machine error: " + e.toString());
					}
				}


		}


	}
	
	//Function: showCustomerInfo
	//inputs: none
	//outputs: none
	/*Description:
	Show customer info, given customer name
	Ask the user to supply the customer name.
	Query the database and print all the information stored for the customer (do not display the
	information on reservations), including the PittRewards number i.e. the cid.*/
	public void showCustomerInfo(String firstName, String lastName)
	{
		try
		{
			query = "SELECT * FROM Customer WHERE FIRST_NAME = ? AND LAST_NAME = ?";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setString(1, firstName);
			prepStatement.setString(2, lastName);
			resultSet = prepStatement.executeQuery();

			if (resultSet.getFetchSize() != 0)
			{

				System.out.format("%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n", new String[]{"PittRewards #", "Salutation",  "First Name", "Last Name", "Street", "City", "State", "Phone", "Email", "FreqMiles#", "CC#", "CCExpire"});

				while (resultSet.next())
				{
					System.out.format("%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n", new String[]{resultSet.getString("CID"), resultSet.getString("Salutation"), resultSet.getString("First_Name"), resultSet.getString("Last_Name"), resultSet.getString("Street"), resultSet.getString("City"), resultSet.getString("State"), resultSet.getString("Phone"), resultSet.getString("Email"), resultSet.getString("Frequent_Miles"), resultSet.getString("Credit_Card_Num"), resultSet.getString("Credit_Card_Expire")});
				}

				System.out.println("\n\n\n");
			}
			else
			{
				System.out.println("ERROR: There was no customer with that name!\n\n\n");
			}

			resultSet.close();

		} catch (Exception e) {
			System.out.print(e);
		} finally {
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: " + e.toString());
			}
		}
	}
	
	//Function: findPriceByRoute
	//inputs: none
	//outputs: none
	/*Description:
	Ask the user to supply the two cities (city A and city B).
	Print the high and low prices for a one-way ticket from city A to city B, the prices for a
	one-way ticket from city B to city A, and the prices for a round-trip ticket between city A
	and city B.*/
	public void findPriceByRoute(String city1, String city2)
	{	
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
	Ask the user to supply the departure city and the arrival city.
	Query the schedule database
	and find all possible one-way routes between the given city combination.
	Print a list of
	flight number, departure, city, departure time, and arrival time for all routes.

	Direct routes are trivial.

	In addition to direct routes, we also allow routes with only one
	connection (i.e. two flights in the route). However, for a connection between two flights to
	be valid, both flights must be operating the same day at least once a week (when looking at
	their weekly schedules) and, also, the arrival time of the first flight must be at least one hour
	before the departure time of the second flight.

	Hint: For simplicity you may split this into two queries: one that finds and prints the direct
	routes, and one that finds and prints the routes with one connection.*/
	public void findAllRoutes(String dc, String ac)
	{
        try
        {
            ArrayList<Integer> flightNums = new ArrayList<Integer>();
            statement = connection.createStatement();

            //ALL DIRECT FLIGHTS
            query = "select flight_number, weekly_schedule from flight where departure_city = \'"+dc+"\' AND arrival_city = \'"+ac+"\'";
            resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                flightNums.add(resultSet.getInt(1));
            }
            resultSet.close();


            //Find all flights that have the start location or end location
            query = "select flight_number, departure_city, arrival_city, weekly_schedule from flight where departure_city = \'"+dc+"\' or arrival_city = \'"+ac+"\'";
            resultSet = statement.executeQuery(query);
            Hashtable<String,ArrayList<String>> connections = new Hashtable<String,ArrayList<String>>();
            ArrayList<Integer> connectFlightNumber = new ArrayList<Integer>();

            while(resultSet.next()) {
                String dctemp = resultSet.getString(2), actemp = resultSet.getString(3);
                int fn = resultSet.getInt(1);

                if(!dctemp.equals(dc))
                {
                    if(!connections.containsKey(dctemp))
                    {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(fn+",2");
                        connections.put(dctemp, temp);
                    }
                    else
                    {
                        ArrayList<String> temp = connections.get(dctemp);
                        temp.add(fn+",2");
                        connections.put(dctemp, temp);
                    }
                    connectFlightNumber.add(fn);
                }
                if(!actemp.equals(ac))
                {
                    if(!connections.containsKey(actemp))
                    {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(fn+",1");
                        connections.put(actemp, temp);
                    }
                    else
                    {
                        ArrayList<String> temp = connections.get(actemp);
                        temp.add(fn+",1");
                        connections.put(actemp, temp);
                    }
                    connectFlightNumber.add(fn);
                }
            }
            resultSet.close();

            //Check to see if the flights connect
            for(String key: connections.keySet())
            {
                boolean leg1 = false, leg2 = false;
                ArrayList<String> temp = connections.get(key);
                ArrayList<Integer> foo = new ArrayList<Integer>();
                for(String leg: temp)
                {
                    int fNum = Integer.parseInt(leg.split(",")[0]);
                    if(connectFlightNumber.contains(fNum))
                    {
                        foo.add(fNum);
                        if(leg.split(",")[1].equals("1"))
                            leg1 = true;
                        else if(leg.split(",")[1].equals("2"))
                            leg2 = true;

                        if(leg1 && leg2)
                            break;
                    }
                }
                if(!leg1 || !leg2)
                {
                    for(Integer i: foo)
                    {
                        connectFlightNumber.remove(i);
                    }
                }
            }

            //supply all the info for direct flights and connecting flights
            System.out.println("\nAll flights between " + dc + " and " + ac);
			System.out.format("%15s%15s%15s%15s%15s%15s2\n", new String[]{"Flight #", "Airline ID",  "Departure City", "Departure Time", "Arrival City", "Arrival Time"});

			if(flightNums.size() > 0)
			{
				System.out.println("\nDIRECT FLIGHTS: ");
				for(Integer i : flightNums)
				{
					query = "select flight_number, airline_id, departure_city, departure_time, arrival_city, arrival_time from flight where flight_number = \'"+i+"\'";
					resultSet = statement.executeQuery(query);
	
					if(resultSet.next()) {
						System.out.format("%15s%15s%15s%15s%15s%15s\n", new String[]{resultSet.getInt(1)+"", resultSet.getInt(2)+"",  resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)});
					}
					resultSet.close();
				}
			}
			else
			{
				System.out.println("THERE ARE NO DIRECT FLIGHTS.");
			}
			
			if(connectFlightNumber.size() > 0)
			{
				System.out.println("\nFLIGHTS WITH ONE CONNECTION: ");
				for(Integer i : connectFlightNumber)
				{
					query = "select flight_number, airline_id, departure_city, departure_time, arrival_city, arrival_time from flight where flight_number = \'"+i+"\'";
					resultSet = statement.executeQuery(query);

					if(resultSet.next()) {
						System.out.format("%15s%15s%15s%15s%15s%15s\n", new String[]{resultSet.getInt(1)+"", resultSet.getInt(2)+"",  resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)});
					}
					resultSet.close();
				}
			}
			else
			{
				System.out.println("THERE ARE NO FLIGHTS WITH ONE CONNECTION.");
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
	
	//Function: findAllRoutesByAirline
	//inputs: departure_city, arrival_city, airline
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
	public void findAllRoutesByAirline(String dc, String ac, String airline_name)
	{
		try
		{
			ArrayList<Integer> flightNums = new ArrayList<Integer>();
			statement = connection.createStatement();
			
			//find airline id
			int airline = 0;
			query = "select airline_id from airline where airline_name = \'"+airline_name+"\'";
			resultSet = statement.executeQuery(query);
			
			if(resultSet.next()) {
				airline = resultSet.getInt(1);
			}
			resultSet.close();

			//ALL DIRECT FLIGHTS
			query = "select flight_number, weekly_schedule from flight where departure_city = \'"+dc+"\' AND arrival_city = \'"+ac+"\' AND AIRLINE_ID = \'" + airline + "\'";
			resultSet = statement.executeQuery(query);

			while(resultSet.next()) {
				flightNums.add(resultSet.getInt(1));
			}
			resultSet.close();


			//Find all flights that have the start location or end location
			query = "select flight_number, departure_city, arrival_city, weekly_schedule from flight where (departure_city = \'"+dc+"\' or arrival_city = \'"+ac+"\') AND airline_id = \'" + airline + "\'";
			resultSet = statement.executeQuery(query);
			Hashtable<String,ArrayList<String>> connections = new Hashtable<String,ArrayList<String>>();
			ArrayList<Integer> connectFlightNumber = new ArrayList<Integer>();

			while(resultSet.next()) {
				String dctemp = resultSet.getString(2), actemp = resultSet.getString(3);
				int fn = resultSet.getInt(1);
				
				if(!dctemp.equals(dc))
				{
					if(!connections.containsKey(dctemp)) //if the table already has the city
					{
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(fn+",2");
						connections.put(dctemp, temp);
					}
					else
					{
						ArrayList<String> temp = connections.get(dctemp);
						temp.add(fn+",2");
						connections.put(dctemp, temp);
					}
					connectFlightNumber.add(fn);
				}
				if(!actemp.equals(ac))
				{
					if(!connections.containsKey(actemp))
					{
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(fn+",1");
						connections.put(actemp, temp);
					}
					else
					{
						ArrayList<String> temp = connections.get(actemp);
						temp.add(fn+",1");
						connections.put(actemp, temp);
					}
					connectFlightNumber.add(fn);
				}
			}
			resultSet.close();

			//Check to see if the flights connect
			for(String key: connections.keySet())
			{
				boolean leg1 = false, leg2 = false;
				ArrayList<String> temp = connections.get(key);
				ArrayList<Integer> foo = new ArrayList<Integer>();
				for(String leg: temp)
				{
					int fNum = Integer.parseInt(leg.split(",")[0]);
					if(connectFlightNumber.contains(fNum))
					{
						foo.add(fNum);
						if(leg.split(",")[1].equals("1"))
							leg1 = true;
						else if(leg.split(",")[1].equals("2"))
							leg2 = true;
						
						//there is a flight into and out of the connection city where the first city is 
						//departure city and last city is arrival city
						if(leg1 && leg2)
							break;
					}
				}
				if(!leg1 || !leg2) //if there isn't a flight into or a flight out of the connection city
				{
					for(Integer i: foo)
					{
						connectFlightNumber.remove(i);
					}
				}
			}

			//supply all the info for direct flights and connecting flights
			System.out.println("\nAll flights between " + dc + " and " + ac);
			System.out.format("%15s%15s%15s%15s%15s%15s2\n", new String[]{"Flight #", "Airline ID",  "Departure City", "Departure Time", "Arrival City", "Arrival Time"});
			
			if(flightNums.size() > 0)
			{
				System.out.println("\nDIRECT FLIGHTS: ");

				for(Integer i : flightNums)
				{
					query = "select flight_number, airline_id, departure_city, departure_time, arrival_city, arrival_time from flight where flight_number = \'"+i+"\'";
					resultSet = statement.executeQuery(query);
	
					if(resultSet.next()) {
						System.out.format("%15s%15s%15s%15s%15s%15s\n", new String[]{resultSet.getInt(1)+"", resultSet.getInt(2)+"",  resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)});
					}
					resultSet.close();
				}
			}
			else
			{
				System.out.println("THERE ARE NO DIRECT FLIGHTS.");
			}
			
			if(connectFlightNumber.size() > 0)
			{
				System.out.println("\nFLIGHTS WITH ONE CONNECTION: ");
				System.out.format("%15s%15s%15s%15s%15s%15s2\n", new String[]{"Flight #", "Airline ID",  "Departure City", "Departure Time", "Arrival City", "Arrival Time"});
				for(Integer i : connectFlightNumber)
				{
					query = "select flight_number, airline_id, departure_city, departure_time, arrival_city, arrival_time from flight where flight_number = \'"+i+"\'";
					resultSet = statement.executeQuery(query);

					if(resultSet.next()) {
						System.out.format("%15s%15s%15s%15s%15s%15s\n", new String[]{resultSet.getInt(1)+"", resultSet.getInt(2)+"",  resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)});
					}
					resultSet.close();
				}
			}
			else
			{
				System.out.println("THERE ARE NO FLIGHTS WITH ONE CONNECTION.");
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
	

	//Function: findAvailableSeatsByAirline
	//inputs: departure_city, arrival_city, day of the week, date of flight, airline
	//outputs: none
	/*Description:
	For a given airline, find all routes with available seats between two cities on given date
	Ask the user to supply the departure city, the arrival city, the date and the name of the airline.
	Same with the previous task, print a list of airline id, flight number, departure, city,
	departure time, and arrival time for all available routes.

	Note that this might be the most difficult query of the project. You need to build upon
	the previous task. You need to be careful for the case where we have a non-direct, oneconnection
	route and one of the two flights has available seats, while the other one does not*/
	public void findAvailableSeatsByAirline(String dc, String ac, int dayOfWeek, String date1, String airline)
	{	
		try
		{
			ArrayList<Integer> flightNums = new ArrayList<Integer>();
			statement = connection.createStatement();
			//find airline id
			int airline_id = 0;
			query = "select airline_id from airline where airline_name = \'"+airline+"\'";
			resultSet = statement.executeQuery(query);
			
			if(resultSet.next()) {
				airline_id = resultSet.getInt(1);
			}
			resultSet.close();
			
			//ALL DIRECT FLIGHTS
			query = "select flight_number, weekly_schedule from flight where (departure_city = \'"+dc+"\' AND arrival_city = \'"+ac+"\') AND airline_id = \'"+airline_id+"\'";
			resultSet = statement.executeQuery(query);
			
			while(resultSet.next()) {
				if(resultSet.getString(2).charAt(dayOfWeek) != '-')
				{
					flightNums.add(resultSet.getInt(1));
				}
			}
			resultSet.close();
		
			
			//Check to see if there is a seat available
			ArrayList<Integer> removeFlights = new ArrayList<Integer>();
			for(Integer i : flightNums)
			{
				int planeCap = 0;
				query = "select plane_capacity from flight natural join plane where flight_number = \'"+i+"\' AND airline_id = \'"+airline_id+"\'";
				resultSet = statement.executeQuery(query);
			
				if(resultSet.next()) {
					planeCap = resultSet.getInt(1);
				}
				resultSet.close();
				
				query = "select count(reservation_number) from flight natural join reservation_detail where flight_number = \'"+i+"\' and flight_date = TO_DATE(\'"+date1+"\',\'yyyy/MM/dd\')";
				resultSet = statement.executeQuery(query);
			
				if(resultSet.next()) {
					if(resultSet.getInt(1) >= planeCap)
					{
						removeFlights.add(i);
					}
				}
				resultSet.close();
			}
			flightNums.removeAll(removeFlights);
			
			//Find all flights that have the start location or end location
			query = "select flight_number, departure_city, arrival_city, weekly_schedule from flight where (departure_city = \'"+dc+"\' or arrival_city = \'"+ac+"\') and airline_id = \'"+airline_id+"\'";
			resultSet = statement.executeQuery(query);
			Hashtable<String,ArrayList<String>> connections = new Hashtable<String,ArrayList<String>>();
			ArrayList<Integer> connectFlightNumber = new ArrayList<Integer>(); 
			
			while(resultSet.next()) {
				String dctemp = resultSet.getString(2), actemp = resultSet.getString(3);
				int fn = resultSet.getInt(1);
				if(resultSet.getString(4).charAt(dayOfWeek) != '-')
				{
					if(!dctemp.equals(dc))
					{
						if(!connections.containsKey(dctemp))
						{
							ArrayList<String> temp = new ArrayList<String>();
							temp.add(fn+",2");
							connections.put(dctemp, temp);
						}
						else
						{
							ArrayList<String> temp = connections.get(dctemp);
							temp.add(fn+",2");
							connections.put(dctemp, temp);
						}
						connectFlightNumber.add(fn);
					}
					if(!actemp.equals(ac))
					{
						if(!connections.containsKey(actemp))
						{
							ArrayList<String> temp = new ArrayList<String>();
							temp.add(fn+",1");
							connections.put(actemp, temp);
						}
						else
						{
							ArrayList<String> temp = connections.get(actemp);
							temp.add(fn+",1");
							connections.put(actemp, temp);
						}
						connectFlightNumber.add(fn);
					}
				}
			}
			resultSet.close();
			
			//Check to see if there is a seat available
			removeFlights = new ArrayList<Integer>();
			for(Integer i : connectFlightNumber)
			{
				int planeCap = 0;
				query = "select plane_capacity from flight natural join plane where flight_number = \'"+i+"\'";
				resultSet = statement.executeQuery(query);
			
				if(resultSet.next()) {
					planeCap = resultSet.getInt(1);
				}
				resultSet.close();
				
				query = "select count(reservation_number) from flight natural join reservation_detail where flight_number = \'"+i+"\' and flight_date = TO_DATE(\'"+date1+"\',\'yyyy/MM/dd\')";
				resultSet = statement.executeQuery(query);
			
				if(resultSet.next()) {
					if(resultSet.getInt(1) >= planeCap)
					{
						removeFlights.add(i);
					}
				}
				resultSet.close();
			}
			connectFlightNumber.removeAll(removeFlights);
			
			//Check to see if the flights connect
			for(String key: connections.keySet())
			{
				boolean leg1 = false, leg2 = false;
				ArrayList<String> temp = connections.get(key);
				ArrayList<Integer> foo = new ArrayList<Integer>();
				for(String leg: temp)
				{
					int fNum = Integer.parseInt(leg.split(",")[0]);
					if(connectFlightNumber.contains(fNum))
					{
						foo.add(fNum);
						if(leg.split(",")[1].equals("1"))
							leg1 = true;
						else if(leg.split(",")[1].equals("2"))
							leg2 = true;
							
						if(leg1 && leg2)
							break;
					}
				}
				if(!leg1 || !leg2)
				{
					for(Integer i: foo)
					{
						connectFlightNumber.remove(i);
					}
				}
			}
			
			//supply all the info for direct flights and connecting flights
			System.out.println("\nAll flights with available seats:\nflight number, airline id, departure_city, arrival_city, departure_time, arrival_time");
			
			if(flightNums.size() > 0)
			{
				System.out.println("\nDIRECT FLIGHTS:");
				for(Integer i : flightNums)
				{
					query = "select flight_number, airline_id, departure_city, arrival_city, departure_time, arrival_time from flight where flight_number = \'"+i+"\'";
					resultSet = statement.executeQuery(query);
			
					if(resultSet.next()) {
						System.out.println(resultSet.getInt(1)+" , "+ 
										resultSet.getInt(2)+" , "+
										resultSet.getString(3)+" , "+
										resultSet.getString(4)+" , "+
										resultSet.getString(5)+" , "+
										resultSet.getString(6));
					}
					resultSet.close();
				}
			}
			else
			{
				System.out.println("THERE ARE NO DIRECT FLIGHTS.");
			}
			
			if(connectFlightNumber.size() > 0)
			{
				System.out.println("\nFLIGHTS WITH ONE CONNECTION:");
				for(Integer i : connectFlightNumber)
				{
					query = "select flight_number, airline_id, departure_city, arrival_city, departure_time, arrival_time from flight where flight_number = \'"+i+"\'";
					resultSet = statement.executeQuery(query);
			
					if(resultSet.next()) {
						System.out.println(resultSet.getInt(1)+" , "+ 
										resultSet.getInt(2)+" , "+
										resultSet.getString(3)+" , "+
										resultSet.getString(4)+" , "+
										resultSet.getString(5)+" , "+
										resultSet.getString(6));
					}
					resultSet.close();
				}
			}
			else
			{
				System.out.println("THERE ARE NO FLIGHTS WITH ONE CONNECTION.");
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
	
	//Function: findAvailableSeats
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
	public void findAvailableSeats(String dc, String ac, int dayOfWeek, String date1)
	{
		try
		{
			ArrayList<Integer> flightNums = new ArrayList<Integer>();
			statement = connection.createStatement();
			
			//ALL DIRECT FLIGHTS
			query = "select flight_number, weekly_schedule from flight where departure_city = \'"+dc+"\' AND arrival_city = \'"+ac+"\'";
			resultSet = statement.executeQuery(query);
			
			while(resultSet.next()) {
				if(resultSet.getString(2).charAt(dayOfWeek) != '-')
				{
					flightNums.add(resultSet.getInt(1));
				}
			}
			resultSet.close();
		
			
			//Check to see if there is a seat available
			ArrayList<Integer> removeFlights = new ArrayList<Integer>();
			for(Integer i : flightNums)
			{
				int planeCap = 0;
				query = "select plane_capacity from flight natural join plane where flight_number = \'"+i+"\'";
				resultSet = statement.executeQuery(query);
			
				if(resultSet.next()) {
					planeCap = resultSet.getInt(1);
				}
				resultSet.close();
				
				query = "select count(reservation_number) from flight natural join reservation_detail where flight_number = \'"+i+"\' and flight_date = TO_DATE(\'"+date1+"\',\'yyyy/MM/dd\')";
				resultSet = statement.executeQuery(query);
			
				if(resultSet.next()) {
					if(resultSet.getInt(1) >= planeCap)
					{
						removeFlights.add(i);
					}
				}
				resultSet.close();
			}
			flightNums.removeAll(removeFlights);
			
			//Find all flights that have the start location or end location
			query = "select flight_number, departure_city, arrival_city, weekly_schedule from flight where departure_city = \'"+dc+"\' or arrival_city = \'"+ac+"\'";
			resultSet = statement.executeQuery(query);
			Hashtable<String,ArrayList<String>> connections = new Hashtable<String,ArrayList<String>>();
			ArrayList<Integer> connectFlightNumber = new ArrayList<Integer>(); 
			
			while(resultSet.next()) {
				String dctemp = resultSet.getString(2), actemp = resultSet.getString(3);
				int fn = resultSet.getInt(1);
				if(resultSet.getString(4).charAt(dayOfWeek) != '-')
				{
					if(!dctemp.equals(dc))
					{
						if(!connections.containsKey(dctemp))
						{
							ArrayList<String> temp = new ArrayList<String>();
							temp.add(fn+",2");
							connections.put(dctemp, temp);
						}
						else
						{
							ArrayList<String> temp = connections.get(dctemp);
							temp.add(fn+",2");
							connections.put(dctemp, temp);
						}
						connectFlightNumber.add(fn);
					}
					if(!actemp.equals(ac))
					{
						if(!connections.containsKey(actemp))
						{
							ArrayList<String> temp = new ArrayList<String>();
							temp.add(fn+",1");
							connections.put(actemp, temp);
						}
						else
						{
							ArrayList<String> temp = connections.get(actemp);
							temp.add(fn+",1");
							connections.put(actemp, temp);
						}
						connectFlightNumber.add(fn);
					}
				}
			}
			resultSet.close();
			
			//Check to see if there is a seat available
			removeFlights = new ArrayList<Integer>();
			for(Integer i : connectFlightNumber)
			{
				int planeCap = 0;
				query = "select plane_capacity from flight natural join plane where flight_number = \'"+i+"\'";
				resultSet = statement.executeQuery(query);
			
				if(resultSet.next()) {
					planeCap = resultSet.getInt(1);
				}
				resultSet.close();
				
				query = "select count(reservation_number) from flight natural join reservation_detail where flight_number = \'"+i+"\' and flight_date = TO_DATE(\'"+date1+"\',\'yyyy/MM/dd\')";
				resultSet = statement.executeQuery(query);
			
				if(resultSet.next()) {
					if(resultSet.getInt(1) >= planeCap)
					{
						removeFlights.add(i);
					}
				}
				resultSet.close();
			}
			connectFlightNumber.removeAll(removeFlights);
			
			//Check to see if the flights connect
			for(String key: connections.keySet())
			{
				boolean leg1 = false, leg2 = false;
				ArrayList<String> temp = connections.get(key);
				ArrayList<Integer> foo = new ArrayList<Integer>();
				for(String leg: temp)
				{
					int fNum = Integer.parseInt(leg.split(",")[0]);
					if(connectFlightNumber.contains(fNum))
					{
						foo.add(fNum);
						if(leg.split(",")[1].equals("1"))
							leg1 = true;
						else if(leg.split(",")[1].equals("2"))
							leg2 = true;
							
						if(leg1 && leg2)
							break;
					}
				}
				if(!leg1 || !leg2)
				{
					for(Integer i: foo)
					{
						connectFlightNumber.remove(i);
					}
				}
			}
			
			//supply all the info for direct flights and connecting flights
			System.out.println("\nAll flights with available seats:\nflight number, airline id, departure_city, arrival_city, departure_time, arrival_time");
			
			if(flightNums.size() > 0)
			{
				System.out.println("\nDIRECT FLIGHTS: ");
				for(Integer i : flightNums)
				{
					query = "select flight_number, airline_id, departure_city, arrival_city, departure_time, arrival_time from flight where flight_number = \'"+i+"\'";
					resultSet = statement.executeQuery(query);
			
					if(resultSet.next()) {
						System.out.println(resultSet.getInt(1)+" , "+ 
										resultSet.getInt(2)+" , "+
										resultSet.getString(3)+" , "+
										resultSet.getString(4)+" , "+
										resultSet.getString(5)+" , "+
										resultSet.getString(6));
					}
					resultSet.close();
				}
			}
			else
			{
				System.out.println("THERE ARE NO DIRECT FLIGHTS.");
			}
				
			if(connectFlightNumber.size() > 0)
			{
				System.out.println("\nFLIGHTS WITH ONE CONNECTION: ");
				for(Integer i : connectFlightNumber)
				{
					query = "select flight_number, airline_id, departure_city, arrival_city, departure_time, arrival_time from flight where flight_number = \'"+i+"\'";
					resultSet = statement.executeQuery(query);
			
					if(resultSet.next()) {
						System.out.println(resultSet.getInt(1)+" , "+ 
										resultSet.getInt(2)+" , "+
										resultSet.getString(3)+" , "+
										resultSet.getString(4)+" , "+
										resultSet.getString(5)+" , "+
										resultSet.getString(6));
					}
					resultSet.close();
				}
			}
			else
			{
				System.out.println("THERE ARE NO FLIGHTS WITH ONE CONNECTION.");
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
	public void addReservation(String cid, ArrayList<Integer> flights, ArrayList<String> dates, ArrayList<Integer> dayOfWeeks)
	{
		boolean cannotReserve = false; //if we can't book one or more of the flights, we cannot make the reservation
		String creditNum = "",airline_id = "", plane = "", sc = "", ec = "";
		java.sql.Date currentDate = java.sql.Date.valueOf(dates.get(0).replace("/","-"));
		int numReservations = 0, rnum = 0;
		//Perform data input checking
		if (flights.size() > 0)
		{
			System.out.println("\nAttempting to make a new reservation...");
			try
			{
				//isolating the reservation
				connection.setAutoCommit(false);
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				statement = connection.createStatement();
				
				for(int i = 0; i < flights.size() && !cannotReserve; i++)
				{
					//check to see that the flight number actually exists
					query = "SELECT airline_id, weekly_schedule FROM flight WHERE Flight_Number = ?";
					prepStatement = connection.prepareStatement(query);
					prepStatement.setString(1, flights.get(i)+"");
					resultSet = prepStatement.executeQuery();
					
					if (resultSet.getFetchSize() != 0)
					{
						if(resultSet.next())
						{
							airline_id = resultSet.getString(1);
							String ws = resultSet.getString(2);
							if(ws.charAt(dayOfWeeks.get(i)) == '-')
							{
								connection.rollback();
								throw new Exception("ERROR: The flight you want does not fly on that day of the week!\n\n");
							}
						}
					}
					else
					{
						connection.rollback();
						throw new Exception("ERROR: There were no flights found for that flight number!\n\n\n");
					}
					resultSet.close();
					
					//check to see if they are a registered customer
					query = "SELECT * FROM customer WHERE cid = ?";
					prepStatement = connection.prepareStatement(query);
					prepStatement.setString(1, cid);
					resultSet = prepStatement.executeQuery();

					if (resultSet.getFetchSize() != 0)
					{
						if(resultSet.next()) {
							creditNum = resultSet.getString("CREDIT_CARD_NUM");
						}	
					}
					else
					{
						connection.rollback();
						throw new Exception("ERROR: You must register as a customer before you can book a flight!\n\n\n");
					}
					resultSet.close();
					
					//select count from reservation_detail where flight_number = ? and flight_date = ?
					query = "select count(*) from flight natural join reservation_detail where flight_number = \'"+flights.get(i)+"\' and flight_date = TO_DATE(\'"+dates.get(i)+"\',\'yyyy/MM/dd\')";
					resultSet = statement.executeQuery(query);
			
					if(resultSet.next()) {
						numReservations = resultSet.getInt(1);
					}
					resultSet.close();
					
					//find plane capacity
					int planeCap = 0;
					query = "select plane_capacity from flight natural join plane where flight_number = \'"+flights.get(i)+"\'";
					resultSet = statement.executeQuery(query);
			
					if(resultSet.next()) {
						planeCap = resultSet.getInt(1);
					}
					resultSet.close();
					
					if(numReservations >= planeCap)
					{
						//Used Oracle's docs online to find out how to call a sql function from java
						//https://docs.oracle.com/cd/E17952_01/connector-j-en/connector-j-usagenotes-statements-callable.html
						callStatement = connection.prepareCall("{? = call findPlane(?,?)}");
						
						callStatement.registerOutParameter(1, Types.VARCHAR);
						callStatement.setInt(2, numReservations);
						callStatement.setString(3, airline_id);
						callStatement.execute();
						plane = callStatement.getString(1);
						callStatement.close();	
						
						if(plane == null)
						{
							connection.rollback();
							throw new Exception("Cannot complete reservation. There aren't enough seats on flight "+flights.get(i));
						}
					}
				}
				
				//generate a unique reservation_number
				query = "select max(reservation_number) from reservation";
				resultSet = statement.executeQuery(query);
			
				if(resultSet.next()) {
					rnum = resultSet.getInt(1) + 1;
				}
				resultSet.close();
					
				//find start_city
				query = "select departure_city from flight where flight_number = \'"+flights.get(0)+"\'";
				resultSet = statement.executeQuery(query);
				if(resultSet.next()) {
					sc = resultSet.getString(1);
				}
				resultSet.close();
					
				//find end_city
				query = "select arrival_city from flight where flight_number = \'"+flights.get(flights.size()-1)+"\'";
				resultSet = statement.executeQuery(query);
				if(resultSet.next()) {
					ec = resultSet.getString(1);
				}
				resultSet.close();
				
				//find current date
				query = "select C_Date from PDate";
				resultSet = statement.executeQuery(query);
				if(resultSet.next()) {
					currentDate = resultSet.getDate(1);
				}
				resultSet.close();
					
				query = "insert into reservation(Reservation_Number, CID, Start_City, End_City, Cost, Credit_Card_Num, Reservation_Date, Ticketed) values (?,?,?,?,?,?,?,?)";
				prepStatement = connection.prepareStatement(query);
				prepStatement.setString(1, rnum+"");
				prepStatement.setString(2, cid);
				prepStatement.setString(3, sc);
				prepStatement.setString(4, ec);
				prepStatement.setString(5, ""+0);//there is a trigger that calculates cost
				prepStatement.setString(6, creditNum);
				prepStatement.setDate(7, currentDate);
				prepStatement.setString(8, "N"); // not ticketed
				prepStatement.executeUpdate();
					
				for(int i = 0; i < flights.size(); i++)
				{
					query = "insert into reservation_detail(Reservation_Number, Flight_Number, Flight_Date, Leg) values ("+rnum+","+flights.get(i)+",TO_DATE(\'"+dates.get(i)+"\',\'yyyy/MM/dd\'),"+i+")";
					statement.executeUpdate(query);
					
				}
				connection.commit();
				System.out.println("Booking flight was successful. Your reservation Number is: "+rnum);

			} catch (Exception e) {
				System.out.print(e);
			} finally {
				try {
					if (statement != null) statement.close();
					if (prepStatement != null) prepStatement.close();
					if (callStatement != null) callStatement.close();
					
				} catch (SQLException e) {
					System.out.println("Cannot close Statement. Machine error: " + e.toString());
				}
			}
		}
		else
		{
			System.out.println("ERROR: No flights were selected. Returning to menu...\n\n\n");
		}
	}
	
	//Function: showReservationByNumber
	//inputs: the reservation number
	//outputs: none
	/*Description:
	Ask the user to supply the reservation number.
	Query the database to get all the flights for the given reservation and print this to the user.
	Print an error message in case of an non-existent reservation number.*/
	public void showReservationByNumber(String reservationNumber)
	{
		//Perform data input checking
		if (reservationNumber.length() > 0)
		{

			System.out.println("\nFetching reservation flight reservation # " + reservationNumber + " ...");
			try
			{
				query = "SELECT * FROM ReservationFlightInfo WHERE Reservation_Number = ?";
				prepStatement = connection.prepareStatement(query);
				prepStatement.setString(1, reservationNumber+"");
				resultSet = prepStatement.executeQuery();

				if (resultSet.getFetchSize() != 0)
				{

					System.out.format("%15s%15s%15s%15s%15s\n", new String[]{"Flight #", "Dept City", "Dept Time", "Arr City", "Arr Time"});

					while (resultSet.next()) {
						System.out.format("%15s%15s%15s%15s%15s\n", new String[]{resultSet.getString("FLIGHT_NUMBER"), resultSet.getString("DEPARTURE_CITY"), resultSet.getString("DEPARTURE_TIME"), resultSet.getString("ARRIVAL_CITY"), resultSet.getString("ARRIVAL_TIME")});
					}

					System.out.println("\n\n\n");
				}
				else
				{
					System.out.println("ERROR: There were no flights found for that reservation number!\n\n\n");
				}

				resultSet.close();

			} catch (Exception e) {
				System.out.print(e);
			} finally {
				try {
					if (statement != null) statement.close();
					if (prepStatement != null) prepStatement.close();
				} catch (SQLException e) {
					System.out.println("Cannot close Statement. Machine error: " + e.toString());
				}
			}
		}
		else
		{
			System.out.println("ERROR: Invalid entry of flight number. Aborting reservation info lookup\n\n\n");
		}
	}
	
	//Function: buyTicket
	//inputs: none
	//outputs: none
	/*Description:
	Ask the user to supply the reservation number.
	Mark the fact that the reservation was converted into a purchased ticket*/
	public void buyTicket(String selection)
	{
		int updatedRows = 0;
		//Ensuring reservation number is within length constraints
		if(selection.length() > 0 && selection.length() <= 5)
		{
			System.out.println("Attempting to mark reservation " + selection + " as purchased...");
			try
			{
				callStatement = connection.prepareCall("{call purchaseTicket(?,?)}");
				callStatement.setString(1, selection);
				callStatement.registerOutParameter(2, Types.INTEGER);
				callStatement.execute();
				updatedRows = callStatement.getInt(2);
				callStatement.close();

			}
			catch(Exception e)
			{
				System.out.println("Encountered an unexpected error while attempting to mark reservation as purchased:");
				System.out.print(e);
			}
			finally
			{
				try
				{
					if (statement != null) statement.close();
					if (callStatement != null) callStatement.close();
				}
				catch (SQLException e)
				{
					System.out.println("Cannot close Statement. Machine error: "+e.toString());
				}
			}

			if (updatedRows == 1)
			{
				System.out.println("Reservation marked as purchased SUCCESSFULLY!");
			}
			else if (updatedRows == 0)
			{
				System.out.println("Reservation marked as purchased FAILED! That reservation does not exist");
			}
			else
			{
				System.out.println("Reservation marked as purchased FAILED! Number of rows updated: " + updatedRows);
			}

		}
		else
		{
			System.out.println("ERROR: Reservation numbers are between 1 and 5 characters. Returning to main menu....\n\n\n");
		}
	}

	public static void main(String[] args) throws SQLException
	{		
		/*NOTE: the majority of the database setup code is from recitation 8 TranDemo1*/

		System.out.println("Welcome to PittFlights...");

		String username,password;
		username = "ahf5"; //MUST EDIT THIS BEFORE RUNNING -- put in your pitt username/password
		password = "13+SCtoPITT+17";
		
		try
		{
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
			System.out.println("Error connecting to database.  Machine Error: ");

			Ex.printStackTrace();
		}
		finally
		{
			connection.close();
		}
	}
}