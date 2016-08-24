import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class CreateFinal 
{
	ArrayList <Question> questions = new ArrayList <Question> ();
	ArrayList <String> files = new ArrayList <String> ();
	ArrayList <Question> myTestQuestions = new ArrayList <Question>();
	File[] filesList;
	String mySemester, myClass;
	
	public CreateFinal(String curSemester, String curClass)
	{
		//Load in all of the questions for this final.
		//Get the amount of tests within the class
		mySemester = curSemester;
		myClass = curClass;
		String[] myFiles = getFolders("Files/" + curSemester + "/" + curClass);
		
		//Randomly decide if a question will be added to the final exam
		Random rand = new Random();
		int randNum = 0;
		
		for (String i : myFiles) {
			TestQuestion myQuestion = new TestQuestion("Files/" + curSemester + "/" + curClass + "/" + i);
			Question quest = new Question(myQuestion.getType(), myQuestion.getQuestion(), myQuestion.getAnswers(), myQuestion.getCorrect());
			//Generate num
			randNum = rand.nextInt(2);
			//System.out.println("Num: " + randNum);
			switch (randNum) {
				case 0:
					//Only add question if not yet added. (The same question may appear in multiple tests, but we only want one version in the final)
					if (!myTestQuestions.contains(quest.getQuestion())) 
					{
						myTestQuestions.add(quest);
						//Create a file for the Final Exam
						/*
							Please note that this is set up so that two of the same questions cannot be added. If this happens, 
							the first question will automatically be overwritten and ignored within the final exam.
						*/
						Questions printQuestion = new Questions(curSemester + "/" + curClass + "/Final Exam", myQuestion.getType(), myQuestion.getQuestion(), myQuestion.getAnswers(), myQuestion.getCorrect());//Dir, type, ques, ans[], cor
					}
				
					break;
				case 1:
					break;
				default:
					break;
			}
			
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
				if (!f.getName().startsWith(".")) 
				{
					if (f.isDirectory()) {//For the Class
						for (File g : f.listFiles()) {
							if (!g.getName().startsWith(".")) 
							{
								if (g.isDirectory()) {//For the test
									for (File d : g.listFiles()) 
									{
										if (!d.getName().toString().startsWith(".")) {
											//System.out.println("File Found: " + d.getName());
											files.add(f.getName() + "/" + g.getName() + "/" + d.getName());
										}
									}
								}
							}
						}
					}
					else {
						//System.out.println("File Found: " + f.getName());
						files.add(f.getName());
					}
				}
			}
		}
		return files.toArray(new String[files.size()]);
	}
	
}