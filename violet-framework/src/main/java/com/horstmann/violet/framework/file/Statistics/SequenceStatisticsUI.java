package com.horstmann.violet.framework.file.Statistics;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.io.*;
import java.util.ArrayList;

public class SequenceStatisticsUI {
	private JFrame frame;
	private JTextArea stats;
	private File file;
	private int numOfObjects = 0;
	private int sumOfOutgoing = 0; //sum of out going messages
	private int sumOfReturn = 0; //sum of outgoing
	private ArrayList<String> objectNames = new ArrayList<String>();
	private ArrayList<String> outGoing = new ArrayList<String>();
	private ArrayList<String> returnMessages = new ArrayList<String>();
	
	
	
	public SequenceStatisticsUI(String filename)
	{
		file = new File("SequenceModelStatistics"+ File.separator + filename);
		if(!file.exists())
		{
			JFrame error = new JFrame("Error");
			JOptionPane.showMessageDialog(error, "A problem has occured, file specified not found");
		}
		    frame = new JFrame("Sequence Statistics");
			frame.setLayout(new FlowLayout()); //allows multiple labels/buttons in a jframe
			frame.setSize(700, 500);
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
			int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
			frame.setLocation(x, y); //setslocation,above code will get it at center
			try {
				getData();
			}
			catch(IOException e)
			{
				JFrame error = new JFrame("Error!");
				JOptionPane.showMessageDialog(error, "IO Exception thrown at getData()");
				return;
			}
			try {
				displayStats();
			}
			catch(IOException e)
			{
				JFrame error = new JFrame("Error!");
				JOptionPane.showMessageDialog(error, "IO Exception thrown at displayStats() in SequenceModelUI");
				return;
			}
			frame.setVisible(true);
	}
	private void getData() throws IOException //stores all information in global variables
	{
		int line = 0;
		FileReader reader = new FileReader(file);
		BufferedReader inputFile = new BufferedReader(reader);
		String s;
		//get data from file
		while((s=inputFile.readLine()) !=null)
		{
			String split[] = s.split(" ");
			if(line == 0)
			{
				for(int i=0;i<split.length;i++)
				{
					objectNames.add(split[i]);
				}
				line++;
				
			}
			else if(line == 1)
			{
				numOfObjects = Integer.parseInt(s);
				line++;
				
			}
			else if(line == 2)
			{
				for(int i=0;i<split.length;i++)
				{
					outGoing.add(split[i]);
				}
				line++;
			}
			else if(line == 3)
			{
				for(int i=0;i<split.length;i++)
				{
					returnMessages.add(split[i]);
				}
			}
			
			
		}
	}
	private void displayStats() throws IOException
	{
		stats = new JTextArea();
		stats.setEnabled(false);//creates uneditable textarea
		stats.setBackground(Color.black);
		//Object Names
		stats.append("Objects: ");
		for(int i=0;i<objectNames.size();i++)
		{
			if(i == objectNames.size()-1)
			{
				stats.append(objectNames.get(i) + "\n");
			}
			else
			{
			stats.append(objectNames.get(i) + " ");
			}
		}
		stats.append("\n");
		//number of objects
		String objects = Integer.toString(numOfObjects);
		stats.append("There are " + objects+ " objects in this diagram.\n");
		stats.append("\n");
		//outgoing messages per object
		stats.append("Out Going Messages: ");
		for(int i=0;i<outGoing.size();i++)
		{
			if(i == outGoing.size()-1)
			{
				stats.append(objectNames.get(i) + ": " + outGoing.get(i) + "\n");
				sumOfOutgoing+=Integer.parseInt(outGoing.get(i));
				//warning
				if(Integer.parseInt(outGoing.get(i)) > 4)
				{
					JFrame error = new JFrame("Warning");
					JOptionPane.showMessageDialog(error, "High outgoing messages at: " + objectNames.get(i));
				}
				
			}
			else
			{
				stats.append(objectNames.get(i) + ": " + outGoing.get(i) + " ");
				sumOfOutgoing+=Integer.parseInt(outGoing.get(i));
				//warning
				if(Integer.parseInt(outGoing.get(i)) > 4)
				{
					JFrame error = new JFrame("Warning");
					JOptionPane.showMessageDialog(error, "High outgoing messages at: " + objectNames.get(i));
				}
			}
		}
		stats.append("\n");
		//return messages per object
		stats.append("Return Messages: ");
		for(int i=0;i<returnMessages.size();i++)
		{
			if(i == returnMessages.size()-1)
			{
				stats.append(objectNames.get(i) + ": " + returnMessages.get(i) + "\n");
				sumOfReturn+=Integer.parseInt(returnMessages.get(i));
				
			}
			else
			{
				stats.append(objectNames.get(i) + ": " + returnMessages.get(i) + " ");
				sumOfReturn+=Integer.parseInt(returnMessages.get(i));
			}
		}
		stats.append("\n");
		//averages
		stats.append("Averages: ");
	    stats.append("Average out going messages per object: " + sumOfOutgoing / numOfObjects +
	    	  " Average return messages per object: " + sumOfReturn / numOfObjects);
		frame.add(stats);
	}
	
}
