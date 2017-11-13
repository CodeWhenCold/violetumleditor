package com.horstmann.violet.framework.file;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MainWindow implements ActionListener
{
	private JButton signUp;
	private JButton login;
	public MainWindow()
	{
		JFrame frame1 = new JFrame("Visualization Home");
		frame1.setLayout(new FlowLayout()); //allows multiple labels/buttons in a jframe
		frame1.setSize(200,300);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame1.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frame1.getHeight()) / 2);
		frame1.setLocation(x, y); //setslocation,above code will get it at center
		signUp = new JButton("Sign Up");
		signUp.addActionListener(this);
		frame1.add(signUp);
		login = new JButton("Login");
		frame1.add(login);
		login.addActionListener(this);
		
		frame1.setVisible(true);//put false to hide it during certain sections
	}
	public void actionPerformed(ActionEvent e) //open up sign in window
	{
		if(e.getSource().equals(signUp))
		{
			new SignUp();
		}
		else //login was selected
		{
			new Login1();

	    }

	}
}
