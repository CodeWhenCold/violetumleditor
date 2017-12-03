package com.horstmann.violet.framework.file.Statistics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.horstmann.violet.framework.util.XMLManager;
import java.util.List;
import java.io.*;
public class ClassModelStatisticsUI {
	private JFrame frame;
	private JTextArea stats;
	private File file;
	private int numberofClasses = 0; //these 3 data members used for averages
	private int sumofCBO = 0;
	private int sumofSizes = 0;
	//open directory object here, get file from there
	
	
	public ClassModelStatisticsUI(String filename) 
	{
		file = new File("ClassModelStatistics" + File.separator + filename);
		if(!file.exists())
		{
			JFrame error = new JFrame("Error");
			JOptionPane.showMessageDialog(error, "A problem has occured, file specified not found");
		}
	    frame = new JFrame("Class Statistics");
		frame.setLayout(new FlowLayout()); //allows multiple labels/buttons in a jframe
		frame.setSize(700, 500);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y); //setslocation,above code will get it at center
		try {
		getAverages();
		}
		catch(Exception e)
		{
			JFrame error = new JFrame("Error!");
			JOptionPane.showMessageDialog(error, "IO Exception thrown at getAverages()");
			return;
		}
		try {
			displayStats();
		}
		catch(Exception e)
		{
			JFrame error = new JFrame("Error!");
			JOptionPane.showMessageDialog(error, "IO Exception thrown at displayStats() in ClassModelUI");
			return;
		}
		
		frame.setVisible(true);
		
	}
	private void getAverages() throws IOException
	{
		int counterofClasses = 0;
		int classNumber= 1;
		int line = 0;
		FileReader reader = new FileReader(file);
		BufferedReader inputFile = new BufferedReader(reader);
		String s;
		//get data from file
		while((s=inputFile.readLine()) !=null)
		{
		    String[] split = s.split(" ");
			if(line == 0) //get number of classes
			{
				numberofClasses = Integer.parseInt(split[split.length-1]);
			
			}
			else if(line>=7 && counterofClasses <numberofClasses) //get sum of class sizes
			{
				int size = Integer.parseInt(split[split.length-1]);
				sumofSizes+=size;
				counterofClasses++;
			}
			else if(line>=(7 + (numberofClasses + 2)))
			{
				int cbo = Integer.parseInt(split[split.length-1]);
				if(cbo>4) //high cbo warning
				{
					JFrame error = new JFrame("Warning");
					JOptionPane.showMessageDialog(error, "High CBO at class " + split[3]);
					classNumber++;
				}
				sumofCBO+=cbo;
			}
			
			line++;
		}
		inputFile.close();
	}
		private void displayStats() throws IOException //display statistics with averages
		{
	
			stats = new JTextArea();
			stats.setEnabled(false);//creates uneditable textarea
			stats.setBackground(Color.black);
			FileReader reader = new FileReader(file);
			BufferedReader inputFile = new BufferedReader(reader);
			String s;
			while((s=inputFile.readLine()) !=null)
			{
				stats.append(s+ "\n");
			}
			stats.append("\n");
			int average = 0;
			String averageString = "";
			inputFile.close();
			stats.append("Averages: \n");
			average = sumofSizes /numberofClasses;
			averageString = Integer.toString(average);
			stats.append("Average size of a Class: " + averageString );
			average = sumofCBO/numberofClasses;
			averageString = Integer.toString(average);
			stats.append(" Average CBO of a class: "+ averageString);
			frame.add(stats);
			
			
			
		
			
			
			
		}
}
