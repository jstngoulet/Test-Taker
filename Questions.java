import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class Questions 
{
	public PrintWriter out;
	
	public Questions(String dir, String type, String ques, String[] ans, String correct) 
	{
		//Create a fileName
		String fileName = type + "/" + ques;
		
		File f = new File("Files/" + dir + "/" + type);
		//System.out.println("File Name: " + f.toString());
		createDirectory(f.toString(), "");
		
		//Scan the answers. If there is a new line, remove it and replace with " || "
		int counter = 0;
		for (String i : ans) {
			ans[counter] = i.replace("\n", " || ");
			counter++;
		}
		
		//Create an output to a file
		try {
			if (ques.length() > 30) {
				out = new PrintWriter(new FileOutputStream(f.toString() + "/" + ques.substring(0, 30) + ".txt"));
			}
			else
			{
				out = new PrintWriter(new FileOutputStream(f.toString() + "/" + ques + ".txt"));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//Get the question Type
		out.println("Question Type: " + type);
		out.println("Question: " + ques);
		
		int in = 0;
		for (String i : ans) 
		{
			out.println("Answer # " + (in + 1) + ": " + i.substring(0, 1).toUpperCase() + i.substring(1, i.length()));
			in++;
		}
		
		
		//Get the correct answer
		out.println();
		out.println("Correct Answer: " + correct);
		
		//Close the file
		out.close();
		JFrame dia = new JFrame();
		
		//JOptionPane.showMessageDialog(dia, "Question: \n\n" + ques + " \n\tAnswer: " + correct + "\n\nCreated!");
		
	}
	

	public void createDirectory(String location, String file)
	{
		File f = new File(location + "/" + file);
		//System.out.println("New File: " + f.toString());
		try{
		    if(f.mkdir()) { 
		        //System.out.println("Directory <" + location + "> Created");
		    } else {
		        //JOptionPane.showMessageDialog(new JFrame(), "Directory is not created");
		    }
		} 
		catch(Exception z)
		{
		    z.printStackTrace();
		} 
	}
}