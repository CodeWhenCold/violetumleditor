package com.horstmann.violet.framework.file;

import java.io.*;
//note we can also store users in an arraylist as well as a file, just make arraylist read from file
public class IOSystem //reads write to username/password file.
{
	private String filename;
	private FileWriter writer;
	private PrintWriter outputFile;
	private FileReader reader;
	private BufferedReader  inputFile;
	
	public IOSystem(String filename) 
	{
		this.filename = filename;
		try {
		File myFile = new File(filename);
		if(!myFile.exists()) //if file doesn't exist create it
		{
			writer = new FileWriter(filename,true); 
			outputFile = new PrintWriter(writer);
			outputFile.close();
		}
		}
		catch(IOException ioerror) {System.out.println("There is an io error"); }
		
	}
	public void addUser(String username,String password) throws IOException
	{
		writer = new FileWriter(filename,true); //for appending  = true
		outputFile = new PrintWriter(writer);
		outputFile.println(username + " " + password);
		outputFile.close();
	
	}
	public boolean checkUsername(String u) throws IOException //checks if a username is in file
	{
		reader = new FileReader(filename);
		inputFile = new BufferedReader(reader);
		String s = "";
		String username;
		while((s = inputFile.readLine()) !=null)
		{
			String[] split = s.split(" ");
			username = split[0];
			if(username.equals(u))
			{
				inputFile.close();
				return true;
			}
		}
		inputFile.close();
		return false;
	
	}
	public boolean checkExistance(String u,String pass) throws IOException //checks for both username and pass
	{
		reader = new FileReader(filename);
		inputFile = new BufferedReader(reader);
		
		String s;
		String username;
		String password;
		while((s=inputFile.readLine()) !=null)
		{
			String[] split = s.split(" ");
			username = split[0];
			password = split[1];
			if(username.equals(u) && password.equals(pass))
			{
				inputFile.close();
				return true;
			}
		}
		inputFile.close();
		return false;
	}
	
	
}
