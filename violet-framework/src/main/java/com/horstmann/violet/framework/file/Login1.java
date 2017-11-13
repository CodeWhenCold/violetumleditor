package com.horstmann.violet.framework.file;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class Login1 implements ActionListener 
{
	private IOSystem io = new IOSystem("Users and Passwords.txt");
	private JFrame frame;
	private JButton enter;
	private JButton cancel;
	private JTextField field1;
	private JPasswordField field2;

	public Login1()
	{
		frame = new JFrame("Log in");
		frame.setLayout(new FlowLayout()); //allows multiple labels/buttons in a jframe
		frame.setSize(200, 300);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y); //setslocation,above code will get it at center
		JLabel username = new JLabel();
		username.setText("username: ");
		frame.add(username);
		field1 = new JTextField(10);
		frame.add(field1);
		JLabel pass = new JLabel();
		pass.setText("password: ");
		frame.add(pass);
		field2 = new JPasswordField(10);
		field2.setEchoChar('*');
		frame.add(field2);
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		frame.add(enter);
		frame.add(cancel);
		enter.addActionListener(this);
		cancel.addActionListener(this);
		frame.setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(enter))
		{
		String username = field1.getText();
		char[] p = field2.getPassword(); //returns password as a char array
		String password = new String(p);
		try {
			if(io.checkExistance(username,password))
			{
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "You are logged on");
				new SelectStats();
				
			}
			else
			{
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "Incorrect Username or password");
			
			}
		}
		catch(IOException ioerror) {System.out.println("There is an io error");}
		}
		else //cancel was pressed
		{
			frame.setVisible(false);
		}
	}
}
