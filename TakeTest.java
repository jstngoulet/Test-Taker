import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;

/*
	This class will extend JPanel.
	The purpose of this class is to take in a directory and display all of the contents in a random order.
		This way, all of the questions change order each time a test is taken.
		
		Also, this class will display a	question label, 
											answer labels, 
											radio buttons, 
											a next button, 
											a back button,
											a Jcombo with all questions (show question numbers)
		
		This will work by gathering all questions, answers, and correct in arrayList. To do this, I must create a helper class that
			takes in each of the above. Then, using the arraylist, I can add the question String, the Answer[] String and the correct String.
			
			Once I shuffle the arraylist order, I will display one question at a time. When the user presses the back button (or next) btn, the 
				answer they have selected will be saved. If there isnt an answer saved, the answer will be wrong.
				
			Once the user itereates through all questions, a JDialog will be shown if there are any empty answers.
			
			Then, the score will be calculated by how many qustions possible over how many correct (Percent form)
			
			The grade will be given in a seperate class where all stats will be given.
				In this class, the user will have an option to export their test (with results and correct) to a text file
					so that they may study harder.

*/

public class TakeTest extends JPanel
{
	//Labels
	JLabel questionCountLbl, incorrectLabel;
	
	//Text Area
	JTextArea questionArea, results;
	JTextArea answerArea[] = new JTextArea[4];
	
	//Scrol Pane
	JScrollPane scroll;
	JScrollPane[] answersScroll = new JScrollPane[5];
	
	//Button Group
	ButtonGroup gp;
	
	//Radio Buttons
	JRadioButton answerSelect[] = new JRadioButton[5];
	
	//Arraylist of Type TestQuestion
	ArrayList <Question> content = new ArrayList <Question>();
	
	//Question counters
	int questionNum = 0, quesTot = 0, numCorrect = 0, questionMax = 0, WIDTH, HEIGHT;
	float grade;
	
	//String answ
	String answerPos[];
	String type, incorrect = "", testCorrect = "", curDirect, curTest, curClass, curDate;
	
	//Buttons
	JButton nextQuestion;
	
	
	public TakeTest(int width, int height, String dir, String test, String cl, int maxQuestions)
	{
		//Create Panel
		setBounds(5, 5, width - 10, height - 10);
		setBackground(Color.WHITE);
		curDirect = dir;
		questionMax = maxQuestions;
		setLayout(null);
		//System.out.println("Directory of Choice: " + dir);
		buildLayout(width - 10, height - 10, dir);
		WIDTH = width - 10;
		HEIGHT = height - 10;
		setVisible(true);
		curTest = test;
		curClass = cl;
	}
	
