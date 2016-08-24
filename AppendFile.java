
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.util.*;


public class AppendFile 
{
	public Scanner inputStream;
	public ArrayList <String> master = new ArrayList <String>();
	public PrintWriter out;
	
	public AppendFile(String fileName, String wordToAdd)
	{
		//Create Stream
		try 
		{
			System.out.println("File Name: " + fileName);
			System.out.println("Words: " + wordToAdd);
			//Read File Contents
			inputStream = new Scanner(new FileInputStream("Files/" + fileName));
		} 
		catch (Exception e) 
		{
			System.out.println(e);
			
			try 
			{
				//Create a new File
				out = new PrintWriter(new FileOutputStream("Files/" + fileName));
				out.println(fileName.substring(fileName.length() - 3, fileName.length()));
				
				out.close();
			} 
			catch (Exception a) 
			{
				System.out.println(a);
			}	
		}
		if (inputStream.hasNext()) 
		{
			while (inputStream.hasNext()) 
			{
				//Read in file contents and add to arraylist
				if (!master.contains(inputStream.next())) {
					master.add(inputStream.next());
				}
				
			}
		}
		
		
		//Now that the current contents of the file are added, lets add our new word
		master.add(wordToAdd);
		
		//Close the input stream
		inputStream.close();
		
		//Sort the arraylist
		Collections.sort(master);
		
		//Open output stream again
		try {
			out = new PrintWriter(new FileOutputStream("Files/" + fileName));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		
		//Write the new word to the original list
		for (String i : master) 
		{
			if (master.contains(i)) {
				out.println(i);
			}
		}
		
		//Close outputstream
		out.close();
	}
}