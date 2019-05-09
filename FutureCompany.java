import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class FutureCompany{
    public static void main(String[] args) throws SQLException {
		// connect to database
    	 String hostName = "yu1357-sql-server.database.windows.net";
         String dbName = "individual-project";
         String user = "yu1357";
         String password = "Yx28408483";
         String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
         
         
         

		try(final Connection test = DriverManager.getConnection(url)){
			final String schema = test.getSchema();
			System.out.print("Successful connected to  - Schema: " + schema);
			System.out.println("");
		}
		catch(Exception e){
			System.out.println("Unable to access the database!");
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		Connection connection = DriverManager.getConnection(url);
		Scanner scan = new Scanner(System.in);
		String menu = 
		        "WELCOME TO THE Future Inc. DATABASE SYSTEM.\n" 
		        + "Please enter the number to the corresponding task you want to run.\n" 
		        + "\n" 
		        + "(1)  Enter a new employee into the database.\n" 
		        + "(2)  Enter a new product into database associated with the person who made the product, repaired the product if it is repaired, or checked the product.\n" 
		        + "(3)  Enter a customer into database associated with some products.\n"
		        + "(4)  Create a new account associated with a product into databse.\n" 
		        + "(5)  Enter a complaint associated with a customer and product.\n"
		        + "(6)  Enter  an accident associated with appropriate employee and product.\n"
		        + "(7)  Retrieve the date produced and time spent to produce a particular product.\n"
		        + "(8)  Retrieve all products  made by  a  particular worker.\n"
		        + "(9)  Retrieve the total number of errors a particular quality controller made.  This is the total number of  products certified by this controller and got some complaints.\n"
		        + "(10) Retrieve the total costs  of the products in the product3 category  which were repaired at the request of  a particular quality controller.\n" 
		        + "(11) Retrieve all customers who purchased all products of a particular color.\n"
		        + "(12) Retrieve  the total number of work days lost due to accidents in repairing  the products which got complaints.\n"
		        + "(13) Retrieve all customers who are also  workers.\n"
		        + "(14) Retrieve all the customers who have purchased the products made or certified or repaired by themselves.\n"
		        + "(15) Retrieve  the average cost of  all products made in a particular year.\n" 
		        + "(16) Switch the position between a technical staff and a quality controller.\n" 
		        + "(17) Delete all accidents whose dates are in some range.\n" 
		        + "(18) Import: enter new customers from a data file until the file is empty (the user must be asked to enter the input file name).\n"
		        + "(19) Export: Retrieve all customers (in name order) and output them to a data file instead of screen (the user must be asked to enter the output file name).\n"
		        + "(20) Quit.";
		        System.out.println("\n" + menu + "\n");

		        while(true){
		        	System.out.println("Please enter the number to the corresponding task you want to run.");
		        	int task = Integer.parseInt(scan.nextLine());
		        	String[] arg={};
		        	String input;
		        	switch (task){
		        		case 1:
		        			System.out.println("Enter the type(number) of employee: 1. Technical, 2. Quality Controller, 3. Worker(Producer)");
		        			switch(Integer.parseInt(scan.nextLine())){
		        				case 1:
		        					System.out.println("Enter your input in follwing format: Technical name, adress, dgree, technical position");
		        					input = scan.nextLine();
		        					arg = input.split(", ");
		        					query1(connection, arg, 1);
		        					break;
		        				case 2:
		        					System.out.println("Enter your input in follwing format: Quality Controller name, adress, product type(type like product # ex. product 1)");
		        					input = scan.nextLine();
		        					arg = input.split(", ");
		        					query1(connection, arg, 2);
		        					break;
		        				case 3:
		        					System.out.println("Enter your input in follwing format: Worker name, adress, max produces/day");
		        					input = scan.nextLine();
		        					arg = input.split(", ");
		        					query1(connection, arg, 3);
		        					break;
		        				default:
			        				System.out.println("Fail to insert data! please try again!");
			        				break;
		
		        			}
		        			break;
		        		case 2:
		        			System.out.println("Enter the type(number) of product: 1 for product1, 2 for product2, 3 for product3)");
		        			String pid = "";	        					
		        			switch(Integer.parseInt(scan.nextLine())){		        					        				
		        				case 1:
		        					System.out.println("Enter your input in follwing format: ");
		        					System.out.println("product ID, produced date(YYYY-MM-DD), producing duration(days), producer name, tester name, size, account number, cost, sotfware name major used");
		        					input = scan.nextLine();
		        					arg = input.split(", ");
		        					pid = arg[0];
		        					query2(connection, arg, 1);
		        					break;
		        				case 2:
		        					System.out.println("Enter your input in follwing format: ");
		        					System.out.println("product ID, produced date(YYYY-MM-DD), producing duration(days), producer name, tester name, size, account number, cost, color");
		        					input = scan.nextLine();
		        					arg = input.split(", ");
		        					pid = arg[0];
		        					query2(connection, arg, 2);
		        					break;
		        				case 3:
		        					System.out.println("Enter your input in follwing format: ");
		        					System.out.println("product ID, produced date(YYYY-MM-DD), producing duration(days), producer name, tester name, size, account number, cost, weight");
		        					input = scan.nextLine();
		        					arg = input.split(", ");
		        					pid = arg[0];
		        					query2(connection, arg, 3);
		        					break;
		        				default:
			        				System.out.println("Fail to insert data! please try again!");
			        				break;

		        			}
		        			if(pid.length() > 0){		        				        			
		        				System.out.println("Is the product repaired? 1 for yes, 0 for no");	        			
		        				if(Integer.parseInt(scan.nextLine())== 1){			        			
		        					System.out.println("Enter your input in follwing format: fixer name, fixed date(YYYY-MM-DD), requested by (type: complaint/controller)");
		        					input = pid + ", " + scan.nextLine();			 		        			
		        					arg = input.split(", ");
		        					query2(connection, arg, 4);
		        				}		        				
		        			}
		        			break;
		        			
		        		case 3:
        					System.out.println("Enter your input in follwing format: customer name, adress, product ID");
        					input = scan.nextLine();
        					arg = input.split(", ");
        					query3(connection, arg);
		        			break;
		        			
		        		case 4:		        			
		        			System.out.println("What type of account you want to create? (1 for product1-account, 2 for product2-account, 3 for  product3-account)");
		        			int temp = Integer.parseInt(scan.nextLine());
		        			System.out.println("Enter your input in follwing format: account number, account established date(YYYY-MM-DD)");
        					input = scan.nextLine();
		        			String pid4 = "";
		        			
        					switch(temp){
		        			case 1:
		        				System.out.println("Enter product " + temp +" associated by account by following format: ");
	        					System.out.println("product ID, produced date(YYYY-MM-DD), producing duration time, producer name, tester name,size, sotfware name major used, cost, ");
	        					input = input + ", product1-account, " + scan.nextLine();
	        					arg = input.split(", ");
	        					pid4 = arg[3];
	        					query4(connection, arg, 1);
		        				break;
		        			case 2:
		        				System.out.println("Enter product " + temp +" associated by account by following format: ");
	        					System.out.println("product ID, produced date(YYYY-MM-DD), producing duration time, producer name, tester name,size, color, cost");
	        					input = input + ", product2-account, " + scan.nextLine();
	        					arg = input.split(", ");
	        					pid4 = arg[3];
	        					query4(connection, arg, 2);
		        				break;
		        			case 3:
		        				System.out.println("Enter product " + temp +" associated by account by following format: ");
	        					System.out.println("product ID, produced date(YYYY-MM-DD), producing duration time, producer name, tester name, size, weight, cost");
	        					input = input + ", product3-account, " + scan.nextLine();
	        					arg = input.split(", ");
	        					pid4 = arg[3];
	        					query4(connection, arg, 3);
		        				break;
		        			default:
		        				System.out.println("Fail to insert data! please try again!");
		        				break;		        				
		        			}
		        			if(pid4.length() > 0){		        				        			
		        				System.out.println("Is the product repaired? 1 for yes, 0 for no");	        			
		        				if(Integer.parseInt(scan.nextLine())== 1){			        			
		        					System.out.println("Enter your input in follwing format: fixer name, fixed date(YYYY-MM-DD), requested by (type: complaint/controller)");
		        					input = pid4 + ", " + scan.nextLine();			 		        			
		        					arg = input.split(", ");
		        					query2(connection, arg, 4);
		        				}		        				
		        			}      					
		        			break;
		        			
		        		case 5:
		        			System.out.println("Enter your input in follwing format: compaint ID, compaint date(YYYY-MM-DD), description, treatment type in (refund/exchange),/n custemer name, product ID");
        					input = scan.nextLine();
        					arg = input.split(", ");
        					query5(connection, arg);		      
		        			break;
		        			
		        		case 6:
		        			System.out.println("Enter your input in follwing format: accident number, accident date(YYYY-MM-DD), lost days, product ID, Employee name");
        					input = scan.nextLine();
        					arg = input.split(", ");
        		    		System.out.println("Enter 1 for fix accident, 2 for producing accident.");       		
        		    		query6(connection, arg, Integer.parseInt(scan.nextLine()));
        					break;
        					
		        		case 7:
		        			System.out.println("Enter the product ID: ");
        					input = scan.nextLine();
        					query7(connection, input);
        					break;
        					
		        		case 8:
		        			System.out.println("Enter the worker's name: ");
        					input = scan.nextLine();
        					query8(connection, input);
		        			break;
		        			
		        		case 9:
		        			System.out.println("Enter the Quality Controller's name: ");
        					input = scan.nextLine();
        					query9(connection, input);
		        			break;
		        		case 10:
		        			System.out.println("Enter the Quality Controller's name: ");
        					input = scan.nextLine();
        					query10(connection, input);
		        			break;
		        		case 11:
		        			System.out.println("Enter color: ");
        					query11(connection,scan.nextLine());
		        			break;
		        		case 12:
		        			query12(connection);
		        			break;
		        		case 13:
		        			query13(connection);
		        			break;
		        		case 14:
		        			query14(connection);		        
		        			break;
		        		case 15:
		        			System.out.println("Enter the year(YYYY): ");
        					input = scan.nextLine();
		        			query15(connection, input);
		        			break;
		        		case 16:
		        			System.out.println("Enter the: technical name, Quality Controller name: ");
        					input = scan.nextLine();
        					arg = input.split(", ");
		        			query16(connection, arg);
		        			break;
		        		case 17:
		        			System.out.println("Enter the: start date, end date(YYYY-MM-DD): ");
        					input = scan.nextLine();
        					arg = input.split(", ");
		        			query17(connection, arg);
		        			break;
		        		case 18:
		        			System.out.println("Enter the input file: ");
        					input = scan.nextLine();
		        			query18(connection, input);
		        			break;
		        		case 19:
		        			System.out.println("Enter the output file: ");
        					input = scan.nextLine();
		        			query19(connection, input);
		        			break;	
		        		case 20:
		        			System.out.println("Thank you for using Future Inc database, Bye!");
		        			scan.close();
		    		        connection.close();
		        			System.exit(0);
		        			break;
		        		}
		        }        
	}
    
    /**THIS IS QUERY 1*/
    public static void query1(Connection connection, String[] arg, int i){
    	try{
    		CallableStatement stmt;
    		switch(i){
    		case 1:
    			stmt = connection.prepareCall("{call QUERY1_1(?,?,?,?)}");
        		stmt.setString(1, arg[0]);
        		stmt.setString(2, arg[1]);
        		stmt.setString(3, arg[2]);
        		stmt.setString(4, arg[3]);
        		stmt.execute();
    			break;
    		case 2:
    			stmt = connection.prepareCall("{call QUERY1_2(?,?,?)}");        		
    			stmt.setString(1, arg[0]);
        		stmt.setString(2, arg[1]);
        		stmt.setString(3, arg[2]);
        		stmt.execute();
    			break;
    		case 3:
    			stmt = connection.prepareCall("{call QUERY1_3(?,?,?)}");        		
    			stmt.setString(1, arg[0]);
        		stmt.setString(2, arg[1]);
        		stmt.setString(3, arg[2]);
        		stmt.execute();
    			break;
    		default:
				System.out.println("Fail to insert data! please try again!");
				break;    		
    		}

    		
    		System.out.println("Successful Excution.");
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
    }
    
    public static void query2(Connection connection, String[] arg, int i){
    	try{
    		CallableStatement stmt;
    		switch(i){
    		case 1:
    			stmt = connection.prepareCall("{call QUERY2_1(?,?,?,?,?,?,?,?,?)}");
    			stmt.setString(1, arg[0]);
    			stmt.setString(2, arg[1]);
    			stmt.setString(3, arg[2]);
    			stmt.setString(4, arg[3]);
    			stmt.setString(5, arg[4]);
    			stmt.setString(6, arg[5]);
    			stmt.setString(7, arg[6]);
    			stmt.setString(8, arg[7]);
    			stmt.setString(9, arg[8]);
        		stmt.execute();
    			break;
    		case 2:
    			stmt = connection.prepareCall("{call QUERY2_2(?,?,?,?,?,?,?,?,?)}");
    			stmt.setString(1, arg[0]);
    			stmt.setString(2, arg[1]);
    			stmt.setString(3, arg[2]);
    			stmt.setString(4, arg[3]);
    			stmt.setString(5, arg[4]);
    			stmt.setString(6, arg[5]);
    			stmt.setString(7, arg[6]);
    			stmt.setString(8, arg[7]);
    			stmt.setString(9, arg[8]);
        		stmt.execute();
    			break;
    		case 3:
    			stmt = connection.prepareCall("{call QUERY2_3(?,?,?,?,?,?,?,?,?)}");
    			stmt.setString(1, arg[0]);
    			stmt.setString(2, arg[1]);
    			stmt.setString(3, arg[2]);
    			stmt.setString(4, arg[3]);
    			stmt.setString(5, arg[4]);
    			stmt.setString(6, arg[5]);
    			stmt.setString(7, arg[6]);
    			stmt.setString(8, arg[7]);
    			stmt.setString(9, arg[8]);
        		stmt.execute();
    			break;
    		case 4:
    			stmt = connection.prepareCall("{call QUERY2_4(?,?,?,?)}");
    			stmt.setString(1, arg[0]);
    			stmt.setString(2, arg[1]);
    			stmt.setString(3, arg[2]);
    			stmt.setString(4, arg[3]);
        		stmt.execute();
    			break;
    		default:
				System.out.println("Fail to insert data! please try again!");
				break; 
    		}
    		System.out.println("Successful Excution.");
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
    }
    
    public static void query3(Connection connection, String[] arg){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY3(?,?,?)}");
			stmt.setString(1, arg[0]);
			stmt.setString(2, arg[1]);
			stmt.setString(3, arg[2]);
    		stmt.execute();
    		System.out.println("Successful Excution.");
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
    }
    
    public static void query4(Connection connection, String[] arg, int i){
    	try{
    		CallableStatement stmt;
    	
			switch(i){
			case 1:
				stmt = connection.prepareCall("{call QUERY4_1(?,?,?,?,?,?,?,?,?,?,?)}");
	    		stmt.setString(1, arg[0]);
				stmt.setString(2, arg[1]);
				stmt.setString(3, arg[2]);
				stmt.setString(4, arg[3]);
				stmt.setString(5, arg[4]);
				stmt.setString(6, arg[5]);
				stmt.setString(7, arg[6]);
				stmt.setString(8, arg[7]);
				stmt.setString(9, arg[8]);
				stmt.setString(10, arg[9]);
				stmt.setString(11, arg[10]);
				stmt.execute();
				break;
			case 2:
				stmt = connection.prepareCall("{call QUERY4_2(?,?,?,?,?,?,?,?,?,?,?)}");
	    		stmt.setString(1, arg[0]);
				stmt.setString(2, arg[1]);
				stmt.setString(3, arg[2]);
				stmt.setString(4, arg[3]);
				stmt.setString(5, arg[4]);
				stmt.setString(6, arg[5]);
				stmt.setString(7, arg[6]);
				stmt.setString(8, arg[7]);
				stmt.setString(9, arg[8]);
				stmt.setString(10, arg[9]);
				stmt.setString(11, arg[10]);
				stmt.execute();
				break;
			case 3:
				stmt = connection.prepareCall("{call QUERY4_3(?,?,?,?,?,?,?,?,?,?,?)}");
	    		stmt.setString(1, arg[0]);
				stmt.setString(2, arg[1]);
				stmt.setString(3, arg[2]);
				stmt.setString(4, arg[3]);
				stmt.setString(5, arg[4]);
				stmt.setString(6, arg[5]);
				stmt.setString(7, arg[6]);
				stmt.setString(8, arg[7]);
				stmt.setString(9, arg[8]);
				stmt.setString(10, arg[9]);
				stmt.setString(11, arg[10]);
				stmt.execute();
				break;
			}	
			
    		System.out.println("Successful Excution.");
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
    }
    
    public static void query5(Connection connection, String[] arg){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY5(?,?,?,?,?,?)}");
    		stmt.setString(1, arg[0]);
			stmt.setString(2, arg[1]);
			stmt.setString(3, arg[2]);
			stmt.setString(4, arg[3]);
			stmt.setString(5, arg[4]);
			stmt.setString(6, arg[5]);
			stmt.execute();
    		System.out.println("Successful Excution.");
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
    }
    
    public static void query6(Connection connection, String[] arg, int i){
    	try{
    		CallableStatement stmt;
			
			switch(i){
			case 1:
				stmt = connection.prepareCall("{call QUERY6_1(?,?,?,?,?)}");
	    		stmt.setString(1, arg[0]);
				stmt.setString(2, arg[1]);
				stmt.setString(3, arg[2]);
				stmt.setString(4, arg[3]);
				stmt.setString(5, arg[4]);
				stmt.execute();
				break;
			case 2:
				stmt = connection.prepareCall("{call QUERY6_2(?,?,?,?,?)}");
	    		stmt.setString(1, arg[0]);
				stmt.setString(2, arg[1]);
				stmt.setString(3, arg[2]);
				stmt.setString(4, arg[3]);
				stmt.setString(5, arg[4]);
				stmt.execute();
				break;
    		default:
				System.out.println("Fail to insert data! please try again!");
				break; 
			}
    		
			
			System.out.println("Successful Excution.");
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
    }
    public static void query7(Connection connection, String in){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY7(?,?,?)}");
    		stmt.setString(1, in);
            stmt.registerOutParameter("pdate", java.sql.Types.DATE);
            stmt.registerOutParameter("duration", java.sql.Types.INTEGER);
			stmt.execute();
			System.out.println("DATE			Duration Time");
			System.out.println(stmt.getDate("pdate") + "		" + stmt.getInt("duration"));
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    	
    public static void query8(Connection connection, String arg){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY8(?)}");
    		stmt.setString(1, arg);		
			ResultSet rs = stmt.executeQuery();
	        System.out.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", 
	        		"Product ID", "Produced Date", "Time Duration", "Producer", "Quality Controller", "Size", "Account Number", "Cost");
			 while (rs.next())
		      {
		        int pid = rs.getInt("pid");
		        Date pdate = rs.getDate("pdate");
		        int duration = rs.getInt("duration");
		        String producer = rs.getString("producer");
		        String tester = rs.getString("tester");
		        String size = rs.getString("size");
		        int accnum = rs.getInt("accnum");
		        int cost = rs.getInt("cost");

		        // print the results
		        System.out.format("%s\t\t%s\t%s\t\t%s\t%s\t\t%s\t%s\t\t%s\n", 
		        		pid, pdate, duration, producer, tester, size, accnum, cost);
		      }
    		System.out.println("Successful Excution.");
		    stmt.close();

    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
    }
    
    public static void query9(Connection connection, String in){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY9(?,?)}");
    		stmt.setString(1, "worker 1");
            stmt.registerOutParameter("count", java.sql.Types.INTEGER);
			stmt.execute();
			System.out.println("The number of error made by " + in + ":  " + stmt.getInt("count"));
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
    public static void query10(Connection connection, String in){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY10(?,?)}");
    		stmt.setString(1, in);
            stmt.registerOutParameter("total_cost", java.sql.Types.INTEGER);
			stmt.execute();
			System.out.println("Total Cost of product 3 which repiration requested by "+ in + ":\t" +stmt.getInt("total_cost"));
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
    public static void query11(Connection connection, String in){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY11(?)}");
    		stmt.setString(1, in);
    		ResultSet rs = stmt.executeQuery();
	        System.out.format("%s\t\t\t%s\n", "Customer Name", "Adress");

			while (rs.next())
		      {
		        String cname = rs.getString("cname");
		        String address = rs.getString("address");
		        System.out.format("%s\t\t\t%s\n", cname, address);

		      }
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
    public static void query12(Connection connection){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY12(?)}");
            stmt.registerOutParameter("totaldays", java.sql.Types.INTEGER);
			stmt.execute();
			System.out.println("Total Lost day: " + "		" + stmt.getInt("totaldays"));
    	}catch(Exception e){
    		System.out.println("Fail execution!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
    public static void query13(Connection connection){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY13()}");
			ResultSet rs = stmt.executeQuery();
	        System.out.format("%s\t\t\t%s\n", "Customer Name", "Adress");

			while (rs.next())
		      {
		        String cname = rs.getString("cname");
		        String address = rs.getString("address");
		   // print the results
		        System.out.format("%s\t\t\t%s\n", cname, address);
		      }
		    stmt.close();
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
    public static void query14(Connection connection){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY14()}");
			ResultSet rs = stmt.executeQuery();
	        System.out.format("%s\n", "Customer Name");			
			while (rs.next())
		      {
		        String cname = rs.getString("cname");
		   // print the results
		        System.out.format("%s\n", cname);
		      }
			
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
    public static void query15(Connection connection, String in){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY15(?,?)}");
    		stmt.setString(1, in);
            stmt.registerOutParameter("ave_cost", java.sql.Types.INTEGER);
			stmt.execute();
			System.out.println("AVERAGE COST IN " + in + " is: " + stmt.getInt("ave_cost"));
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
    public static void query16(Connection connection, String[] arg){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY7(?,?)}");
    		stmt.setString(1, arg[0]);
    		stmt.setString(2, arg[1]);
			stmt.execute();
			System.out.println("Update Sucessfully!");
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
    public static void query17(Connection connection, String[] arg){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY17(?,?)}");
    		stmt.setString(1, arg[0]);
    		stmt.setString(2, arg[1]);
    		stmt.execute();
    		
			System.out.println("Executed Sucessfully!");
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
    public static void query18(Connection connection, String in){
    	try{
    		CallableStatement stmt = connection.prepareCall("{call QUERY3(?,?,?)}");
    		CSVReader reader = null;
    		reader = new CSVReader(new FileReader(in));
    		String[] agrs;
    		int lineNum = 0;
            try {
				while ((agrs = reader.readNext()) != null) {
					lineNum++;
					stmt.setString(1, agrs[0]);
					stmt.setString(2, agrs[1]);
					stmt.setString(3, agrs[2]);
					stmt.addBatch();
				}
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}    

            int[] numRecords = new int[lineNum];
            try {
            	numRecords = stmt.executeBatch();
            } catch(BatchUpdateException e) {
            	numRecords = e.getUpdateCounts();
            	System.out.println("(ERROR) INSERTION exception: " + e.getMessage());
            }
			reader.close();

    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
    	System.out.println("\n\n\n");
    }

    
    public static void query19(Connection connection, String out){
    	try{
            CallableStatement stmt = connection.prepareCall("{call QUERY19()}");
            CSVWriter writer = new CSVWriter(new FileWriter(out));
			ResultSet rs = stmt.executeQuery();
			String in[] = new String[2];
			while (rs.next()){
		        in[0] = rs.getString("cname");
		        in[1] = rs.getString("address");
		        writer.writeNext(in);		      
		      }
			writer.close();
    	}catch(Exception e){
    		System.out.println("Fail to insert data!");
    		e.printStackTrace();
    	}
		System.out.println("\n\n\n");
    }
    
}