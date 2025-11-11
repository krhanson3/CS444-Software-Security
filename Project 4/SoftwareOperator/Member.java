//Authors: Kyra Peters, Ainsley Lewis

package SoftwareOperator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Member {
	//data
	//Name,Phone,Address,City,State,ZIP
	public String name;
	public int phone;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String status;
	public static String filepath = "DataCenter/MemberData.txt";
	
		public Member(String name, int phone, String address, String city, String state, String zip, String status){
			this.name = name;
			this.phone = phone;
			this.address = address;
			this.city = city;
			this.state = state;
			this.zip = zip;
			this.status = status;
		}
	
		
		public void writeToData(){
			String line =   name + "," +
							phone + "," + 
							address + "," +
							city + "," +
							state + "," +
							zip + "," +
							status + "\n";
	
			try(FileWriter writer = new FileWriter(filepath, true))
			{
				writer.write(line);
				writer.close();
			} 
			catch(IOException e)
			{
				System.out.println("Sorry, an error occured\n");
			}
		}
	
		public void deleteMember(String name, int phone){
			String phoneString = String.valueOf(phone);	
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
	
					if(items[0].equalsIgnoreCase(name) && items[1].equalsIgnoreCase(phoneString))
					{
						continue;
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
			{	System.out.println("sorry, an error occured, provider was not deleted/n");	}
		}
	
		// Choice is 1-6 , so it will edit items[0] - items[5]
		public static void editMember(String name, int id, int choice, String newVal){
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
				} else {
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
			System.out.println("sorry, an error occured, provider was not edited/n");
		}

	}

	
}