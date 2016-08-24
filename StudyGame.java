import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.text.*;


public class StudyGame extends JPanel
{
	//Save Variables
	String mySemester, myClass, myTest, directory;
	String selected = "", correct = "";
	String[] folders;
	
	Timer loner = new Timer(1000, new TimerListener());
	
	int width, height, questionsShown = 0, currentCount, score = 0, seconds = 10, answeredCorrect = 0, attempted = 0, totalAdded = 0;
	
	boolean[] checked = new boolean[5];
	
	JPanel main[];
	
	ArrayList <String> files = new ArrayList <String> ();
	ArrayList <Question> myQuestions = new ArrayList <Question> ();
	ArrayList <Question> missedQuestions = new ArrayList <Question> ();
	
	JTextArea questionArea[] = new JTextArea[5];
	JScrollPane questionScroll[] = new JScrollPane[5];
	JLabel questionTitle[] = new JLabel[5];
	JLabel scoreLbl = new JLabel("Score: " + score);
	JLabel timeLbl = new JLabel(seconds + "");
	JButton nextBtn;
	JComboBox answers[] = new JComboBox[5];
	JTextArea missedQuestionsArea;
	JScrollPane missedQuestionsScroller;
	String master = "";
	
	
	public StudyGame(String curSemester, String curClass, String curTest, int w, int h, JPanel[] in)
	{
		//Set Vars
		mySemester = curSemester;
		myClass = curClass;
		myTest = curTest;
		width = w;
		height = h;
		main = in;
		
		//Create the main directory
		directory = "Files/" + mySemester + "/" + myClass + "/" + myTest;
		folders = getFolders(directory);
		
		//Now that we have the files, we can read them in.
		getInfo();
		
		/*
			The point of this game is simple:
				
				1) On start, there will be five questions to view. Beneath each question, there is 
					a JComboBox.
				2) The user must respond to the question with the correct answer from the comboBox as quickly as they can.
				3) The catch? - They only have 10 seconds to answer each set or the game ends. The user gets 3 Seconds for each correct response
					So that they can continue. 
					
						In addition, each correct answer is worth 10 points where incorrect repsponses are -5. Not answering a question results in a 							simple -2. This is because the question will keep appearing anyways until the user gets it right or runs out of time.
						
				4) The game ends when each question has been correctly responded to at least 3 times. Or, time runs out.
					The score for each game is saved in a "master" file with the date earned. 
				
				Notes:
					The question minimum is 10. If there aren't ten questions yet, the user must add them
					This will be overridded anyways becuase each question is added twice.
		
		*/
		
		//Build layout and set visible
		setSize(width, height);
		setLayout(null);
		setBackground(Color.WHITE);
		buildLayout();
		setVisible(true);
	}
	
	public void scan(int cou)
	{
		int index = answers[cou].getSelectedIndex();
		selected = answers[cou].getSelectedItem().toString().replace("\n", " ");
		//System.out.println("Selected: " + selected);
		
		if ((index > 0) && (!checked[cou])) {
			//Check for correct answer
			correct = getCorrect(questionArea[cou].getText().trim());
			
			
			if (selected.equalsIgnoreCase(correct)) {
				//System.out.println("Correct Answer Provided");
				score += 10;
				seconds += 3;
				
				timeLbl.setText(seconds + "");
				addQuestion(myQuestions, questionArea[cou].getText().trim(), 1);
				
				//Update Master
				updateMaster(questionArea[cou].getText().trim(), selected, correct, true);
				
				//Change Background Color
				answers[cou].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
			
				//Change amout correct
				answeredCorrect++;	
				attempted++;
			}
			else {
				//System.out.println("Correct Answer: " + correct);
				score -= 5;
				
				//Add Question back in
				addQuestion(myQuestions, questionArea[cou].getText().trim(), 2);
				
				//Add question to missed questions
				addQuestion(missedQuestions, questionArea[cou].getText().trim(), 1);
				
				//Update Master
				updateMaster(questionArea[cou].getText().trim(), selected, correct, false);
				
				//Change Background Color
				answers[cou].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
				attempted++;
			}
			//Marked as checked
			checked[cou] = true;
			
			//Now, disable the JCombo - The first answer is kept
			answers[cou].setEnabled(false);
			
			//Check to see if all boxes have been selected
			boolean pageComplete = false;
			
			for (JComboBox z : answers) {
				if (z.getSelectedItem().toString().equals("Select Answer")) {
					pageComplete = false;
					break;
				}
				else {
					pageComplete = true;
				}
			}
			if (pageComplete) {
				nextPressed();
			}
		}
		else if ((index == 0) && (!checked[cou])){
			score -= 2;
			addQuestion(myQuestions, questionArea[cou].getText().trim(), 1);
		}
		scoreLbl.setText("Score: " + score);
	}
	
