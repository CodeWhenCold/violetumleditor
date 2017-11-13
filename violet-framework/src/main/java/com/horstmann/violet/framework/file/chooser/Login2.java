package com.horstmann.violet.framework.file.chooser;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

//Login Class Summary
//this class is an implementaion of a login which prompts the user to enter a username and password when they attempt to accesses a statistical file.
//the statistical file consists of a text file that contains the statistical information of a diagram (Sequence stats, Class Model stats, Visualization). 
//Once the user enters the correct username and password they are granted access to the text file's contents

public class Login2 extends JDialog{
	
	private JTextField usernameTextBox;
	private JLabel usernameLabel;
	private JPasswordField passwordTextBox;
	private JLabel passwordLabel;
	private JButton loginButton;
	private JButton cancelButton;
	private JOptionPane optionPane;
	boolean loginCheck;
	public int result;
	final private String testTextFile = "../violet-framework/credentials.txt";

	//this will implement the prompt to the user of entering their username and password
	public Login2(){
		
		//main window size and prompt details
		JPanel window = new JPanel(new GridLayout(4, 4, 4, 4));
		window.add(new JLabel("Please login to access this file."));//adds prompt text to window
		
		JPanel usernameSection = new JPanel();//making a component section for username
		usernameLabel = new JLabel("Enter username: "); //represents written text that will be beside text box
		usernameTextBox = new JTextField(10); //textbox field
		usernameSection.add(usernameLabel);
		usernameSection.add(usernameTextBox);
		
		JPanel passwordSection = new JPanel();//making a component section for password info 
		passwordLabel = new JLabel("Enter password: "); //represents written text that will be beside text box
		passwordTextBox = new JPasswordField(10); //textbox field
		passwordSection.add(passwordLabel);
		passwordSection.add(passwordTextBox);
		
		JPanel buttonSection = new JPanel();//making a component section for buttons 
		loginButton = new JButton("Login");
		cancelButton = new JButton("Cancel");
		buttonSection.add(loginButton);
		buttonSection.add(cancelButton);

		//add in the components you created to the main window
		window.add(usernameSection);
		window.add(passwordSection);
		window.add(buttonSection);
		
		//set placement of window components that were added
		JPanel layoutPanel = new JPanel();
		layoutPanel.add(window, BorderLayout.NORTH);
		layoutPanel.add(buttonSection, BorderLayout.SOUTH);
		
		//finalize setup for window
		Object[] buttons = {loginButton, cancelButton};
		optionPane = new JOptionPane(layoutPanel, JOptionPane.OK_CANCEL_OPTION, JOptionPane.OK_CANCEL_OPTION, null, buttons, null);

		//when login button is pressed (listener) which will then trigger an action event that checks for valid credential input
		loginButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent event){
				try{
					if(authenticate(getUsername(), getPassword())){		
						
						//if true returned login window is closed
						Window loginWindow = SwingUtilities.getWindowAncestor(loginButton);
						if (loginWindow != null){
							loginWindow.setVisible(false);
						}
						
						//this will determine whether the stat file is opened when checked in JFileChooserService
						//in this case stat file will open
						loginCheck = true;
					} 
					else{
						//if false was returned for credentials that were inputted we show a new window that contains an error message for this instance of login
						JOptionPane.showMessageDialog(Login2.this,"Invalid authentication, please try again.","Invalid Login", JOptionPane.ERROR_MESSAGE);
						
						//erasing text to allow user to retry
						usernameTextBox.setText("");
						passwordTextBox.setText("");
						
						//this will determine whether the stat file is opened when checked in JFileChooserService
						//in this case stat file won't open
						loginCheck = false;
					}
				} 
				catch (IOException e){
					e.printStackTrace(System.out);
				}
			}
		});

		//when cancel button is pressed (listener) it will then trigger and action event that closes the window
		cancelButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent event) {
				Window loginWindow = SwingUtilities.getWindowAncestor(cancelButton);				
				if (loginWindow != null)
					loginWindow.setVisible(false);
			}
		});
	}
	
	//this will get passed to the JFileChooserService.java file to notify it whether a login was successful and if the stats can now be shown
	public boolean loginCheck() {
		return loginCheck;
	}
	//return username the user entered
	private String getUsername() {
		return usernameTextBox.getText();
	}

	//return the password user entered
	private String getPassword() {
		return new String(passwordTextBox.getPassword());
	}
	
	//returns the setup window
	public JOptionPane getOptionPane() {
		return optionPane;
	}

	//after user gets prompted to enter username and password and enters credentials
	//we check if the credentials match file that contains the correct credentials
	private boolean authenticate(String usernameText, String passwordText) throws IOException {
		
		File users = new File(testTextFile);
		Scanner kb = new Scanner(users);
		String s = kb.nextLine();
		String[] credentials = s.split(" ");
		
		//split both username and password in file into separate indices facilitating comparison with credentials user entered
		//if match found return true
		if (usernameText.equals(credentials[0]) && passwordText.equals(credentials[1])){
			kb.close();
			return true;
		}
		//if didn't match return false
		kb.close();
		return false;
	}
}
