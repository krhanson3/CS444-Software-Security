/*
 * Author: Kaitlyn Hanson
 * 
 */

package ReportGenerator;
//anything related to psr goes here
import java.util.*;
 

import java.io.*;  // Import the File class


public class ProviderServiceReport
{	String ProviderName; 
    int ProviderNumber; 
    int WeekNumber; 
    String FileName; 
    String providerEmail; 
        
    public int getProviderNumber() { return ProviderNumber; }
    public void setProviderNumber(int ProviderNumber) { this.ProviderNumber = ProviderNumber; }

    public String getProviderName() { return ProviderName; }
    public void setProviderName(String ProviderName) { this.ProviderName = ProviderName; }
    
    public int getWeekNumber() { return WeekNumber; }
    public void setWeekNumber(int WeekNumber) { this.WeekNumber = WeekNumber; }
    
    public String getFileName() { return FileName; }
    public void setFileName(String ProviderName, int userID, int WeekNumber) { this.FileName = "P." + WeekNumber + "." + ProviderName + "." + ProviderNumber + ".txt"; }
    
    public String getproviderEmail() {return providerEmail; }
    public void setproviderEmail(String provideremail) {providerEmail = provideremail;}

    //Constructor 
    public ProviderServiceReport(String ProviderName, int ProviderNumber, int WeekNumber, String providerEmail) 
    {   this.ProviderName = ProviderName;
        this.ProviderNumber = ProviderNumber;
        this.WeekNumber = WeekNumber;
        this.FileName = "P." + WeekNumber + "." + ProviderName + "." + ProviderNumber + ".txt";
        this.providerEmail = providerEmail; 
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
    		System.out.println("To: " + getproviderEmail());
    		System.out.println("Subject: Requested Report");
    		System.out.println("Body:\n" + reportContent + "\n");
    		System.out.println("The report data has been successfully sent to " + getproviderEmail() + ".");


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
            reportContent.append("To: " + getproviderEmail() + "\n");
    		reportContent.append("Subject: Requested Report\n");
    		reportContent.append("Body:\n");
    		
    		while (readRequestFile.hasNextLine()) 
    		{	reportContent.append(readRequestFile.nextLine()).append("\n");	}
    		
    		// Simulate sending the email
    		
    		reportContent.append("The report data has been successfully sent to " + getproviderEmail() + ".");

            return reportContent.toString();
    	} 
    	catch (FileNotFoundException e) 
    	{	System.out.println("An error occurred while accessing the file.");
    		e.printStackTrace();
    	}
        return "";
        
    }

    public void create() {
        String servicesFile = "Billing\\AllServices.txt";
        String reportFile = "ReportCenter\\" + getFileName();
        HashMap<String, ServiceData> serviceDataMap = new HashMap<>();
        HashSet<String> writtenProviders = new HashSet<>(); // Track written providers
        double totalFee = 0; // Track total fee
        int totalConsultations = 0; // Track total number of consultations
    
        try (BufferedReader br = new BufferedReader(new FileReader(servicesFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(reportFile))) {
    
            String line;
            boolean isFirstLine = true;
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { // Skip header
                    isFirstLine = false;
                    continue;
                }
    
                String[] parts = line.split(",");
                if (parts.length >= 11) 
                {   String providerName = parts[0].trim();
                    String providerNumber = parts[1].trim();
                    String dateService = parts[4].trim();
                    String dateReceived = parts[2].trim();
                    String timeReceived = parts[3].trim();
                    String memberName = parts[5].trim();
                    String memberNumber = parts[6].trim();
                    String serviceCode = parts[7].trim();
                    double fee;
    
                    try {
                        fee = Double.parseDouble(parts[9].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid fee value on line: " + line);
                        continue; // Skip invalid rows
                    }
    
                    // Write provider information only if not already written
                    if (!writtenProviders.contains(providerNumber)) 
                    {    String providerDataFile = "DataCenter\\ProviderData.txt";
                        try (BufferedReader br2 = new BufferedReader(new FileReader(providerDataFile))) 
                        {    String line1;
                            while ((line1 = br2.readLine()) != null) 
                            {    String[] parts1 = line1.split(",");
                                if (parts1.length >= 7 && parts1[0].equalsIgnoreCase(providerName)) 
                                {    StringBuilder providerInfo = new StringBuilder();
                                    providerInfo.append(providerName).append(",").append(providerNumber).append(",")
                                                .append(parts1[2].trim()).append(",")
                                                .append(parts1[3].trim()).append(",")
                                                .append(parts1[4].trim()).append(",")
                                                .append(parts1[5].trim());
                                    bw.write(providerInfo.toString());
                                    bw.newLine();
                                    writtenProviders.add(providerNumber); // Mark provider as written
                                    break;
                                }
                            }
                        }
                    }
    
                    String key = providerNumber + "-" + serviceCode; // Unique key per provider and service
                    serviceDataMap.putIfAbsent(key, new ServiceData(dateService, dateReceived, timeReceived,
                                                                    memberName, memberNumber, serviceCode, 0));
                    ServiceData data = serviceDataMap.get(key);
                    data.addConsultation();
                    data.addFee(fee);
    
                    // Update totals
                    totalConsultations++;
                    totalFee += fee;
                }
            }
    
            // Write aggregated service data
            for (ServiceData data : serviceDataMap.values()) {
                bw.write(data.toString());
                bw.newLine();
            }
    
            // Write totals at the end of the report
            bw.newLine();
            bw.write("Total Consultations: " + totalConsultations);
            bw.newLine();
            bw.write("Total Fee: " + totalFee);
            bw.newLine();
    
        } catch (IOException e) {
            System.err.println("Error processing the files: " + e.getMessage());
        }
    }
    
    

    public class ServiceData {
        private String dateService;
        private String dateReceived;
        private String timeReceived;
        private String memberName;
        private String memberNumber;
        private String serviceCode;
        private double totalFee;
        private int consultationCount;
    
        // Constructor
        public ServiceData(String dateService, String dateReceived, String timeReceived, 
                           String memberName, String memberNumber, String serviceCode, double fee) {
            this.dateService = dateService;
            this.dateReceived = dateReceived;
            this.timeReceived = timeReceived;
            this.memberName = memberName;
            this.memberNumber = memberNumber;
            this.serviceCode = serviceCode;
            this.totalFee = fee;
            this.consultationCount = 1;
        }
    
        // Increment the number of consultations
        public void addConsultation() {
            consultationCount++;
        }
    
        // Add to the total fee
        public void addFee(double fee) {
            totalFee += fee;
        }
    
        // Properly format the data for writing to the file
        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%s,%s,%.2f,%d",
                    dateService, dateReceived, timeReceived, memberName, memberNumber, serviceCode, totalFee, consultationCount);
        }
    }
}