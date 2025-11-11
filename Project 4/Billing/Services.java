package Billing;
import java.io.*;

public class Services {
    private String providerName;
    private int providerNumber;
    private String currentDate;
    private String currentTime;
    private String serviceDate;
    private String memberName;
    private int memberNumber;
    private int serviceId;
    private double fee;
    private String comments;
    private String serviceName;
    public String filepath = "Billing\\AllServices.txt";

    // Constructor
    public Services(String providerName, int providerNumber, String currentDate, String currentTime, String serviceDate, 
                   String memberName, int memberNumber, int serviceId, String ServiceName, double fee, String comments) 
    {   this.providerName = providerName;
        this.providerNumber = providerNumber;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.serviceDate = serviceDate;
        this.memberName = memberName;
        this.memberNumber = memberNumber;
        this.serviceId = serviceId;
        this.fee = fee;
        this.comments = comments;
        this.serviceName = ServiceName; 
    }

    // getters
    public String getProviderName() { return providerName; }
    public int getProviderNumber() { return providerNumber; }
    public String getCurrentDate() { return currentDate; }
    public String getCurrentTime() { return currentTime; }
    public String getServiceDate() { return serviceDate; }
    public String getMemberName() { return memberName; }
    public int getMemberNumber() { return memberNumber; }
    public int getServiceId() { return serviceId; }
    public double getFee() { return fee; }
    public String getComments() { return comments; }
    public String getServiceName() { return serviceName; }
    //setters
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public void setProviderNumber(int providerNumber) { this.providerNumber = providerNumber; }
    public void setCurrentDate(String currentDate) { this.currentDate = currentDate; }
    public void setCurrentTime(String currentTime) { this.currentTime = currentTime; }
    public void setServiceDate(String serviceDate) { this.serviceDate = serviceDate; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public void setMemberNumber(int memberNumber) { this.memberNumber = memberNumber; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public void setFee(double fee) { this.fee = fee; }
    public void setComments(String comments) { this.comments = comments; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public void writeToData() {
    	String line = providerName + "," +
    				  providerNumber + "," +
    				  currentDate + "," +
    				  currentTime + "," +
    				  serviceDate + "," +
    				  memberName + "," +
    				  memberNumber + "," +
    				  serviceId + "," +
    				  serviceName + "," +
    				  fee + "," +
    				  comments + "\n" ;
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
}
