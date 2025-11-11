

//Prewritten classes we need:
import java.util.Scanner; //import to take input
// import java.util.stream.Gatherer.Integrator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;       
import java.lang.String;
import java.security.Provider;

/*
 * Main Class
 * Authors: Kaitlyn Hanson, Ainsley Lewis, Eoin Ohearn
 * 
 * TODO:
 * 	Operator Functions
 * 		Member: updateMember
 * 		Provder: updateProvider
 * 	Provider
 * 		Request Provider Report
 * 		Request Member Report
 * 		Verify Billing
 * 		Billing ChocAn
 * 	Manager 
 * 		Request Reports (stems from other request functions)
 */ 

//classes we create 

// import javax.imageio.stream.ImageInputStream;

// import ReportGenerator.*;
// import SoftwareOperator.Member;
// import SoftwareOperator.Provider;

// import start.HelloWorldSwing;
// import Billing.*; 

public class ChocAn extends JPanel implements ActionListener{
	protected JTextField textField;
    protected JTextField passwordField;
    protected JComboBox<String> cb;
    JPanel cards; //a panel that uses CardLayout
    final static String PROVIDER = "Provider";
    final static String OPERATOR = "Operator";
    final static String MANAGER = "Manager";

    String username = "";
    int userID = 0;



	public void addComponentToPane(Container pane) {
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        String comboBoxItems[] = { PROVIDER, OPERATOR, MANAGER };
        cb = new JComboBox<String>(comboBoxItems);
        cb.setEditable(false);
        comboBoxPane.add(cb);

        textField = new JTextField(20);
        textField.addActionListener(this);
        
        passwordField = new JPasswordField(20);
        passwordField.addActionListener(this);
        JLabel label = new JLabel("ChocAn Software");
        JLabel usernameLabel = new JLabel("Username or Id");
        JLabel passwordLabel = new JLabel("Password");
        JLabel userType = new JLabel("User Type");

        JButton b1 = new JButton("Submit");
        b1.setActionCommand("submit");
        b1.addActionListener(this);



        JPanel card1 = new JPanel();
        card1.setLayout(new BoxLayout(card1, BoxLayout.PAGE_AXIS));

        card1.add(label);
        card1.add(usernameLabel);
        card1.add(textField);
        card1.add(passwordLabel);
        card1.add(passwordField);
        card1.add(userType);
        card1.add(comboBoxPane);
        card1.add(b1);


        //Card 2 Operator Menu
        JPanel card2 = new JPanel();
        card2.setLayout(new BoxLayout(card2, BoxLayout.PAGE_AXIS));
        card2.add(new JLabel("Operator Menu"));
        card2.add(new JLabel("What would you like to do?"));
        
        JButton b10 = new JButton("Add New Member");
        b10.setActionCommand("AddNewMember");
		b10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String _name = JOptionPane.showInputDialog(null, "Enter their name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter their ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);
                String _address = JOptionPane.showInputDialog(null, "Enter their address:","Address Input",JOptionPane.INFORMATION_MESSAGE);
                String _city = JOptionPane.showInputDialog(null, "Enter their city:","City Input",JOptionPane.INFORMATION_MESSAGE);
                String _state = JOptionPane.showInputDialog(null, "Enter their state:","State Input",JOptionPane.INFORMATION_MESSAGE);
                String _zipText = JOptionPane.showInputDialog(null, "Enter their Zip Code:","Zip Input",JOptionPane.INFORMATION_MESSAGE);
                String _status = "A";

    
                        
                // Check for empty fields (you can add more checks as needed)
                if (_name.isEmpty() || _idText.isEmpty() || _address.isEmpty() || _city.isEmpty() || _state.isEmpty() || _zipText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!");
                    return; // Prevent further execution
                }
        
                // Try parsing the ID and Zip code
                try {
                    int _id = Integer.parseInt(_idText); 
                    
                    // Create a new member object and process it
                    SoftwareOperator.Member member = new SoftwareOperator.Member(_name, _id, _address, _city, _state, _zipText, _status);
                    member.writeToData(); 
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }
            }
        });
        


        
        JButton b11 = new JButton("Delete Existing Member");
        b11.setActionCommand("DeleteMember");
        b11.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				String _name = JOptionPane.showInputDialog(null, "Enter their name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter their ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);
				String _address = "";
				String _city = "";
				String _state = "";
				String _zip = "";
				String status = "";

				if (_name.isEmpty() || _idText.length() != 9 ) {
					JOptionPane.showMessageDialog(null, "Name and ID required!");
					return;
				}
				
                try{
                    int _id = Integer.parseInt(_idText);
                    
                    SoftwareOperator.Member member = new SoftwareOperator.Member(_name, _id, _address, _city, _state, _zip, status);
				    member.deleteMember(_name, _id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }

				
            }
        });
        
        JButton b12 = new JButton("Update Member Records");
        b12.setActionCommand("UpdateMember");
        b12.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                
                String _name = JOptionPane.showInputDialog(null, "Enter their name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter their ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);

                Object[] possibleValues = { "1 - Name", "2 - ID", "3 - Address", "4 - City", "5 - State", "6 - Zip Code", "7 - Status" };

                Object selectedValue = JOptionPane.showInputDialog(null,
                            "Choose one", "Input",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            possibleValues, possibleValues[0]);

                String _choiceString = String.valueOf(selectedValue);
                
                int _choice = Character.getNumericValue(_choiceString.charAt(0));
                
				
                String _newVal = JOptionPane.showInputDialog(null, "Enter the new value:","New Value Input",JOptionPane.INFORMATION_MESSAGE);

                String _address = "";
                String _city = "";
                String _state = "";
                String _zip = "";
                String _status = "";



                if (_name.isEmpty() || _idText.length() != 9 ) {
                    JOptionPane.showMessageDialog(null, "Name and ID required!");
                    return;
                }
                
                try{
                    int _id = Integer.parseInt(_idText);

                    
                    SoftwareOperator.Member member = new SoftwareOperator.Member(_name, _id, _address, _city, _state, _zip, _status);
                    member.editMember(_name, _id, _choice, _newVal);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }
    
                    
                
            }
        });
        
        JButton b13 = new JButton("Add New Provider");
        b13.setActionCommand("AddProvider");
        b13.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String _name = JOptionPane.showInputDialog(null, "Enter their name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter their ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);
                String _address = JOptionPane.showInputDialog(null, "Enter their address:","Address Input",JOptionPane.INFORMATION_MESSAGE);
                String _city = JOptionPane.showInputDialog(null, "Enter their city:","City Input",JOptionPane.INFORMATION_MESSAGE);
                String _state = JOptionPane.showInputDialog(null, "Enter their state:","State Input",JOptionPane.INFORMATION_MESSAGE);
                String _zipText = JOptionPane.showInputDialog(null, "Enter their Zip Code:","Zip Input",JOptionPane.INFORMATION_MESSAGE);
                String _email = JOptionPane.showInputDialog(null, "Enter their email:","Email Input",JOptionPane.INFORMATION_MESSAGE);
                

    
                        
                // Check for empty fields (you can add more checks as needed)
                if (_name.isEmpty() || _idText.isEmpty() || _address.isEmpty() || _city.isEmpty() || _state.isEmpty() || _zipText.isEmpty() || _email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!");
                    return; // Prevent further execution
                }
        
                // Try parsing the ID and Zip code
                try {
                    int _id = Integer.parseInt(_idText); 
                    int _zip = Integer.parseInt(_zipText);
                    
                    // Create a new member object and process it
                    SoftwareOperator.Provider provider = new SoftwareOperator.Provider(_name, _id, _address, _city, _state, _zip, _email);
                    provider.writeToData(); 
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }
            }
        });
        
        JButton b14 = new JButton("Delete Existing Provider");
        b14.setActionCommand("DeleteProvider");
        b14.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				String _name = JOptionPane.showInputDialog(null, "Enter their name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter their ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);
				String _address = "";
				String _city = "";
				String _state = "";
				int _zip = -1;
				String _email = "";

				if (_name.isEmpty() || _idText.length() != 9 ) {
					JOptionPane.showMessageDialog(null, "Name and ID required!");
					return;
				}
				
                try{
                    int _id = Integer.parseInt(_idText);

                    
                    SoftwareOperator.Provider provider = new SoftwareOperator.Provider(_name, _id, _address, _city, _state, _zip, _email);
				    provider.deleteProvider(_name, _id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }

				
            }
        });
        
        JButton b15 = new JButton("Update Provider Records");
        b15.setActionCommand("UpdateProvider");
        b15.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                
                String _name = JOptionPane.showInputDialog(null, "Enter their name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter their ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);

                Object[] possibleValues = { "1 - Name", "2 - ID", "3 - Address", "4 - City", "5 - State", "6 - Zip Code", "7 - Email" };

                Object selectedValue = JOptionPane.showInputDialog(null,
                            "Choose one", "Input",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            possibleValues, possibleValues[0]);

                String _choiceString = String.valueOf(selectedValue);
                
                int _choice = Character.getNumericValue(_choiceString.charAt(0));
                
				
                String _newVal = JOptionPane.showInputDialog(null, "Enter the new value:","New Value Input",JOptionPane.INFORMATION_MESSAGE);

                String _address = "";
                String _city = "";
                String _state = "";
                int _zip = -1;
                String _email = "";



                if (_name.isEmpty() || _idText.length() != 9 ) {
                    JOptionPane.showMessageDialog(null, "Name and ID required!");
                    return;
                }
                
                try{
                    int _id = Integer.parseInt(_idText);

                    
                    SoftwareOperator.Provider provider = new SoftwareOperator.Provider(_name, _id, _address, _city, _state, _zip, _email);
                    provider.editProvider(_name, _id, _choice, _newVal);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }
    
                    
                
            }
        });

        JButton b16 = new JButton("Log Out");
        b16.setActionCommand("Logout");
        b16.addActionListener(this);

        card2.add(b10);
        card2.add(b11);
        card2.add(b12);
        card2.add(b13);
        card2.add(b14);
        card2.add(b15);
        card2.add(b16);

        //Card 3 Provider Menu
        JPanel card3 = new JPanel();
        card3.setLayout(new BoxLayout(card3, BoxLayout.PAGE_AXIS));
        card3.add(new JLabel("Provider Menu"));
        card3.add(new JLabel("What would you like to do?"));


        JButton b3 = new JButton("Request Provider Service Report");
        b3.setActionCommand("RequestProviderServiceReport");
        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				String _weekText = JOptionPane.showInputDialog(null, "Enter the Week Number:","Week Input",JOptionPane.INFORMATION_MESSAGE);
				String _email = findProviderEmail(username);

				
		
                try{
                    int _week = Integer.parseInt(_weekText);

                    ReportGenerator.ProviderServiceReport psr = new ReportGenerator.ProviderServiceReport(username, userID, _week, _email);
                    String text = psr.readGUI();

                    JOptionPane.showMessageDialog(null, text,"Report",JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }

				
            }
        });

        JButton b4 = new JButton("Request Member Service Report");
        b4.setActionCommand("RequestMemberServiceReport");
        b4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				String _name = JOptionPane.showInputDialog(null, "Enter the member's name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter the member's ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);
                String _weekText = JOptionPane.showInputDialog(null, "Enter the Week Number:","Week Input",JOptionPane.INFORMATION_MESSAGE);
				String _email = findProviderEmail(username);

				if (_name.isEmpty() || _idText.length() != 9 ) {
					JOptionPane.showMessageDialog(null, "Name and ID required!");
					return;
				}
				
                try{
                    int _id = Integer.parseInt(_idText);
                    int _week = Integer.parseInt(_weekText);

                    ReportGenerator.MemberServiceReport msr = new ReportGenerator.MemberServiceReport(_name, _id, _week, _email);
                    String text = msr.readGUI();

                    JOptionPane.showMessageDialog(null, text,"Report",JOptionPane.INFORMATION_MESSAGE);

                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }

				
            }
        });

        JButton b5 = new JButton("Verify Billing Information for the week");
        b5.setActionCommand("VerifyBillingInformation");
        b5.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				Object[] possibleValues = { "1 - Input Service Information", "2 - View Records for the current week"};

                Object selectedValue = JOptionPane.showInputDialog(null,
                            "Choose one", "Input",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            possibleValues, possibleValues[0]);

                String _choiceString = String.valueOf(selectedValue);
                
                int _choice = Character.getNumericValue(_choiceString.charAt(0));
                switch(_choice){
                    case 1:
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userID + ".txt", true)))
						        {
						            String _name = JOptionPane.showInputDialog(null, "Input the member's name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                                    String _idText = JOptionPane.showInputDialog(null, "Input the member's ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);
                                    String _date = JOptionPane.showInputDialog(null, "Input Current Date (MM-DD-YYYY)","Date Input",JOptionPane.INFORMATION_MESSAGE);
                                    String _currentTime = JOptionPane.showInputDialog(null, "Input Current Time (HH:MM:SS)","Time Input",JOptionPane.INFORMATION_MESSAGE);
                                    String _serviceCode = JOptionPane.showInputDialog(null, "Input Service Code","Service Code Input",JOptionPane.INFORMATION_MESSAGE);
                                    String _serviceFee = JOptionPane.showInputDialog(null, "Input Service Fee","Service Fee Input",JOptionPane.INFORMATION_MESSAGE);
				                    writer.write(_date + ",");
				                    writer.write(_currentTime + ",");
				                    writer.write(_name + ",");
				                    writer.write(_idText + ",");
				                    writer.write(_serviceCode + ",");
				                    writer.write(_serviceFee + ",");
                                    writer.newLine();

                                    JOptionPane.showMessageDialog(null, "Service information has been recorded.");

						        }
						        catch(IOException ex)
						        {
						            System.out.println("An error has occurred.");
						            ex.printStackTrace();
						        }

                    break;

                    case 2:
                        String fileName = userID + ".txt";
                        BufferedReader reader = null;
                        
                        try
                        {
                            reader = new BufferedReader(new FileReader(fileName));
                            String line;
                            String lines = "";

                            // Read and print each line from the file
                            while ((line = reader.readLine()) != null) {
                                lines = lines + line + "\n";
                            }
                    
                            JOptionPane.showMessageDialog(null, lines);
                        }
                        catch(IOException ex)
                        {
                            System.out.println("An error occurred while reading the file.");
                            ex.printStackTrace();
                        }

                        

                    break;
                }
            }
        });

        JButton b6 = new JButton("Bill ChocAn for a Member's Service");
        b6.setActionCommand("BillChocAn");
        b6.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				String _name = JOptionPane.showInputDialog(null, "Enter the member's name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter the member's ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);
                String _weekText = JOptionPane.showInputDialog(null, "Enter the Week Number:","Week Input",JOptionPane.INFORMATION_MESSAGE);
				
                try{
                    int _id = Integer.parseInt(_idText);
                    int _week = Integer.parseInt(_weekText);

                    String status = findMemberStatus(_id);

                    if(status.equalsIgnoreCase("S")){	

                        JOptionPane.showMessageDialog(null, "Member Suspended");

                    } 
                    else if(status.equalsIgnoreCase("A")){	
                        String memberType = "Member";
						boolean memberVerified = verifyIdentity(_name, _id, "Member");
						if(memberVerified) {	
                            JOptionPane.showMessageDialog(null,"Member Verified");
                        }
					}

                    String dateofService = JOptionPane.showInputDialog(null, "Input Date of Service (MM-DD-YYYY)","Date of Service Input",JOptionPane.INFORMATION_MESSAGE);
                    String currentDate = JOptionPane.showInputDialog(null, "Input Current Date (MM-DD-YYYY)","Current Date Input",JOptionPane.INFORMATION_MESSAGE);
                    String currentTime = JOptionPane.showInputDialog(null, "Input Current Time (HH:MM:SS)","Current Time Input",JOptionPane.INFORMATION_MESSAGE);


                    String providerDirectory = "DataCenter\\ProviderDirectory.txt";
					readAndPrintFile(providerDirectory);

                    String serviceCodeString = JOptionPane.showInputDialog(null, "Input Service Code","Current Time Input",JOptionPane.INFORMATION_MESSAGE);
                    String comments = JOptionPane.showInputDialog(null, "Enter Comments or N/A","Current Time Input",JOptionPane.INFORMATION_MESSAGE);

                    int serviceCode = Integer.parseInt(serviceCodeString);

                    String serviceName = findServiceName(serviceCode); 
					double fee = findFee(serviceCode);


                    Billing.Services service = new Billing.Services(username, userID, currentDate, currentTime, dateofService, 
								_name, _id, serviceCode, serviceName, fee, comments);
					service.writeToData(); 
                    
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }

                
               

				
            }
        });

        JButton b20 = new JButton("Request Provider Directory");
        b20.setActionCommand("RequestProviderDirectory");
        b20.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				String providerDirectory = "DataCenter\\ProviderDirectory.txt";
				readAndPrintFile(providerDirectory);
            }
        });


        JButton b17 = new JButton("Log Out");
        b17.setActionCommand("Logout");
        b17.addActionListener(this);


        card3.add(b3);
        card3.add(b4);
        card3.add(b5);
        card3.add(b6);
        card3.add(b20);
        card3.add(b17);

        //Card 4 Manager Menu
        JPanel card4 = new JPanel();
        card4.add(new JLabel("Manager Menu"));
        card4.add(new JLabel("What would you like to do?"));
        card4.setLayout(new BoxLayout(card4, BoxLayout.PAGE_AXIS));

        JButton b7 = new JButton("Request a Provider's Service Report");
        b7.setActionCommand("ProviderServiceReport");
        b7.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				String _name = JOptionPane.showInputDialog(null, "Enter the provider's name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter the provider's ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);
                String _weekText = JOptionPane.showInputDialog(null, "Enter the Week Number:","Week Input",JOptionPane.INFORMATION_MESSAGE);
				
                String _email = findManagerEmail(username);

                try{
                    int _id = Integer.parseInt(_idText);
                    int _week = Integer.parseInt(_weekText);

                    ReportGenerator.ProviderServiceReport psr = new ReportGenerator.ProviderServiceReport(_name, _id, _week, _email);
                    String text = psr.readGUI();

                    JOptionPane.showMessageDialog(null, text,"Report",JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }

            }
        });

        JButton b8 = new JButton("Request a Member's Service Report");
        b8.setActionCommand("MemberServiceReport");
        b8.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				String _name = JOptionPane.showInputDialog(null, "Enter the member's name:","Name Input",JOptionPane.INFORMATION_MESSAGE);
                String _idText = JOptionPane.showInputDialog(null, "Enter the member's ID:","ID Input",JOptionPane.INFORMATION_MESSAGE);
                String _weekText = JOptionPane.showInputDialog(null, "Enter the Week Number:","Week Input",JOptionPane.INFORMATION_MESSAGE);
				
                String _email = findManagerEmail(username);

                try{
                    int _id = Integer.parseInt(_idText);
                    int _week = Integer.parseInt(_weekText);

                    ReportGenerator.MemberServiceReport msr = new ReportGenerator.MemberServiceReport(_name, _id, _week, _email);
                    String text = msr.readGUI();

                    JOptionPane.showMessageDialog(null, text,"Report",JOptionPane.INFORMATION_MESSAGE);

        
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }

            }
        });

        JButton b9 = new JButton("Request a Weekly Service Report");
        b9.setActionCommand("WeeklyServiceReport");
        b9.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				String _weekText = JOptionPane.showInputDialog(null, "Enter the Week Number:","Week Input",JOptionPane.INFORMATION_MESSAGE);
				
                String _email = findManagerEmail(username);

                try{
                    int _week = Integer.parseInt(_weekText);

                    ReportGenerator.WeeklyServiceReport wsr = new ReportGenerator.WeeklyServiceReport(_week, _email);
                    String text = wsr.readGUI();

                    JOptionPane.showMessageDialog(null, text,"Report",JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format! Please enter valid numbers for ID and Zip.");
                }

            }
        });

        JButton b18 = new JButton("Log Out");
        b18.setActionCommand("Logout");
        b18.addActionListener(this);

        card4.add(b7);
        card4.add(b8);
        card4.add(b9);
        card4.add(b18);

		




        cards = new JPanel(new CardLayout());
        cards.add(card1, "login");
        cards.add(card2, OPERATOR);
        cards.add(card3, PROVIDER);
        cards.add(card4, MANAGER);
		// cards.add(card5, "information");

        pane.add(cards, BorderLayout.CENTER);        
    }

	public void actionPerformed(ActionEvent evt) {

        CardLayout cl = (CardLayout)(cards.getLayout());
        if("submit".equals(evt.getActionCommand())){
            username = textField.getText();
            userID = Integer.parseInt(passwordField.getText());
            Object type = cb.getSelectedItem();
			String userType = String.valueOf(type);
            // System.out.println(username+ password+ type);
			boolean Verified = verifyIdentity(username, userID, userType);
			if(Verified){
				cl.show(cards, userType);
			}
            // cl.show(cards, "manager");
        }
        else if("Logout".equals(evt.getActionCommand())){
            cl.show(cards, "login");
        }
    

            
    }
	

	public static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("ChocAn Software");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ChocAn gui = new ChocAn();
		gui.addComponentToPane(frame.getContentPane());

		frame.setSize(300,300);
		
		//Display the window.
		frame.pack();
        frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
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
            br.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return isVerified;
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
        String lines = "";

        // Read and print each line from the file
        while ((line = reader.readLine()) != null) {
            lines = lines + line + "\n";
        }

        JOptionPane.showMessageDialog(null, lines);
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


}

