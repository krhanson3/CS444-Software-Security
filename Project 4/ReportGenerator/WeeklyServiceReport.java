/*
 * Author: Kaitlyn Hanson
 * 
 */

package ReportGenerator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
//anything related to wsr goes here
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class WeeklyServiceReport {
	String fileName; 
	int weekNumber; 
	String email; 
	
	public String getFileName() { return fileName; }
	public void setFileName(String fileName) { this.fileName = fileName; }

	// Getter and Setter for weekNumber
	public int getWeekNumber() { return weekNumber; }
	public void setWeekNumber(int weekNumber) { this.weekNumber = weekNumber; }

	// Getter and Setter for email
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	//Constructor
	public WeeklyServiceReport(int WeekNumber, String email)
	{	this.weekNumber = WeekNumber;
		this.fileName = "W." + WeekNumber + ".Report.txt";	
		this.email = email; 
	}
	
	public void read()
    {   String filepath = "ReportCenter\\"+ getFileName(); 
        File reportFile = new File(filepath); 

        if(!reportFile.exists()) { System.out.println("Error: The file does not exist.");	return; }

        try (Scanner readRequestFile = new Scanner(reportFile)) 
    	{	StringBuilder reportContent = new StringBuilder();
    		while (readRequestFile.hasNextLine()) 
    		{	reportContent.append(readRequestFile.nextLine()).append("\n");	}
    		
    		// Simulate sending the email
    		System.out.println("Simulating email send...");
    		System.out.println("To: " + getEmail());
    		System.out.println("Subject: Requested Report");
    		System.out.println("Body:\n" + reportContent + "\n");
    		System.out.println("The report data has been successfully sent to " + getEmail() + ".");
    	} 
    	catch (FileNotFoundException e) 
    	{	System.out.println("An error occurred while accessing the file.");
    		e.printStackTrace();
    	}
        
    }

	public String readGUI()
    {   String filepath = "ReportCenter\\"+ getFileName(); 
        File reportFile = new File(filepath); 

        if(!reportFile.exists()) { System.out.println("Error: The file does not exist.");	return ""; }

        try (Scanner readRequestFile = new Scanner(reportFile)) 
    	{	StringBuilder reportContent = new StringBuilder();

			reportContent.append("Simulating email send...\n");
    		reportContent.append("To: " + getEmail() + "\n");
    		reportContent.append("Subject: Requested Report\n");
    		reportContent.append("Body:\n");
    		
    		while (readRequestFile.hasNextLine()) 
    		{	reportContent.append(readRequestFile.nextLine()).append("\n");	}
    		
    		
    		reportContent.append("The report data has been successfully sent to " + getEmail() + ".");

			return reportContent.toString();
    	} 
    	catch (FileNotFoundException e) 
    	{	System.out.println("An error occurred while accessing the file.");
    		e.printStackTrace();
    	}

		return "";
        
    }

	public void create()
    {	/*	total number of consulations
    		combined fee to all providers
    	*/
    	String servicesFile = "Billing\\AllServices.txt";
    	String reportFile = "ReportCenter\\" + getFileName();
    	HashMap<String, double[]> uniqueProviders = new HashMap<>();

		int totalConsultations = 0;
		double combinedFee = 0.0;
    	
    	try(BufferedReader br = new BufferedReader(new FileReader(servicesFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(reportFile)))
    	{
    		String line; 
    		
    		while((line = br.readLine()) != null)
    		{	if (line.startsWith("ProviderName") || line.trim().isEmpty()) {	continue;	}
    			
    			String[] parts = line.split(",");
    			if(parts.length >= 11)
    			{	String providerName = parts[0].trim(); 
					double fee = Double.parseDouble(parts[9].trim());
    				
    				uniqueProviders.putIfAbsent(providerName, new double[]{0, 0.0});
                    uniqueProviders.get(providerName)[0] += 1; // Increment count
                    uniqueProviders.get(providerName)[1] += fee; // Add fee to the total
    			}
    			else {	System.err.println("Invalid line format: " + line);	}
			}
			bw.write("Provider, # of Consultations, Total Fee");
            bw.newLine();
            for (String providerName : uniqueProviders.keySet()) 
            {    double[] data = uniqueProviders.get(providerName);
                bw.write(providerName + ", " + (int) data[0] + ", " + data[1]);
                bw.newLine();

				combinedFee += data[1];
				totalConsultations += data[0];
			}
			
			bw.newLine();
            bw.write("Total Consultations: " + totalConsultations);
            bw.newLine();
            bw.write("Total Combined Fee: " + combinedFee);
            bw.newLine();
		}
    	catch (IOException e) {
            System.err.println("Error processing the files: " + e.getMessage());
        }
    	
    	
    }
}