	public void updateMaster(String question, String selected, String correctAnswer, boolean correct)
	{
		if (correct) {
			//Do not add it to the missed questions list
		}
		else {
			//Check to see if the question already exists. If it does, do not include it again in the master, just increase the count
			if (!master.contains(question)) {
				//Add question
				master += "Question: " + question + "\n\n";
				master += "     Answer Selected: " + selected + "\n";
				master += "     Correct Answer: " + correctAnswer + "\n\n";
			}
		}
	}
	
	public void buildLayout()
	{
		/*The layout will be comprised of the following:
			1) five scrolling text areas. This is for each question. 
				Each one will be only 3 rows deep, but will take up half of the screen
				Also, They will be set to start at the top of the view
			2) There is a JComboBox with the possible answers to the right of the top of each scrollview
			3) Respondingly, there is a JtextArea for each scrollview
			4) there will be a label for each question number
		*/
		loner.start();
		for (int i = 0; i < 5; i++) 
		{
			currentCount = i;
			
			//Create ScrollPanes and Text Area
			questionArea[i] = new JTextArea(3, 0);
			questionArea[i].setBounds(30, 35 + (i * 60), width/2 + 50, 60);
			questionArea[i].setText("\n     " + myQuestions.get(i).getQuestion());
			questionArea[i].setEditable(false);
			questionArea[i].setCaretPosition(0);
			questionArea[i].setWrapStyleWord(true);
			questionArea[i].setLineWrap(true);
			questionScroll[i] = new JScrollPane (questionArea[i], 
			   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			add(questionScroll[i]);
			questionScroll[i].setBounds(questionArea[i].getBounds());
			questionScroll[i].setWheelScrollingEnabled(true);
			
			//Now add the basic labels with numbers
			questionTitle[i] = new JLabel((i+1) + "");
			questionTitle[i].setBounds(5, questionScroll[i].getY(), questionScroll[i].getX() - 5, questionScroll[i].getHeight());
			questionTitle[i].setHorizontalAlignment(JLabel.CENTER);
			add(questionTitle[i]);
			
			//Add JComboBoxes
			//Shuffle answers
			Collections.shuffle(Arrays.asList(myQuestions.get(i).getAnswers()));
			
			answers[i] = new JComboBox();
			String tem[] = myQuestions.get(i).getAnswers();
			answers[i].addItem("Select Answer");
			for (int a = 0; a < tem.length; a++) {
				answers[i].addItem(tem[a]);
				totalAdded++;
			}
			
			//Add actionlistener to the JCombo. When the user selects one, it becomes disabled (if not index 0). 
			//Then, once disabled, it will check to see if the answer is correct. 
				//If correct, add the question back into the list.
					//Add points
					//Add time
					//Remove original item
				//If it is wrong, add the questin back twice
					//Subtract Points
					//Shuffle questions
			
			
			answers[i].setBounds(questionScroll[i].getX() + questionScroll[i].getWidth(), questionScroll[i].getY(), width - 10 - (questionScroll[i].getX() + questionScroll[i].getWidth()), 25);
			add(answers[i]);
			questionsShown++;
		}	
			answers[0].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					scan(0);
				}
			});	
				answers[1].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					scan(1);
				}
			});	
				answers[2].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						scan(2);
					}
			});	
				answers[3].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						scan(3);
					}
			});	
				answers[4].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						scan(4);
					}
				});
		//Add Menu btn
		JButton quit = new JButton("Menu");
		quit.setBounds(width - 130, 10, answers[0].getWidth()/2 + 25, answers[0].getHeight());
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Stop timer
				
				//Hide current and remove
				showMain(true);
			}
		});
		add(quit);
		
		//Add Score Label
		scoreLbl.setBounds(questionScroll[0].getX(), 5, width - (questionScroll[0].getX() *2), questionArea[0].getY() - 5);
		scoreLbl.setHorizontalAlignment(JLabel.LEFT);
		scoreLbl.setFont(new Font("Berlin Sans FB", Font.BOLD, 20));
		add(scoreLbl);
		
		timeLbl.setHorizontalAlignment(JLabel.CENTER);
		timeLbl.setFont(new Font("Arial", Font.BOLD, 32));
		add(timeLbl);
		
		//Add next btn
		nextBtn = new JButton("Next Group");
		nextBtn.setBounds(quit.getX(), height - 10 - quit.getHeight(), quit.getWidth(), quit.getHeight());
		quit.setBounds(10, nextBtn.getY(), answers[0].getWidth()/2 + 25, answers[0].getHeight());
		timeLbl.setBounds(5, quit.getY() - 10, width - 10, quit.getHeight() + 15);
		nextBtn.addActionListener(new allMatched());
		add(nextBtn);
	}
	
	public class allMatched implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			nextPressed();
		}
	}
	
	public void nextPressed()
	{
		//Remove all items from each answer box
		int t = 0;
		for (JComboBox i : answers) {
			//First, check to see if it says "Select Answer"
			if (i.getSelectedItem().toString().equals("Select Answer")) {
				addQuestion(myQuestions, questionArea[t].getText().trim(), 1);
			}
			
			for (int a = i.getItemCount() - 1; a > 0; a--) {
				if (!i.getItemAt(a).toString().equals("Select Answer")) {
					i.removeItemAt(a);
				}
			}
			t++;
		}
		
		//Remove all text from JTextAreas and add new
		int areaCounter = 0;
		for (JTextArea i : questionArea) {
			if (questionsShown < myQuestions.size()) {
				i.setText("\n     " + myQuestions.get(questionsShown).getQuestion());
				
				i.setCaretPosition(0);
				
				//Now, add in the answers
				//Retrieve answers first
				String temp[] = myQuestions.get(questionsShown).getAnswers();
				for (int c = 0; c < temp.length; c++) {
					answers[areaCounter].addItem(temp[c]);
				}
				checked[areaCounter] = false;
				answers[areaCounter].setEnabled(true);
				questionScroll[areaCounter].setVisible(true);
				questionTitle[areaCounter].setVisible(true);
				answers[areaCounter].setVisible(true);
				answers[areaCounter].setBorder(null);
				questionsShown++;	
			}
			else {
				questionScroll[areaCounter].setVisible(false);
				questionTitle[areaCounter].setVisible(false);
				answers[areaCounter].setVisible(false);
			}
			areaCounter++;
		}
	}
	
	public String getCorrect(String question)
	{
		String result  = "";
		for (int i = 0; i < myQuestions.size(); i++) {
			//System.out.println("Scanning: " + myQuestions.get(i).getQuestion());
			if (myQuestions.get(i).getQuestion().contains(question.trim())) {
				//System.out.println("Correct Answer: " + myQuestions.get(i).getCorrect());
				result = myQuestions.get(i).getCorrect().replace(" || ", " ");
			}
		}
		return result.trim();
	}
	
	public void getInfo()
	{
		for (String i : folders) 
		{
			//Gets the file to read the information
			TestQuestion quiz = new TestQuestion(i);     
			
			//Create a new Question from that information
			Question q = new Question(quiz.getType(), quiz.getQuestion(), quiz.getAnswers(), quiz.getCorrect());
			
			//Read the data and add to a Questions ArrayList
			myQuestions.add(q);
			myQuestions.add(q);
			totalAdded++;
			totalAdded++;
			
			//Shuffle array every time
			Collections.shuffle(myQuestions);
		}
	}
	
	public void addQuestion(ArrayList <Question> list, String question, int times)
	{
		//Scan myQuestions for existing
		for (int i = 0; i < list.size(); i++) 
		{
			if (list.get(i).getQuestion().contains(question.trim())) {
				//If we found it, add it back
				Question a = new Question(list.get(i).getType(), 
											 list.get(i).getQuestion(), 
											 list.get(i).getAnswers(), 
											 list.get(i).getCorrect());
				for (int c = 0; c < times; c++) {
					list.add(a);
					totalAdded++;
				}
				Collections.shuffle(list);
				break;
			}
		}
	}
	
	public void removeQuestion(String question)
	{
		for (int i = 0; i < myQuestions.size(); i++) {
			if (myQuestions.get(i).getQuestion().contains(question.trim())) {
				//We found it, now remove it
				myQuestions.remove(i);
			}
		}
	}
	
	public String[] getFolders(String mainDir)
	{
		File folderName = new File(mainDir);
		File temp[] = folderName.listFiles();
		
		if (temp != null) {
			for (File i : temp) {
				if (i.isDirectory()) {
					getFolders(i.toString());
					//System.out.println("folder Found: " + i);
				}
				else {
					if ((!i.getName().startsWith(".")) && (!i.getName().contains("Study_Guide"))) {
						//System.out.println("File Found: " + i.getPath().toString());
						files.add(i.getPath().toString());
					}
				}
			}
		}
		//Convert Arraylist to array
		String[] filesFound = files.toArray(new String[files.size()]);
		
		//Sort array
		Arrays.sort(filesFound);
		
		return filesFound;
	}
	
	public void showMain(boolean a)
	{
		if (a) //True, showing main, hiding current
		{
			for (JPanel i : main) {
				i.setVisible(a);
			}
			setVisible(false);
		}
		else 
		{
			for (JPanel i : main) {
				i.setVisible(false);
				remove(i);
			}
			setVisible(a);
		}
	}
	public void finish()
	{
		//Hide all current views
		
		//Show final score:
		//Calculate final score:
		double finalScore = (double)answeredCorrect/(double)attempted;
		
		//Format score
		DecimalFormat df = new DecimalFormat("#,###,##0.##");
		String s = (df.format(finalScore*100)) + " %";
		
		//Now that we have the score, we are going to update the list of scores we have for that test. Note that this will be a contant list update because we are just saving the scores here versus the entire test like previously.
		String fileName = mySemester + "/" + myClass + "/" + myTest + "/Study_Guide_Scores.txt";
		if (attempted > 0) {
			try {
				PrintWriter out = new PrintWriter(new FileOutputStream("Files/" + fileName, true));
				out.println(score + " (" + answeredCorrect + " / " + attempted + ") " + s);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		//We are going to create a finish screen. First, hide everything on the panel
		int areaCounter = 0;
		for (JTextArea i : questionArea) {
			if (questionsShown < myQuestions.size()) {
				i.setText("\n     " + myQuestions.get(questionsShown).getQuestion());
				
				i.setCaretPosition(0);
				
				//Now, add in the answers
				//Retrieve answers first
				String temp[] = myQuestions.get(questionsShown).getAnswers();
				for (int c = 0; c < temp.length; c++) {
					answers[areaCounter].addItem(temp[c]);
				}
				checked[areaCounter] = false;
				answers[areaCounter].setEnabled(false);
				questionScroll[areaCounter].setVisible(false);
				questionTitle[areaCounter].setVisible(false);
				answers[areaCounter].setVisible(false);
				answers[areaCounter].setBorder(null);
				questionsShown++;	
			}
			else {
				questionScroll[areaCounter].setVisible(false);
				questionTitle[areaCounter].setVisible(false);
				answers[areaCounter].setVisible(false);
			}
			areaCounter++;
		}
		nextBtn.setVisible(false);
		
		//Add a text view and a scroller
		missedQuestionsArea = new JTextArea();
		missedQuestionsArea.setEditable(false);
		missedQuestionsArea.setBounds(5, scoreLbl.getY() + scoreLbl.getHeight() + (scoreLbl.getHeight()/2),
											width - 10, 
											height - scoreLbl.getY() - scoreLbl.getHeight() - timeLbl.getHeight() - (height - timeLbl.getY()));
		missedQuestionsArea.setEditable(false);
		missedQuestionsArea.setWrapStyleWord(true);
		missedQuestionsArea.setLineWrap(true);
		missedQuestionsArea.setBorder(null);
		missedQuestionsScroller = new JScrollPane (missedQuestionsArea, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(missedQuestionsScroller);
		missedQuestionsScroller.setBounds(missedQuestionsArea.getBounds());
		missedQuestionsScroller.setWheelScrollingEnabled(true);
		missedQuestionsArea.setFont(new Font("Serif", Font.PLAIN, 16));
		missedQuestionsArea.setText("\nMissed: \n\n" + master);
		missedQuestionsArea.setCaretPosition(0);
		//missedQuestionsScroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		missedQuestionsScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		
		//After the file was updated, we aer going to show the user what questions they missed and how many times it was missed. We are going to do this by checking the missedQuestions arraylist and determining the count as well as the quantity of each question (using a method).
		
		
		JOptionPane.showMessageDialog(new JFrame("Final Score"), "You scored: " + s + "\n(" + answeredCorrect + " / " + attempted + ")");
		//showMain(true);
	}
	
	public int getMissedCount(String question)
	{
		int count = 0;
		
		for (Question q : missedQuestions) {
			if (q.getQuestion().toString().equals(question)) {
				count++;
			}
		}
		
		return count;
	}
	
	public class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//System.out.println("Seconds: " + seconds);
			//Update the second count
			seconds--;
			
			//Update the label
			if (seconds >= 0) {
				timeLbl.setText(seconds + "");
			}
			else {
				loner.stop();
				nextBtn.setEnabled(false);
				
				//Disable the comboboxes
				for (JComboBox i : answers) {
					i.setEnabled(false);
				}
				
				//Show finish Screen
				finish();
			}
		}
	}
	
}