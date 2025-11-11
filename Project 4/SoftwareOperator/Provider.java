package SoftwareOperator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
 * Author: Ainsley Lewis, Eoin O'Hearn
 * keeps track of basic provider data  
 */

public class Provider {
    public String ProviderName;
    public int ProviderNumber;
    public String ProviderStreetAddress;
    public String ProviderCity;
    public String ProviderState;
    public int ProviderZip;
    public String email;
    public static String filepath = "DataCenter/ProviderData.txt";
	
		public String getProviderName() { return ProviderName; }
		public void setProviderName(String ProviderName) { this.ProviderName = ProviderName; }
	
		public int getProviderNumber() { return ProviderNumber; }
		public void setProviderNumber(int ProviderNumber) { this.ProviderNumber = ProviderNumber; }
	
		public String getProviderStreetAddress() { return ProviderStreetAddress; }
		public void setProviderStreetAddress(String ProviderStreetAddress) { this.ProviderStreetAddress = ProviderStreetAddress; }
	
		public String getProviderCity() { return ProviderCity; }
		public void setProviderCity(String ProviderCity) { this.ProviderCity = ProviderCity; }
	
		public String getProviderState() { return ProviderState; }
		public void setProviderState(String ProviderState) { this.ProviderState = ProviderState; }
	
		public int getProviderZip() { return ProviderZip; }
		public void setProviderZip(int ProviderZip) { this.ProviderZip = ProviderZip; }
	
		public String getEmail() { return email; }
		public void setEmail(String email) { this.email = email; }
	
		public Provider(String ProviderName, int ProviderNumber, String ProviderStreetAddress, String ProviderCity, String ProviderState, int ProviderZip, String email) {
			this.ProviderName = ProviderName;
			this.ProviderNumber = ProviderNumber;
			this.ProviderStreetAddress = ProviderStreetAddress;
			this.ProviderCity = ProviderCity;
			this.ProviderState = ProviderState;
			this.ProviderZip = ProviderZip;
			this.email = email;
		}
	
		public Provider(String ProviderName, int ProviderNumber) {
			this.ProviderName = ProviderName;
			this.ProviderNumber = ProviderNumber;
			this.ProviderStreetAddress = "Unknown";
			this.ProviderCity = "Unknown";
			this.ProviderState = "Unknown";
			this.ProviderZip = 0;
			this.email = "Unknown"; 
		}
	
		public void writeToData()
		{
			//,A\n sets the provider to active, and writes a new line to prevent errors reading/writing in the future
			String line =   ProviderName + "," +
							ProviderNumber + "," + 
							ProviderStreetAddress + "," +
							ProviderCity + "," +
							ProviderState + "," +
							ProviderZip + "," +
							email + ",\n";
			
			try(FileWriter writer = new FileWriter(filepath, true))
			{
				writer.write(line);
			} 
			catch(IOException e)
			{
				System.out.println("Sorry, an error occured\n");
			}
		}
	
		// deletes the provider
		public void deleteProvider(String name, int id){
	
			String idString = String.valueOf(id);	
			List<String> lines = new ArrayList<>();
	
			try
			{
				//collect all the current lines
				List<String> fullLines = Files.readAllLines(Paths.get(filepath));
				//finding the correct line:
				for(int i = 0; i < fullLines.size(); i++)
				{
					String line = fullLines.get(i);
					String[] items = line.split(",");
	
					if(items[0].equalsIgnoreCase(name) && items[1].equalsIgnoreCase(idString))
					{continue; 
					} else {
						lines.add(line);
					}
				}
	
				
				//Overwrite the current file with the new information 
				try (FileWriter writer = new FileWriter(filepath)) 
				{	for(String line : lines)
					{	writer.write(line + System.lineSeparator());	}	
				}
			}
			catch(IOException e)
			{
				System.out.println("sorry, an error occured, provider was not deleted/n");
			}	
		}
	
		public static void editProvider(String name, int id, int choice, String newVal){
			String idString = String.valueOf(id);
			List<String> lines = new ArrayList<>();
	
			try
			{
				//collect all the current lines
				List<String> fullLines = Files.readAllLines(Paths.get(filepath));
			//finding the correct line:
			for(int i = 0; i < fullLines.size(); i++)
			{
				String line = fullLines.get(i);
				String[] items = line.split(",");

				
				if(items[0].equalsIgnoreCase(name) && items[1].equalsIgnoreCase(idString))
				{
					items[choice - 1] = newVal;
					String newLine = "";
					for(String item: items){
						newLine = newLine + item +",";
					}
					lines.add(newLine);
				} else{
					lines.add(line);
				}
				
				
				
			}

			//Overwrite the current file with the new information 
			try (//Overwrite the current file with the new information 
					FileWriter writer = new FileWriter(filepath)) {
						for(String line : lines)
						{
							writer.write(line + System.lineSeparator());
						}
					}
		}
		catch(IOException e)
		{
			System.out.println("sorry, an error occured, provider was not deleted/n");
		}

	}
}	
	