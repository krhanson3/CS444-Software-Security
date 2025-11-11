//Prewritten classes we need:
import java.util.*; 
import java.io.*;

//classes we create 
import javax.imageio.stream.ImageInputStream;
import ReportGenerator.*;
import SoftwareOperator.*; 
import Billing.*; 

public class Main{
	public static void main(String[] args)   
    {	System.out.println("Input User Type (Provider, Manager, Operator):");
        Scanner inputFromUser = new Scanner(System.in);
        String userType = inputFromUser.nextLine();

        // loops until correct input is taken in
        Set<String> validUserTypes = Set.of("provider", "manager", "operator");
        while (!validUserTypes.contains(userType.toLowerCase())) 
        {	System.out.println("User Type Not Found");
        	userType = inputFromUser.nextLine();
        }
	  	
        System.out.println("Enter User Name");
        String userName = inputFromUser.nextLine();

        // Attempt to read user ID as an integer
        int userID = -1; 
        while( userID == -1 ) 
        {	System.out.println("Input User ID"); 
			try	{ userID = inputFromUser.nextInt();	} 
        	catch (Exception e) 
        	{ System.out.println("Invalid input. Please enter a numeric value.");	} 
        }   
        
        //general verify identity function for all user types and members
		boolean Verified = verifyIdentity(userName, userID, userType);

		//leads to provider menu screen
        if(Verified && userType.equalsIgnoreCase("Provider"))
		{	
			System.out.println("What would you like to do? (input # relative to action)");
			System.out.println("\t 0. Exit");
			System.out.println("\t 1. Request Provider Service Report");
			System.out.println("\t 2. Request Member Service Report");
			System.out.println("\t 3. Verify Billing Information for the week");
			System.out.println("\t 4. Bill ChocAn for a Member's Service");
			System.out.println("\t 5. Request Provider Directory");
            
            int actionToPerform = -1;
			while(actionToPerform != 0) 
			{	System.out.println("Choose Action to Perform");
				actionToPerform = inputFromUser.nextInt();
            	//case statements that match printed options 
            	switch(actionToPerform) 
            	{	case 0:
            			System.out.println("Exiting Program");
            			break;
					case 1: //Request Provider Service Report WORKS
						System.out.println("Requesting Provider Service Report");
						System.out.println("Which week's report would you like?");
						int weeekNumber = inputFromUser.nextInt();
						inputFromUser.nextLine();
						String emaill = findProviderEmail(userName); 
						ProviderServiceReport psr = new ProviderServiceReport(userName, userID, weeekNumber, emaill);
						psr.read(); 
						break;
					case 2: //Request Member Service Report WORKS
						System.out.println("Requesting Member Service Report");
						System.out.println("Enter the Member's Number");
						int mmmemberNumber = inputFromUser.nextInt();
						inputFromUser.nextLine(); 
						System.out.println("Enter the Member's Name");
						String memmName = inputFromUser.nextLine(); 
						System.out.println("Which week's report would you like?");
						int weeeekNumber = inputFromUser.nextInt();
						inputFromUser.nextLine();
						String emailll = findProviderEmail(userName); 
						MemberServiceReport msr = new MemberServiceReport(memmName, mmmemberNumber, weeeekNumber, emailll);
						msr.read(); 
						break;
					case 3:	//works 
						System.out.println("Verifying Billing Information");
						System.out.println("\t 1:input service information");
						System.out.println("\t 2:view records for the current week");	
						int actionToPerformm = inputFromUser.nextInt();
						inputFromUser.nextLine();
						switch(actionToPerformm)
						{	case 1: //Input a Service
							    try (BufferedWriter writer = new BufferedWriter(new FileWriter(userID + ".txt", true)))
						        {	String input; //Writing in all info to file in database
						            System.out.println("Enter today's date: ");
						            input = inputFromUser.nextLine();
						            writer.write(input + ",");
						            System.out.println("Enter the current time: ");
						            input = inputFromUser.nextLine();
						            writer.write(input + ",");
						            System.out.println("Enter the member's name: ");
						            input = inputFromUser.nextLine();
						            writer.write(input + ",");
						            System.out.println("Enter the member's ID number: ");
						            input = inputFromUser.nextLine();
						            writer.write(input + ",");
						            System.out.println("Enter the code for the service provided: ");
						            input = inputFromUser.nextLine();
						            writer.write(input + ",");
						            System.out.println("Enter the fee for the service provided: ");
						            input = inputFromUser.nextLine();
						            writer.write(input);
						            writer.newLine();

						            System.out.println("Service information has been recorded.");
						        }
						        catch(IOException e)	{	System.out.println("An error has occurred.");	e.printStackTrace();	}

						    case 2:
						    	String fileName = userID + ".txt";
						    	BufferedReader reader = null;
						    	try
						    	{	reader = new BufferedReader(new FileReader(fileName));
						    		String line;
						    		while((line = reader.readLine()) != null)
						    		{	System.out.println(line);	}
						    	}
						    	catch(IOException e)	{	System.out.println("An error has occurred.");	e.printStackTrace();	}

						}
						break; 
						//Bill ChocAn for a Member's Service
					case 4:	//THIS WORKS 
						System.out.println("Billing ChocAn for a Member's Service");
						System.out.println("Enter the Member's Number");
						int mmemberNumber = inputFromUser.nextInt();
						inputFromUser.nextLine(); 
						System.out.println("Enter the Member's Name");
						String memName = inputFromUser.nextLine(); 
						String status = findMemberStatus(mmemberNumber);
						if(status.equalsIgnoreCase("S")) 
						{	System.out.println("Member Suspended");	}
						else if(status.equalsIgnoreCase("A"))
						{	String memberType = "Member";
							boolean memberVerified = verifyIdentity(memName, mmemberNumber, memberType);
							if(memberVerified) {	System.out.println("Member Verified"); }
						}
						System.out.println("Input Date of Service (MM-DD-YYYY)");
						String dateofService = inputFromUser.nextLine();
						System.out.println("Input Current Date (MM-DD-YYYY)");
						String currentDate = inputFromUser.nextLine();
						System.out.println("Input Current Time (HH:MM:SS)");
						String currentTime = inputFromUser.nextLine();
						String providerDirectory = "DataCenter\\ProviderDirectory.txt";
						readAndPrintFile(providerDirectory);
						System.out.println("Input Service Code");
						int serviceCode = inputFromUser.nextInt();
						inputFromUser.nextLine(); 
						String serviceName = findServiceName(serviceCode); 
						double fee = findFee(serviceCode); 
						System.out.println("Enter Comments or N/A");
						String comments = inputFromUser.nextLine(); 
						Services service = new Services(userName, userID, currentDate, currentTime, dateofService, 
								memName, mmemberNumber, serviceCode, serviceName, fee, comments);
						service.writeToData(); 
						break;
					case 5: //works 
						System.out.println("Requesting Provider Directory");
						String provideDirectory = "DataCenter\\ProviderDirectory.txt";
						readAndPrintFile(provideDirectory);
						break; 
            	}
			}
		}
		//leads to manager menu screen
		else if(Verified && userType.equalsIgnoreCase("Manager"))
		{	System.out.println("What would you like to do?");
			System.out.println("\t 0. Exit"); 
            System.out.println("\t 1. Request a Provider's Service Report");	
			System.out.println("\t 2. Request a Member's Service Report");
			System.out.println("\t 3. Request a Weekly Service Report");
			System.out.println("\t 4. Create a Weekly Service Report");
			System.out.println("\t 5. Create a Member's Service Report");
			System.out.println("\t 6. Create a Provider's Service Report");
            int actionToPerform = -1; 
            while(actionToPerform!=0)
            {	System.out.println("Choose Action to Perform");
            	actionToPerform = inputFromUser.nextInt();
            	switch(actionToPerform) 
            	{	case 0: System.out.println("Exiting Program"); break;
	            	case 1:
	            		System.out.println("Requesting Provider Service Report");
	            		System.out.println("Enter the Provider's Number");
						int proviNumber = inputFromUser.nextInt();
						inputFromUser.nextLine(); 
						System.out.println("Enter the Provider's Name");
						String proviName = inputFromUser.nextLine(); 
						System.out.println("Which week's report would you like?");
						int wkNumber = inputFromUser.nextInt();
						inputFromUser.nextLine();
						String eeemaill = findManagerEmail(userName); 
						ProviderServiceReport psr = new ProviderServiceReport(proviName, proviNumber, wkNumber, eeemaill);
						psr.read(); 
						break;
	                case 2:
	                	System.out.println("Requesting Member Service Report");
						System.out.println("Enter the Member's Number");
						int mmmmemberNumber = inputFromUser.nextInt();
						inputFromUser.nextLine(); 
						System.out.println("Enter the Member's Name");
						String mmemmName = inputFromUser.nextLine(); 
						System.out.println("Which week's report would you like?");
						int weeeeekNumber = inputFromUser.nextInt();
						inputFromUser.nextLine();
						String eeeemailll = findManagerEmail(userName); 
						MemberServiceReport msr = new MemberServiceReport(mmemmName, mmmmemberNumber, weeeeekNumber, eeeemailll);
						msr.read(); 
						break;
	                case 3:
	                    System.out.println("Requesting a Weekly Service Report");
	                    System.out.println("Which week's report would you like?");
						int wkkNumber = inputFromUser.nextInt();
						inputFromUser.nextLine();
						String emmaill = findManagerEmail(userName); 
						WeeklyServiceReport wsr = new WeeklyServiceReport(wkkNumber, emmaill);
						wsr.read(); 
	                    break;
	                case 4:
	                	System.out.println("\t Creating a Weekly Service Report");
	                	System.out.println("Enter Current Week Number");
						int waalkNumber = inputFromUser.nextInt();
						inputFromUser.nextLine(); 
						WeeklyServiceReport wwsr = new WeeklyServiceReport(waalkNumber, findManagerEmail(userName));
						wwsr.create();
						wwsr.read();
	                	break;
	                case 5:
	                	System.out.println("Creating a Member's Service Report");
	        			System.out.println("Enter Current Week Number");
						int weeeekNumber = inputFromUser.nextInt();
						inputFromUser.nextLine(); 
						System.out.println("Input the Member's Name");
						String memmemberName = inputFromUser.nextLine(); 
						int mmmemberNumber = findmembernumber(memmemberName);
						MemberServiceReport mmsr = new MemberServiceReport(memmemberName, mmmemberNumber, weeeekNumber, findManagerEmail(userName));
	                	mmsr.create(); 
						mmsr.read(); 
						break;
	                case 6:
	                	System.out.println("Creating a Provider's Service Report");
	        			System.out.println("Enter Current Week Number");
						int wNumber = inputFromUser.nextInt();
						inputFromUser.nextLine(); 
						System.out.println("Input the Provider's Name");
						String pName = inputFromUser.nextLine(); 
						int pNumber = findprovidernumber(pName);
						ProviderServiceReport ppsr = new ProviderServiceReport(pName, pNumber, wNumber, findManagerEmail(userName));
	                	ppsr.create(); 
						ppsr.read();
	                	break;
            	}
            }
		}
		//leads to operator meny screen
		else if(Verified && userType.equalsIgnoreCase("Operator"))
		{	System.out.println("What would you like to do?");
			System.out.println("\t 0. Exit");
			System.out.println("\t 1. Add New Member");
			System.out.println("\t 2. Delete Existing Member");
			System.out.println("\t 3. Update Member Records");
			System.out.println("\t 4. Add New Provider");
			System.out.println("\t 5. Delete Existing Provider");
			System.out.println("\t 6. Update Provider Records");
			System.out.println("\t 7. Run Main Accounting Procedure");
            
			int actionToPerform = -1; 
            while(actionToPerform!=0)
            {	System.out.println("Choose Action to Perform");
				actionToPerform = inputFromUser.nextInt();
				inputFromUser.nextLine(); 
				switch(actionToPerform) 
				{	case 0: System.out.println("Exiting Program");break;
				case 1:	//by ainsley WORKS
					System.out.println("Enter Member Name:");
					String name = inputFromUser.next();
					System.out.println("Enter Member Number (format: 1234567890)");
					int number = inputFromUser.nextInt();
					inputFromUser.nextLine();
					System.out.println("Enter Member Address:");
					String address = inputFromUser.nextLine();
					System.out.println("Enter Member City:");
					String city = inputFromUser.next();
					System.out.println("Enter Member State:");
					String state = inputFromUser.next();
					System.out.println("Enter Member ZIP:");
					String zip = inputFromUser.next();
					inputFromUser.nextLine();
					String status = "A";
					Member member = new Member(name,number,address,city,state,zip,status);
					member.writeToData();
					break;
				case 2:	//delete member WORKS
					System.out.println("Enter Member Name:");
					String mname = inputFromUser.next();
					System.out.println("Enter Member ID (format: 1234567890)");
					int id = inputFromUser.nextInt();
					inputFromUser.nextLine();
					Member temp = new Member(mname, id, "", "","","","");
					temp.deleteMember(mname, id);
					break;
				case 3:	//update member records - by ainsley SHOULD WORK
					System.out.println("Enter Member Name:");
					String MnameUpdate = inputFromUser.next();
					System.out.println("Enter member ID (format: 1234567890)");
					int MidUpdate = inputFromUser.nextInt();
					System.out.println("What would you like to update?");
					System.out.println("\t 1. Name");
					System.out.println("\t 2. ID");
					System.out.println("\t 3. Address");
					System.out.println("\t 4. City");
					System.out.println("\t 5. State");
					System.out.println("\t 6. ZIP");
					System.out.println("\t 7. Suspension Status");
					int choice = inputFromUser.nextInt();
					System.out.println("What would you like this to be changed to?");
					String newVal = inputFromUser.next();
					Member.editMember(MnameUpdate, MidUpdate, choice, newVal);
					break;
				case 4:	// add provider WORKS
					System.out.println("Enter Provider Name:");
					String pname = inputFromUser.next();
					System.out.println("Enter Provider Number (format: 1234567890)");
					int pnumber = inputFromUser.nextInt();
					inputFromUser.nextLine();
					System.out.println("Enter Provider Address:");
					String paddress = inputFromUser.nextLine();
					System.out.println("Enter Provider City:");
					String pcity = inputFromUser.nextLine();
					System.out.println("Enter Provider State:");
					String pstate = inputFromUser.nextLine();
					System.out.println("Enter Provider ZIP:");
					int pzip = inputFromUser.nextInt();
					inputFromUser.nextLine();
					System.out.println("Enter Provider Email:");
					String email = inputFromUser.next();
					Provider provider = new Provider(pname,pnumber,paddress,pcity,pstate,pzip,email);
					provider.writeToData();
					break; 
				case 5:	//delete provider WORKS
					System.out.println("Enter Provider Name:");
					String mmname = inputFromUser.next();
					System.out.println("Enter Provider ID (format: 1234567890)");
					int mid = inputFromUser.nextInt();
					inputFromUser.nextLine();
					Provider ptemp = new Provider(mmname,mid,"","","",-1,"");
					ptemp.deleteProvider(mmname, mid);
					break; 
				case 6:	//update provider details - by ainsley SHOULD WORK
					System.out.println("Enter Provider Name:");
					String PnameUpdate = inputFromUser.next();
					System.out.println("Enter provider ID (format: 1234567890)");
					int PidUpdate = inputFromUser.nextInt();
					System.out.println("What would you like to update?");
					System.out.println("\t 1. Name");
					System.out.println("\t 2. ID");
					System.out.println("\t 3. Address");
					System.out.println("\t 4. City");
					System.out.println("\t 5. State");
					System.out.println("\t 6. ZIP");
					System.out.println("\t 7. Email");
					int pchoice = inputFromUser.nextInt();
					System.out.println("What would you like this to be changed to?");
					String pnewVal = inputFromUser.next();
					Provider.editProvider(PnameUpdate, PidUpdate, pchoice, pnewVal);
					break; 
				case 7:	
					System.out.println("Running Main Accounting Procedure");
					System.out.println("Enter Current Week Number");
					int walkNumber = inputFromUser.nextInt();
					inputFromUser.nextLine(); 
					WeeklyServiceReport wsr = new WeeklyServiceReport(walkNumber, "");
					wsr.create();
					String servicesFile = "Billing\\AllServices.txt";
					HashSet<Integer> uniqueProviders = new HashSet<>();
					HashSet<Integer> uniqueMembers = new HashSet<>();
					try(BufferedReader br = new BufferedReader(new FileReader(servicesFile)))
					{	String line; 
						while ((line = br.readLine()) != null) 
						{	// Split the line by commas to get the columns
							String[] parts = line.split(",");
							if(parts.length >=11)
							{   if (line.startsWith("ProviderName") || line.trim().isEmpty()) {	continue;	}
								String providerName = parts[0].trim();
								int pNumber = Integer.parseInt(parts[1].trim());
								if(!uniqueProviders.contains(pNumber))
								{	uniqueProviders.add(pNumber);
									ProviderServiceReport psr = new ProviderServiceReport(providerName, pNumber, walkNumber, "");
									psr.create();
								}
								String memberName = parts[5].trim(); 
								int memberNumber = Integer.parseInt(parts[6].trim());
								if(!uniqueMembers.contains(memberNumber)) 
								{	uniqueMembers.add(memberNumber);
									MemberServiceReport msr = new MemberServiceReport(memberName, memberNumber, walkNumber, "");
									msr.create(); 
								}
							}
						}
					}
					catch (IOException e) {	System.err.println("Error reading the file: " + e.getMessage());	}
					break;
				}
        	}
		}
		//last option is user login is not verified
		else if(!Verified){	System.out.println("User Not Verified");}      
        inputFromUser.close();
        System.exit(0);
    }