	public void buildLayout(int width, int height, String dir)
	{
		//Get all files
		parseDir(new File(dir));
		Collections.shuffle(content);
		
		//quesTot = getUpdatedCount();
		quesTot = questionMax;
		
		questionCountLbl = new JLabel("Question: " + (questionNum + 1) + " of " + (quesTot));
		questionCountLbl.setBounds(10, 5, width - 25, 20);
		questionCountLbl.setFont(new Font("Arial Black", Font.PLAIN, 18));
		add(questionCountLbl);
		
		//Add Text Area
		questionArea = new JTextArea(5, 0);
		questionArea.setBounds(15, 50, width - 30, questionCountLbl.getHeight() *5);
		questionArea.setText("\n     " + content.get(questionNum).getQuestion());
		questionArea.setEditable(false);
		questionArea.setCaretPosition(0);
		//questionArea.setBackground(Color.LIGHT_GRAY);
		questionArea.setWrapStyleWord(true);
		questionArea.setLineWrap(true);
		scroll = new JScrollPane (questionArea, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroll);
		scroll.setBounds(questionArea.getBounds());
		scroll.setWheelScrollingEnabled(true);
		
		//Get type (Change the for loop by type (4 or 2))
		
		//Add Answer fields
		int offset = 0, num;
		
		//Create a group
		gp = new ButtonGroup();
		
		//Get answers
		answerPos = content.get(questionNum).getAnswers();
		
		//Get type
		type = content.get(questionNum).getType();
		int typeNum = 0;
		if (type.equals("Multiple Choice")) {
			typeNum = 4;
		}
		else if (type.equals("Translation")) {
			typeNum = 4;
		}
		else if (type.equals("Definition")) {
			typeNum = 4;
		}
		else if (type.equals("Conjugate Verb")) {
			typeNum = 4;
		}
		else if (type.equals("True or False")) {
			typeNum = 2;
		}
		
		for (int i = 0; i < 4; i++) {
			answerArea[i] = new JTextArea();
			answerArea[i].setBounds(scroll.getX() + 50, questionArea.getY() + questionArea.getHeight() + 8 + offset, questionArea.getWidth() - 50, 38);
			answerArea[i].setEditable(false);
			if (i < answerPos.length) {
				answerArea[i].setText(answerPos[i]);
				answerArea[i].setCaretPosition(0);
			}
			//answerArea[i].setBackground(Color.ORANGE);
			offset += 40;
			answerArea[i].setWrapStyleWord(true);
			answerArea[i].setLineWrap(true);
			answersScroll[i] = new JScrollPane (answerArea[i], 
					   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			answersScroll[i].setBounds(answerArea[i].getBounds());
			answersScroll[i].setWheelScrollingEnabled(true);
			add(answersScroll[i]);
			
			//Now add JRadioButtons
			answerSelect[i] = new JRadioButton();
			answerSelect[i].setBounds(answersScroll[i].getX() - 25, answersScroll[i].getY(), answersScroll[i].getWidth(), answersScroll[i].getHeight());
			add(answerSelect[i]);
			gp.add(answerSelect[i]);
			if (i >= typeNum) {
				answersScroll[i].setVisible(false);
				answerSelect[i].setVisible(false);
			}

		}
		
		//Add Next Question button
		nextQuestion = new JButton("Next");
		nextQuestion.setBounds(width/2 - 50, height - 50, 100, 25);
		nextQuestion.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Check if there is another question
				if (questionNum < quesTot - 1) {
					showNextQuestion();
				}
				else {
					//When no more questions, show final screen
					endTest();
				}
			}
		});
		add(nextQuestion);

	}
	
	public int getUpdatedCount()
	{
		int userInput = 0;
		//System.out.println("Question Count: " + quesTot);
		
		//Ask the user how many questions they want on the exam (if more than ten)
		if (quesTot > 10) {
			//System.out.println("Changing Count...");
			//Ask
			while (userInput == 0) {
				String input = JOptionPane.showInputDialog("Question Count is higher then 10.\nPlease set your question count");
				
				if ((!input.equals(null)) || (!input.trim().equals(""))) {
					try {
						userInput = Integer.parseInt(input);
						if (userInput > quesTot) {
							userInput = 0;
							JOptionPane.showMessageDialog(new JFrame(), "Please select a valid number from 1 - " + quesTot);
						}
						else {
							quesTot = userInput;
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(new JFrame(), "Please select a valid number from 1 - " + quesTot);
						userInput = 0;
					}
				}
				else {
					userInput = quesTot;
				}
			}
		}
		else {
			userInput = quesTot;
		}
		return userInput;
	}
	
	public void endTest()
	{
		//Get last question results
		calculateCorrect();
		
		//Now, show results screen
		showResults();
	}
	
	public void calculateCorrect()
	{
		//Get type
		type = content.get(questionNum).getType();
		
		int typeNum = 0;
		if (type.equals("Multiple Choice")) {
			typeNum = 4;
		}
		else if (type.equals("Translation")) {
			typeNum = 4;
		}
		else if (type.equals("Definition")) {
			typeNum = 4;
		}
		else if (type.equals("Conjugate Verb")) {
			typeNum = 4;
		}
		else if (type.equals("True or False")) {
			typeNum = 2;
		}
		testCorrect += "Question " + (questionNum + 1) + " of " + quesTot + ": " + content.get(questionNum).getQuestion() + "\n\n     Answer Selected: ";
		
		boolean sele = false;
		
		//Calculate if correct
		for (int i = 0; i < typeNum; i++) 
		{
			if (answerSelect[i].isSelected()) {
				//Get the answerArea text
				String a = answerArea[i].getText();
				a = a.replace("\n", " || ");
				sele = true;
				//System.out.println(answerArea[i].getText());
				if (a.equalsIgnoreCase(content.get(questionNum).getCorrect())) {
					numCorrect++;
					testCorrect += " (√ Correct!) " + a + "\n\n";
				}
				else {
					//If incorrect, add to a String 
					incorrect += "Question " + (questionNum + 1) + " of " + (quesTot) + ": " + content.get(questionNum).getQuestion() + "\n\n     Selected Answer: " + a + "\n     Correct Answer: " + content.get(questionNum).getCorrect() + "\n\n";
					testCorrect += " ( -- Incorrect -- ) " + a;
					//Add Correct to string
					testCorrect += "\n     Correct Answer: " + content.get(questionNum).getCorrect() + "\n\n";
				}
			}
			
		}
		if (!sele) {
			//If no answer was selected
			incorrect += "» Question " + (questionNum + 1) + " of " + (quesTot) + ": " + content.get(questionNum).getQuestion() + "\n\n     Correct Answer: " + content.get(questionNum).getCorrect() + " (None Selected)\n\n";
			testCorrect += " (None Selected) ";
			//Add Correct to string
			testCorrect += "\n     ◊ Correct Answer: " + content.get(questionNum).getCorrect() + "\n\n";
		}
		
		
	}
	
	public void showResults()
	{
		//Calculate Grade
		grade = (float)numCorrect/(float)quesTot;
		//System.out.println(grade);
		
		//Hide answers
		for (int i = 0; i < 4; i++) {
			answersScroll[i].setVisible(false);
			answerSelect[i].setVisible(false);
			answerArea[i].setVisible(false);
		}
		//Unselect radio
		gp.clearSelection();
		
		//Hide Question Area
		scroll.setVisible(false);
		
		//Hide next Button
		nextQuestion.setVisible(false);
		
		//Hide questions possible label
		questionCountLbl.setVisible(false);
		
		//Create a label in the center of the screen with a big font to show the grade
		JLabel gradeLbl = new JLabel();
		float percent = (float)grade;
		DecimalFormat df = new DecimalFormat("#,###,##0.##");
		String s = (df.format(grade*100)) + " %";
		gradeLbl.setText(s);
		gradeLbl.setHorizontalAlignment(SwingConstants.CENTER);
		gradeLbl.setVerticalAlignment(SwingConstants.CENTER);
		gradeLbl.setFont(new Font("Ariel Black", Font.BOLD, 100));
		
		
		if ((grade <= 1.0) && (grade > .8)){
			gradeLbl.setForeground(Color.GREEN);
		}
		else if ((grade <= .8) && (grade > .7)) {
			gradeLbl.setForeground(Color.ORANGE);
		}
		else if ((grade <= .7) && (grade > .6)) {
			gradeLbl.setForeground(Color.ORANGE);
		}
		else {
			gradeLbl.setForeground(Color.RED);
		}
		
		gradeLbl.setBounds(5, 0, WIDTH - 10, HEIGHT/2);
		add(gradeLbl);
		
		//Add a text Area to see all of the incorrect questions with the correct answers
		results = new JTextArea(incorrect);
		results.setBounds(10, HEIGHT - 150, WIDTH - 20, 145);
		//add(results);
		results.setWrapStyleWord(true);
		results.setEditable(false);
		results.setLineWrap(true);
		JScrollPane scroller = new JScrollPane (results, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroller);
		incorrect = incorrect.trim();
		//System.out.println("Incorrect: " + incorrect + ";");
		
		if (grade == 1) 
		{
			//System.out.println("Incorrect: " + incorrect + ";");
			results.setText(testCorrect);
		}
		
		scroller.setBounds(results.getBounds());
		scroller.setWheelScrollingEnabled(true);
		
		//Add a label so the user knows that they are the ones that are incorrect
		incorrectLabel = new JLabel("Incorrect Answers: ");
		incorrectLabel.setBounds(scroller.getX(), scroller.getY() - 25, scroller.getWidth(), 25);
		incorrectLabel.setForeground(Color.RED);
		add(incorrectLabel);
		
		if (grade == 1) 
		{
			//System.out.println("Incorrect: " + incorrect + ";");
			incorrectLabel.setText("Correct Results: ");
			incorrectLabel.setForeground(Color.BLACK);
		}
		
		//Scroll to top of scrolling view
		results.setCaretPosition(0);
		
		//Create File
		PrintWriter out;
		String tempFile;
		//System.out.println("Current Directory: " + curDirect);
		createDirectory("Previous Tests", "");
		createDirectory("Previous Tests", curClass);
		try {
			curDate = getDate();
			out = new PrintWriter(new FileOutputStream("Previous Tests/" + curClass + "/" + curTest + " " + curDate + ".txt"));
			out.println("Grade Received: " + (df.format(grade*100)) + " %");
			out.println("Total Correct: " + numCorrect + " / " + quesTot);
			out.println();
			out.println();
			out.println(testCorrect.trim());
			out.close();
		} catch (Exception a) {
			a.printStackTrace();
		}
		
		//Add a button to export test (Just question, selected, and correct)
		//Please note that "testCorrect" is the variable to send to a file for exporting
			//Also note that some formatting should be done before test is opened.
			//Add borders, date of test printed, total correct, total questions, etc.
		JButton exportBtn = new JButton("Open File");
		exportBtn.setBounds(results.getX() + results.getWidth() - 135, incorrectLabel.getY(), 130, 25);
		exportBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Export to a file here
				//Name the file the test name with the date taken
				//If already exported, just open it
				boolean done = false;
				
				//EDIT: This should only open the file. The test should be save automatically
				
				if (!done) 
				{
					//Open File just Created
					new openFile("Previous Tests/" + curClass + "/" + curTest + " " + curDate + ".txt");
				}
				else if (done){
					//Just open it adn relabel btn
					exportBtn.setText("Open File");
					new openFile("Previous Tests/" + curClass + "/" + curTest + " " + curDate + ".txt");
				}
			}
		});
		add(exportBtn);
		
		//Add a button to the left of the "Open File" Button to print using system defaults
		JButton printBtn = new JButton("Print Results");
		printBtn.setBounds(results.getX() + results.getWidth() - 270, incorrectLabel.getY(), 130, 25);
		printBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new PrintFile("Previous Tests/" + curClass + "/" + curTest + " " + curDate + ".txt");
			}
		});
		add(printBtn);
			
		//Create another file, (or append using class) with stats of this test. If the file
			//Exists, read in current data then increment test count and such. There should be 
			//A different class for this that gathers the amount incorrect and the amount correct.
			//Each test has their own stats file as well, so there should be just one master one.
			//The individual stats file will be hidden in each directory(. in front) and read when a test
			//Is selected from the corresponding JCombo
	}
	
	public void createDirectory(String location, String file)
		{
			File f = new File(location + "/" + file);
			try{
			    if(f.mkdir()) { 
			        System.out.println("Directory <" + file + "> Created");
			    } else {
			        //JOptionPane.showMessageDialog(new JFrame(), "Directory is not created");
			    }
			} 
			catch(Exception z)
			{
			    z.printStackTrace();
			} 
		}
	
	public Font setFontOfLabel(JLabel label)
	{
		Font labelFont = label.getFont();
		String labelText = label.getText();

		int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
		int componentWidth = label.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double)componentWidth / (double)stringWidth;

		int newFontSize = (int)(labelFont.getSize() * widthRatio);
		int componentHeight = label.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);

		// Set the label's font size to the newly determined size.
		labelFont = new Font("Ariel Black", Font.BOLD, fontSizeToUse);
		return labelFont;
	}
	
	public void showNextQuestion()
	{
		calculateCorrect();
		
		//Hide answers
		for (int i = 0; i < 4; i++) {
			answersScroll[i].setVisible(false);
			answerSelect[i].setVisible(false);
			answerArea[i].setVisible(false);
		}
		//Unselect radio
		gp.clearSelection();
		
		//Increment question number
		questionNum++;
		questionCountLbl.setText("Question: " + (questionNum + 1) + " of " + (quesTot));
		
		//Get answers
		answerPos = content.get(questionNum).getAnswers();
		
		//Get type
		type = content.get(questionNum).getType();
		
		int typeNum = 0;
		if (type.equals("Multiple Choice")) {
			typeNum = 4;
		}
		else if (type.equals("Translation")) {
			typeNum = 4;
		}
		else if (type.equals("Definition")) {
			typeNum = 4;
		}
		else if (type.equals("Conjugate Verb")) {
			typeNum = 4;
		}
		else if (type.equals("True or False")) {
			typeNum = 2;
		}
		
		//Get Question
		questionArea.setText("\n     " + content.get(questionNum).getQuestion()); 
		questionArea.setCaretPosition(0);
		
		
		for (int i = 0; i < answerPos.length; i++) {
			answersScroll[i].setVisible(true);
			answerSelect[i].setVisible(true);
			answerArea[i].setVisible(true);
			answerArea[i].setText(answerPos[i]);
			answerArea[i].setCaretPosition(0);
		}
	}
	
	public void parseDir(File dirPath)
    {
        File files[] = null;
        if(dirPath.isDirectory())
        {
            files = dirPath.listFiles();
            for(File dirFiles:files)
            {

                if(dirFiles.isDirectory())
                {
                    parseDir(dirFiles);
                }
                else
                {
                    if((dirFiles.getName().endsWith(".txt") && (!dirFiles.getName().startsWith("."))))
                    {
                        //do your processing here....
						  TestQuestion t = new TestQuestion(dirFiles.toString());
						String q = t.getQuestion();
						String[] an = t.getAnswers();
						//System.out.println("Question: " + questionNum + "\n");
						for (String a : an) {
							//System.out.println(a);
						}
						Collections.shuffle(Arrays.asList(an));						String c = t.getCorrect();
						String ty = t.getType();
						content.add(new Question(ty, q, an, c));
						quesTot++;
						//System.out.println("Question: " + q + "\nAnswers: " + an.toString() + "\nCorrect: " + c + "\nType: " + ty);
							
                    }
                }
            }

        }
        else
        {
            if(dirPath.getName().endsWith(".txt"))
            {
                //do your processing here....
					System.out.println(dirPath.getName().toString());
            }
        }

    }
	private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}




















