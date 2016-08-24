import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;



public class TestQuestion 
{
	public String question, correct, type, file;
	ArrayList <String> ans = new ArrayList <String>();
	
	public TestQuestion(String fileName) 
	{
		//This is going to get pass the file name to read in the question
		//Read th file and add objects in
		Scanner inputStream = null, nextStream = null;
		file = fileName;
	      try
	      {
	      //Read in scores and names
	         inputStream = new Scanner(new FileInputStream(fileName));

			if (inputStream.hasNext()) 
			{
				while (inputStream.hasNext())
			      {
			      //Get the questions and answers
			         String temp = inputStream.nextLine();
			         
			         //Use if statements 
					if (temp.startsWith("Question Type: ")) {
						type = temp.substring(15, temp.length());
						//System.out.println("Type: " + type);
					}
					else if (temp.startsWith("Answer # ")) {
						temp = temp.replace(" || ", "\n");
						ans.add(temp.substring(12, temp.length()));
					}
					else if (temp.startsWith("Correct Answer: ")) {
						correct = temp.substring(16, temp.length());
						//System.out.println("Correct: " + correct);
					}
					else if (temp.startsWith("Question: ")) {
						//Read in question up until the first answer, then trim it.(Use peek)
						question = temp.substring(10, temp.length());
						question = question.trim();
						//System.out.println("Question: " + question);
					}
					else {
						//System.out.println("Nothing in line");
						question += temp + "\n\n     ";
						question.trim();
					}
			      }
				
			//Close the stream
			inputStream.close();
			
			}
			else {
				String temp = "< List Empty >";
				JOptionPane.showMessageDialog(new JFrame(), temp);
			}
		      
	      }
	      
	      	catch(FileNotFoundException e)
	      	{
				e.printStackTrace();
			}

	}
	
	public String getQuestion()
	{
		//System.out.println("Question: " + question);
		return question.trim();
	}
	
	public String getType()
	{
		//System.out.println("Type: " + type);
		return type;
	}
	
	public String[] getAnswers()
	{
		String[] answers = ans.toArray(new String[ans.size()]);
		int u = 0;
		for (String a : answers) {
			//System.out.println(u + ": " + a);
			answers[u] = a.replace(" || ", " ");
			u++;
		}
		
		//If type is definition or translation, we need to read in the corresponding file for 4 answers.
		if (getType().equals("Translation")) 
		{
			
		}
		else if (getType().equals("Definition")) {
			
		}
		return answers;
	}
	
	public String getCorrect()
	{
		//System.out.println("Correct: " + correct);
		return correct;
	}
	
	
}