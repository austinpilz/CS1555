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
	public void generateManifest()
	{
		String flightNumber = "";
		String flightDate = "";

		System.out.print("Please enter the flight number to generate manifest for: ");
		flightNumber = keyboard.nextLine();

		System.out.print("Please enter the flight date to generate manifest in the format (DD-MM-YY): ");
		flightDate = keyboard.nextLine();

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
					findAvailableSeats();
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
	public void showCustomerInfo()
	{
		String firstName, lastName;

		System.out.print("Please enter the customers first name: ");
		firstName = keyboard.nextLine();

		System.out.print("Please enter the customers last name: ");
		lastName = keyboard.nextLine();

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
	public void findPriceByRoute()
	{
		String city1, city2;
		System.out.print("Please enter the three-letter airport code of the city: ");
		city1 = keyboard.nextLine().toUpperCase();
		System.out.print("Please enter the three-letter airport code of the city: ");
		city2 = keyboard.nextLine().toUpperCase();
		
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
	public void findAllRoutes()
	{
        //USER INPUT
        String dc, ac, date1;
        int dayOfWeek = 0;

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
            System.out.println(e);}

        //DB INTERACTIONS
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
            System.out.println("All flights between " + dc + " and " + ac);


			System.out.format("%15s%15s%15s%15s%15s%15s2\n", new String[]{"Flight #", "Airline ID",  "Departure City", "Departure Time", "Arrival City", "Arrival Time"});

			for(Integer i : flightNums)
            {
                query = "select flight_number, airline_id, departure_city, departure_time, arrival_city, arrival_time from flight where flight_number = \'"+i+"\'";
                resultSet = statement.executeQuery(query);

                if(resultSet.next()) {
					System.out.format("%15s%15s%15s%15s%15s%15s\n", new String[]{resultSet.getInt(1)+"", resultSet.getInt(2)+"",  resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)});
                }
                resultSet.close();
            }

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
		//USER INPUT
		String dc, ac, date1, airline;
		int dayOfWeek = 0;

		System.out.print("Please enter the three-letter airport code of the DEPARTURE city: ");
		dc = keyboard.nextLine();
		System.out.print("Please enter the three-letter airport code of the ARRIVAL city: ");
		ac = keyboard.nextLine();
		System.out.print("Please enter the airline ID: ");
		airline = keyboard.nextLine();
		System.out.print("Please enter the date you wish to fly in the format yyyy/MM/dd: ");
		date1 = keyboard.nextLine();
		try{
			java.util.Date date = new java.text.SimpleDateFormat("yyyy/MM/dd").parse(date1);
			dayOfWeek = date.getDay();
		}
		catch(Exception e){
			System.out.println(e);}

		//DB INTERACTIONS
		try
		{
			ArrayList<Integer> flightNums = new ArrayList<Integer>();
			statement = connection.createStatement();

			//ALL DIRECT FLIGHTS
			query = "select flight_number, weekly_schedule from flight where departure_city = \'"+dc+"\' AND arrival_city = \'"+ac+"\' AND AIRLINE_ID = \'" + airline + "\'";
			resultSet = statement.executeQuery(query);

			while(resultSet.next()) {
				if(resultSet.getString(2).charAt(dayOfWeek) != '-')
				{
					flightNums.add(resultSet.getInt(1));
				}
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
			System.out.println("All flights between " + dc + " and " + ac);


			System.out.format("%15s%15s%15s%15s%15s%15s2\n", new String[]{"Flight #", "Airline ID",  "Departure City", "Departure Time", "Arrival City", "Arrival Time"});

			for(Integer i : flightNums)
			{
				query = "select flight_number, airline_id, departure_city, departure_time, arrival_city, arrival_time from flight where flight_number = \'"+i+"\'";
				resultSet = statement.executeQuery(query);

				if(resultSet.next()) {
					System.out.format("%15s%15s%15s%15s%15s%15s\n", new String[]{resultSet.getInt(1)+"", resultSet.getInt(2)+"",  resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)});
				}
				resultSet.close();
			}

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
		//USER INPUT
		String dc, ac, date1;
		int dayOfWeek = 0;
		String airline;
		int airline_id = 0;
		
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
			System.out.println(e);}
		
		//DB INTERACTIONS
		try
		{
			ArrayList<Integer> flightNums = new ArrayList<Integer>();
			statement = connection.createStatement();
			//find airline id
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
			System.out.println("All flights with available seats:\nflight number, airline id, departure_city, arrival_city, departure_time, arrival_time");
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
	public void findAvailableSeats()
	{
		//USER INPUT
		String dc, ac, date1;
		int dayOfWeek = 0;
		
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
			System.out.println(e);}
		
		//DB INTERACTIONS
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
			System.out.println("All flights with available seats:\nflight number, airline id, departure_city, arrival_city, departure_time, arrival_time");
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
	public void addReservation()
	{
		System.out.println("COMING SOON");
		/*int[] flight = new int[4];
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy/MM/dd");
		java.sql.Date[] date_reg = new java.sql.Date[4];
		
		try{
		System.out.print("Please enter the flight number of first leg: ");
		flight[0] = Integer.parseInt(keyboard.nextLine());
		
		if(flight[0] != 0)
		{
		System.out.print("Please enter the date of the flight in format (yyyy-mm-dd): ");
		date_reg[0] = df.parse(keyboard.nextLine());
		System.out.println("DATE: "+date_reg[0]);
		System.out.print("Please enter the flight number of second leg: ");
		flight[1] = Integer.parseInt(keyboard.nextLine());
		if(flight[1] != 0)
		{
			System.out.print("Please enter the date of the flight in format (yyyy-mm-dd): ");
			date_reg[1] = df.parse(keyboard.nextLine());
			System.out.print("Please enter the flight number of third leg: ");
			flight[2] = Integer.parseInt(keyboard.nextLine());
			
			if(flight[2] != 0)
			{
				System.out.print("Please enter the date of the flight in format (yyyy-mm-dd): ");
				date_reg[2] = df.parse(keyboard.nextLine());
				System.out.print("Please enter the flight number of fourth leg: ");
				flight[3] = Integer.parseInt(keyboard.nextLine());
				
				if(flight[3] != 0)
				{
					System.out.print("Please enter the date of the flight in format (yyyy-mm-dd): ");
					date_reg[3] = df.parse(keyboard.nextLine());
				}
			}
		}
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}*/
		
		//TO DO: DB PART
	}
	
	//Function: showReservationByNumber
	//inputs: the reservation number
	//outputs: none
	/*Description:
	Ask the user to supply the reservation number.
	Query the database to get all the flights for the given reservation and print this to the user.
	Print an error message in case of an non-existent reservation number.*/
	public void showReservationByNumber()
	{
		String reservationNumber = "";

		System.out.print("Please enter the reservation number to generate manifest for: ");
		reservationNumber = keyboard.nextLine();

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
	public void buyTicket()
	{
		String selection = "";
		int updatedRows = 0;
		System.out.print("Please enter the reservation number: ");
		selection = keyboard.nextLine();

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
		username = "username"; //MUST EDIT THIS BEFORE RUNNING -- put in your pitt username/password
		password = "password";
		
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