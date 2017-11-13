package com.horstmann.violet.framework.file;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class SelectStats implements  ActionListener
{
	private JFrame frame;
	private JButton classModel,sequence,allStats,cancel;
  public SelectStats() 
  {
	  frame = new JFrame("Select Statistics to View");
	  frame.setLayout(new FlowLayout()); //allows multiple labels/buttons in a jframe
	  frame.setSize(800, 1000);
	  Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	  int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	  int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	  frame.setLocation(x, y); //setslocation,above code will get it at center
	  classModel = new JButton("Class Model");
	  sequence  = new JButton("Sequence Diagram");
	  allStats = new JButton("Visualization");
	  cancel = new JButton("Cancel");
	  frame.add(classModel);
	  frame.add(sequence);
	  frame.add(allStats);
	  frame.add(cancel);
	  cancel.addActionListener(this);
	  frame.setVisible(true);
	  
  }
  public void actionPerformed(ActionEvent e) //for now just implement cancel button functionality
  {
	  frame.setVisible(false);
  }
}
