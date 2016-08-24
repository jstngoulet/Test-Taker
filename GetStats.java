import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class GetStats
{
	ArrayList <String> files = new ArrayList <String>();
	public Scanner inputStream;
	public File[] filesList;
	public String fileString[], currentTest, bestGrade;
	ArrayList <String> grades = new ArrayList <String>();
	ArrayList <String> sortedGrades = new ArrayList <String>();
	ArrayList <Integer> attempt = new ArrayList <Integer>();
	ArrayList <String> question = new ArrayList <String>();
	ArrayList <Integer> totCor = new ArrayList <Integer>();
	boolean sorted = false;
	
	public int attempts = 0, questions = 0, totQuesCor;
	public double average = 0;
	
	public GetStats(String curClass, String curTest)
	{
		currentTest = curTest;
		
		//Get all folders and files within the folder and return the stats
		fileString = getFolders("Previous Tests/" + curClass + "/");
		//System.out.println("Class: " + curClass);
		//System.out.println("Test: " + curTest);
		
		//Create Stream
		try 
		{
			if (filesList != null) {
				for (String i : fileString) 
				{
					//System.out.println("Files: " + i);
					//Read File Contents
					inputStream = new Scanner(new FileInputStream("Previous Tests/" + curClass + "/" + i));
					
					attempts++;
					
					//Read in Data
					while (inputStream.hasNextLine()) 
					{
						String temp = inputStream.nextLine();
						
						//Check what the file is reading
						if (temp.startsWith("Grade Received: ")) {
							//This is the grade
							grades.add(temp.substring(16, temp.length()));
						}
						else if (temp.startsWith("Total Correct: ")) 
						{
							temp = temp.substring(15, temp.length());
							//System.out.println("Temps: " + temp);
							String hold[] = temp.split("\\ / ");
							String co = hold[0].toString();
							String outOf = hold[1].toString();
							//System.out.println("Hold: " + hold);
							totQuesCor = Integer.parseInt(outOf);
							totCor.add(totQuesCor);
						}
					}
					
					Collections.sort(totCor);
				}
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	public String getAttemptCount()
	{
		String tem = "";
		if (attempts > 0) {
			tem = attempts + "";
		}
		else {
			tem = "None";
		}
		return tem;
	}
	
	public String getQuestionCount(String curSem, String curClass, String curTest)
	{
		String tem = "";
		
		//Get total questions from questions.arraylist
		String[] que = getQuestions(curSem, curClass, curTest);
		//System.out.println(que.toString());
		
		if (que.length > 0) {
			tem = que.length + "";
		}
		else {
			tem = "None";
		}
		return tem;
	}
	
	public String getBestGrade()
	{
		for (String a : grades) {
			sortedGrades.add(a);
		}
		Collections.sort(sortedGrades);
		//System.out.println("Grades: " + grades.toString());
		
		if (sortedGrades.size() > 0) {
			if (sortedGrades.contains("100 %")) {
				bestGrade = "100 %";
			}
			else {
				bestGrade = sortedGrades.get(sortedGrades.size() - 1);
			}
			
			int counter = 0;
			//Get count of the grade
			for (int i = 0; i < sortedGrades.size(); i ++) {
				if (sortedGrades.get(i).toString().equals(bestGrade))
				{
					counter++;
				}
			}
			bestGrade += " (" + counter + ")";
		}
		else {
			bestGrade = "Not Yet";
		}
		return bestGrade;
	}
	
	public String[] getGrades()
	{
		return grades.toArray(new String[grades.size()]);
	}
	
	public String getAverageGrade()
	{
		if (grades.size() > 0) {
			
			int tot = 0;
			//calculate the average
			for (int a = 0; a < grades.size(); a++) 
			{
				String temp = grades.get(a);
				String temp1 = temp.substring(0, temp.length() - 2);
				tot += Double.parseDouble(temp1);
			}
			
			average = (double)tot/(double)grades.size();
			
			DecimalFormat df = new DecimalFormat("#,###,##0.##");
			String s = (df.format(average)) + " %";
			return s;
		}
		else {
			return "Not Yet";
		}
	}
	
	public String[] getFolders(String dirName)
	{
		File dir = new File(dirName);
		filesList = dir.listFiles();
		files.clear();
		//System.out.println("CurDirect: " + dirName);
		if (filesList != null) {
			for( File f : dir.listFiles())
			{
				if ((!f.getName().toString().startsWith(".")) && (f.getName().toString().endsWith(".txt")) && (f.getName().toString().startsWith(currentTest))){
					files.add(f.getName().toString());
					//System.out.println("Found: " + f.getName() );
				}
				else if (f.isDirectory()) {
					for (File a : f.listFiles()) 
					{
						//System.out.println("File is a directory");
						if ((!a.getName().toString().startsWith(".")) && (a.getName().toString().endsWith(".txt"))){
							files.add(f.getName().toString() + "/" + a.getName().toString());
							//System.out.println("Found: " + a.getName() );
				}
					}
				}
				
			}
		}
		
		
		//Add arraylist items to array
		String[] stringArray = files.toArray(new String[files.size()]);
		Arrays.sort(stringArray);
		return stringArray;
	}
	
	
	
	public String[] getQuestions(String curSem, String curClass, String curTest)
	{
		//Get all of the questions for the test selected here
		//Do this by reading in every file in the folder of the test. Each one is a question.
		//Then, count them up
		//System.out.println("Getting Questions");
		String[] files = getFolders("Files/" + curSem + "/" + curClass + "/" + curTest);
		//System.out.println("Directory: " + "Files/" + curSem + "/" + curClass + "/" + curTest);
		
		//Scan each file for the correct question and answer
		Scanner inputStream = null;
		
		for (String i : files) 
		{
			//System.out.println("I = " + i);
			try {
				inputStream = new Scanner(new FileInputStream("Files/" + curSem + "/" + curClass + "/" + curTest + "/" + i));
				
				while (inputStream.hasNextLine()) 
				{
					String temp = inputStream.nextLine().toString();
					
					if (temp.startsWith("Correct Answer: ")) {
						question.add(temp.substring(10, temp.length()));
						//System.out.println("Adding Question: " + temp);
					}
				}
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(), e);
			}
		}
		
		//We will be using this method for StudyJam ® and Flash cards mode. we will need to also get their correct answers
		//We might need to create another class so that the flash cards can be written as a question and answer arraylist
		String[] questions = question.toArray(new String[question.size()]);
		return questions;
	}
	
	
	
	
	
	
	
}