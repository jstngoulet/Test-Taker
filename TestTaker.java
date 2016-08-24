import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class TestTaker extends JFrame 
{
	//Init Vars
	public static final int _WIDTH = 600, _HEIGHT = 450;
	
	//Init Specal classes for JPanels
	public Welcome wl;
	
	public static void main(String[] args) 
	{
		new TestTaker();
	}
	
	public TestTaker()
	{
		super("Test Taker");
		setSize(_WIDTH, _HEIGHT);
		setLayout(null);
		setResizable(false);
		buildComp();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void buildComp()
	{
		//Add Main Screen using class (This will just be on top of everything)
		wl = new Welcome(_WIDTH, _HEIGHT, "Review Classes");
		add(wl);
	}
	
	
	
	
	
	
	
	
}