	// Main Helper Functions
	// by Daniel Marx (this works!)
	public static boolean verifyIdentity(String name, int ID, String userType){

        boolean isVerified = false;
        String filepath = "";                    // initialize variable
        userType = userType.toLowerCase();       // set all lowercase for readability
        String id = String.valueOf(ID);          // convert to string to make comparison easier

        switch(userType){
            case "manager" -> filepath = "DataCenter\\ManagerData.txt";
            case "provider" -> filepath = "DataCenter\\ProviderData.txt";
            case "operator" -> filepath = "DataCenter\\OperatorData.txt";
			case "member" -> filepath = "DataCenter\\MemberData.txt";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            //We need to compare columns[0] and columns[1]
            while ((line = br.readLine()) != null) {
                // Split the line into columns
                String[] columns = line.split(",");

                if(columns[0].equalsIgnoreCase(name)){
                    if(columns[1].equals(id)){
                        isVerified = true;
						break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return isVerified;
    }
	
	public static String findManagerEmail(String userName)
	{	String filepath = "DataCenter\\ManagerData.txt";                    // initialize variable
    	
		String email = ""; 
    	
    	try (BufferedReader br = new BufferedReader(new FileReader(filepath))) 
        {	String line;
            
            while ((line = br.readLine()) != null) 
            {   // Split the line into columns
                String[] columns = line.split(",");

                if(columns[0].equalsIgnoreCase(userName))
                {   email =  columns[2];
                    }
            }
         }
         catch (IOException e) {	System.err.println("Error reading file: " + e.getMessage());	}
		return email; 
	}
	public static String findProviderEmail(String userName)
	{	String filepath = "DataCenter\\ProviderData.txt";                    
    	String email = ""; 
    	    	
    	try (BufferedReader br = new BufferedReader(new FileReader(filepath))) 
        {	String line;
            
            while ((line = br.readLine()) != null) 
            {   // Split the line into columns
                String[] columns = line.split(",");

                if(columns[0].equalsIgnoreCase(userName))
                {   email =  columns[6];
                    }
            }
         }
         catch (IOException e) {	System.err.println("Error reading file: " + e.getMessage());	}
		return email; 
	}
	public static String findMemberStatus(int memberNumber)
	{	String filepath = "DataCenter\\MemberData.txt";
        String status = "";
		String id = String.valueOf(memberNumber);

		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            //We need to compare columns[0] and columns[1]
            while ((line = br.readLine()) != null) {
                // Split the line into columns
                String[] columns = line.split(",");

                if(columns[1].equalsIgnoreCase(id))
                {    status = columns[6];	}
                
            }
        } 
		catch (IOException e) {	}

        return status;
	}
	public static void readAndPrintFile(String filePath)
	{	BufferedReader reader = null;

    try {
        // Initialize a BufferedReader to read the file
        reader = new BufferedReader(new FileReader(filePath));
        String line;

        // Read and print each line from the file
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    } catch (IOException e) {
        // Handle exceptions that occur during file I/O
        System.err.println("An error occurred while reading the file: " + e.getMessage());
    } finally {
        // Ensure the BufferedReader is closed to free resources
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("An error occurred while closing the file: " + e.getMessage());
            }
        }
    }
	}
	public static String findServiceName(int serviceNumber)
	{	String filepath = "DataCenter\\ProviderDirectory.txt";
		String status = "";
		String id = String.valueOf(serviceNumber);

		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
		String line;
		//We need to compare columns[0] and columns[1]
		while ((line = br.readLine()) != null) {
			// Split the line into columns
			String[] columns = line.split(",");

			if(columns[0].equalsIgnoreCase(id)){
				status = columns[1];
			}
		}
	} 
	catch (IOException e) {	}

	return status;
	}
	public static double findFee(int serviceNumber)
	{	String filepath = "DataCenter\\ProviderDirectory.txt";                    
    	double fee = -1; 
    	    	
    	try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Split the line into columns
                if (line.startsWith("ServiceCode") || line.trim().isEmpty()) {
					continue;
				}
				String[] columns = line.split(",");

                // Check if the service number matches
                if (Integer.parseInt(columns[0].trim()) == serviceNumber) {
                    fee = Double.parseDouble(columns[2].trim());
                    break; // Exit loop once the fee is found
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
        }

        return fee;
	}
	public static int findmembernumber(String memberName)
	{	String filepath = "DataCenter\\MemberData.txt";
		int memberNumberr = -1; 
		
		try (BufferedReader br = new BufferedReader(new FileReader(filepath)))
		{	String line;
			//We need to compare columns[0] and columns[1]
			while ((line = br.readLine()) != null) 
			{	// Split the line into columns
				String[] columns = line.split(",");

				if(columns[0].equalsIgnoreCase(memberName))
				{    memberNumberr = Integer.parseInt(columns[1]);	}
				}
		} 
	catch (IOException e) {	}

	return memberNumberr;
	}
	public static int findprovidernumber(String memberName)
	{	String filepath = "DataCenter\\ProviderData.txt";
		int memberNumberr = -1; 
		
		try (BufferedReader br = new BufferedReader(new FileReader(filepath)))
		{	String line;
			//We need to compare columns[0] and columns[1]
			while ((line = br.readLine()) != null) 
			{	// Split the line into columns
				String[] columns = line.split(",");

				if(columns[0].equalsIgnoreCase(memberName))
				{    memberNumberr = Integer.parseInt(columns[1]);	}
				}
		} 
	catch (IOException e) {	}

	return memberNumberr;
	}
}

