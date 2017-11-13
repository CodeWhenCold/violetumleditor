package com.horstmann.violet.framework.file;

import javax.swing.*;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


//this class generates a Violet UML window with the stat file's info 
//it takes the file that was passed by JfileChooserService.java, reads it, and writes it onto the new window
public class statistics extends JFrame {
	
	public statistics(File selectedFile) throws IOException {
		JFrame mainFrame = new JFrame();
		mainFrame.setSize(500,500);
		JPanel mainPanel = new JPanel();
		Scanner statFile = new Scanner(selectedFile);
		
		while (statFile.hasNextLine()){
			
			//for each line in the txt file create a new label that will display what was written in that line
			JLabel label = new JLabel(statFile.nextLine());
			
			//add each label to the stat window
			mainPanel.add(label);
		}
		//clears stat file after it's been read and rewritten on new window
		statFile.close();

		//finalize creation of window
		mainFrame.add(mainPanel);
		mainFrame.setVisible(true);
	}
}
