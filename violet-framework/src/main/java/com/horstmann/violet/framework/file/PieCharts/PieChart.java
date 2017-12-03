package com.horstmann.violet.framework.file.PieCharts;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;


public class PieChart {
	
	private static final String REGEX ="Size of Class: ";
	private String fileName;
	
	public ArrayList<String> Classes;
	public ArrayList<Integer>ClassSize;
	String diagramType = "";
	String directoryAndFile = "";
	

	
	public PieChart(){ }
	
	public PieChart(String fileName,String diagram){
		diagramType = diagram;
		Classes = new ArrayList<>();
		ClassSize = new ArrayList<>();
		this.fileName = fileName;
		
		if(diagramType == "ClassModel")
		{
			directoryAndFile = "ClassModelStatistics" + File.separator + fileName;
			read_ClassModelFile(directoryAndFile);
		}
		else
		{
			directoryAndFile = "SequenceModelStatistics" + File.separator + fileName;
			System.out.println(directoryAndFile);
			read_Sequencefile(directoryAndFile);
		}
			
		PieChart();
		
	}
	
	public void read_ClassModelFile(String fileName){
		
		String line ="";
		Scanner sc = null;
		int count = 0;
		
		Pattern p = Pattern.compile(REGEX);
		
		try{
			sc = new Scanner(new FileReader(fileName));
				
			while(sc.hasNextLine()){
				line = sc.nextLine();
					
				if(count == 2)
					Classes = getClassNames(line);
						
				Matcher m = p.matcher(line);
				while(m.find()){
						String st = line.substring(m.end());
						ClassSize.add(Integer.parseInt(st));
					}
				  ++count;
				} // end of while
			
		}catch(FileNotFoundException ex){
				System.out.println("File unable to open"+ fileName);
		}catch(IOException e){
			System.out.println("Error: An Error has occured to read file" + fileName);
		}finally{
			sc.close();	
		}
		
	}

// reading Sequence file
public void read_Sequencefile(String fileName){
	
		Scanner sc = null;
		String line = "";
		int count = 0;
	
		try{
			sc = new Scanner(new FileReader(fileName));
			
			while(sc.hasNextLine()){
				line = sc.nextLine();
			
				if(count == 0){
					for(String str : line.split("\\s"))
						Classes.add(str.toString());
				}
				if(count == 2){
					for(String str: line.split("\\s"))
						ClassSize.add(Integer.parseInt(str));
				}
				++count;
			}
			sc.close();	
		
		}catch(FileNotFoundException ex){
			System.out.println("File unable to open"+ fileName);
		}catch(IOException e){
			System.out.println("Error: An Error has occured to read file" + fileName);
		}
}


private ArrayList<String> getClassNames(String st){
		
		String newString = st.replaceAll("Name of Classes:","");
		ArrayList<String> classes = new ArrayList<>();
		
		for(String x:newString.split("\\s")){
			String str = x.toString();
			if(!str.isEmpty())
				classes.add(str);
			}
		return classes;
}
	
// calling CreatePieChartClass
public void PieChart(){
	
	CreatePieChart cpc = new CreatePieChart("PieChart","VioletUML Editor",Classes,ClassSize);
	cpc.pack();
	cpc.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	cpc.setVisible(true);
	
}

	
}
