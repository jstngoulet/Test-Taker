import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/*
	This class is actually going to be a really helpful tool for studying.
		There are a few things that we must accomplish in this class
			• The User must be shown a starting question in frame. This question is chosen at random
			• The corresponding answer will be on the right hand side of the frame, but hidden. There will 
				be a button to show it.
			• The user must be given the chance to test themselves to see if they know the correct answer. This is 
				based on the honor system.
			• After the user clicked the "Show Answer" btn, they will be asked to see if they got it correct or not.
				• If they got it correct, it will be removed from the list of questions and the list will continue.
				• If they got the answer incorrect on their guess, the answer will be added to the end of the question list once 
					more.
			• Once the user goes through the entire list, they will be able to see which ones they missed the most and which ones
				they knew the best
			
		• If the user quits at any time, their progress is not saved at all, and the file that has been created will be deleted.
			This file is only a temp file that lasts only as long as the user stays on this screen.
*/


public class FlashCards extends JPanel
{
	public static int width, height;	
	String mySemester, myClass, myTest;
	ArrayList <Question> myQuestions = new ArrayList <Question>();
	JPanel panels[] = new JPanel[2];
	int[] panelWidth = new int[2];
	int[] panelHeight = new int[2];
	int currentQuestion = 0;
	
	//JTextAReas
	JTextArea curQuestion, curAnswer, questionsMissed;
	
	//ScrollPane
	JScrollPane scroll, answerScroll, missedScroll;
	
	//Buttons
	JButton showAnswer, close;
	
	//Panels
	JPanel results = new JPanel();
	JPanel myPanel[];
	
	public FlashCards(int w, int h, String curSemester, String curClass, String curTest, JPanel[] mainPanels)
	{
		width = w;
		height = h;
		mySemester = curSemester;
		myClass = curClass;
		myTest = curTest;
		myPanel = mainPanels; 
		setBounds(5, 5, w, h);
		setLayout(null);
		setBackground(Color.LIGHT_GRAY);
		getQuestions();
		buildLayout();
		setVisible(true);
		//System.out.println("Showing Flash Cards");
	}
	
	public void buildLayout()
	{
		//Split into two different views
		for (int i = 0; i < 2; i++) {
			panels[i] = new JPanel();
			panels[i].setLayout(null);
			panels[i].setBackground(Color.WHITE);
			switch (i) {
				case 0:
					panels[i].setBounds(0, 0, width/2 - 17, height);
					panelHeight[i] = panels[i].getHeight();
					panelWidth[i] = panels[i].getWidth();
					break;
					
				case 1:
					panels[i].setBounds(panels[i-1].getWidth() + 2, 0, width/2 - 25, height);
					panelHeight[i] = panels[i].getHeight();
					panelWidth[i] = panels[i].getWidth();
					break;
				default:
					break;
			}
			add(panels[i]);
			panels[i].setVisible(true);
		}
		
		//Add Text Area
		curQuestion = new JTextArea();
		curQuestion.setBounds(5, 0, panelWidth[0] -10, panelHeight[0]/2);
		curQuestion.setEditable(false);
		curQuestion.setWrapStyleWord(true);
		curQuestion.setLineWrap(true);
		scroll = new JScrollPane (curQuestion, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panels[0].add(scroll);
		scroll.setBounds(curQuestion.getBounds());
		scroll.setWheelScrollingEnabled(true);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		addMainClose();
		
		//Add Answer Text Area
		curAnswer = new JTextArea();
		curAnswer.setBounds(5, 0, panelWidth[1] - 10, panelHeight[1]/2);
		curAnswer.setEditable(false);
		curAnswer.setWrapStyleWord(true);
		curAnswer.setLineWrap(true);
		answerScroll = new JScrollPane (curAnswer, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panels[1].add(answerScroll);
		answerScroll.setBounds(curAnswer.getBounds());
		answerScroll.setWheelScrollingEnabled(true);
		answerScroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		answerScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Set first question and answer (answer text is just white until btn is pushed)
		//Check to see if question is True or False. If it is, Write T/F so the user knows how to answer
		String q = "\n     Question " + (currentQuestion + 1) + " of " + myQuestions.size() + ": \n\n     ";
		String a = "\n     Answer: \n\n     ";
		curAnswer.setText(a); //This should only display 'a' right now
		
		if (myQuestions.get(0).getType().trim().contains("True or False")) {
			curQuestion.setText(q + "T/F " + myQuestions.get(0).getQuestion().trim());
		}
		else {
			curQuestion.setText(q + myQuestions.get(0).getQuestion().trim());
		}
		
		
		showAnswer = new JButton("Reveal");
		showAnswer.setBounds(panelWidth[1] - 155, panelHeight[1] - 100, 150, 25);
		showAnswer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Show the correct answer
				curAnswer.setText(a + myQuestions.get(currentQuestion).getCorrect().trim());
				
				//Ask if they knew it or not
				int reply = JOptionPane.showConfirmDialog(null, "Did you know that the correct answer was: \n" + myQuestions.get(currentQuestion).getCorrect().trim(), "Did You Get It?", JOptionPane.YES_NO_OPTION);
									
				if ((reply == JOptionPane.YES_OPTION) && (currentQuestion < myQuestions.size() - 1)) {
					//If yes, move on.
					currentQuestion++;
					
					//Show next Question
					//Check to see if question is True or False. If it is, Write T/F so the user knows how to answer
					String q = "\n     Question " + (currentQuestion + 1) + " of " + myQuestions.size() + ": \n\n     ";
					String a = "\n     Answer: \n\n     ";
					curAnswer.setText(a); //This should only display 'a' right now
					
					if (myQuestions.get(currentQuestion).getType().trim().contains("True or False")) {
						curQuestion.setText(q + "T/F " + myQuestions.get(currentQuestion).getQuestion().trim());
					}
					else {
						curQuestion.setText(q + myQuestions.get(currentQuestion).getQuestion().trim());
					}
				}
				else if (reply == JOptionPane.NO_OPTION)
				{
					//If not, add the question to the end of the list
					Question tempQuestion = new Question(myQuestions.get(currentQuestion).getType(), myQuestions.get(currentQuestion).getQuestion(), myQuestions.get(currentQuestion).getAnswers(), myQuestions.get(currentQuestion).getCorrect());
					myQuestions.add(tempQuestion);
					
					//Shuffle
					Collections.shuffle(myQuestions);//Note that the user can get the same question again or never see a missed question again
					
					//Increase Question #
					currentQuestion++;
					
					//Then, show next question
					//Check to see if question is True or False. If it is, Write T/F so the user knows how to answer
					String q = "\n     Question " + (currentQuestion + 1) + " of " + myQuestions.size() + ": \n\n     ";
					String a = "\n     Answer: \n\n     ";
					curAnswer.setText(a); //This should only display 'a' right now
					
					if (myQuestions.get(currentQuestion).getType().trim().contains("True or False")) {
						curQuestion.setText(q + "T/F " + myQuestions.get(currentQuestion).getQuestion().trim());
					}
					else {
						curQuestion.setText(q + myQuestions.get(currentQuestion).getQuestion().trim());
					}
				}
				else {
					curQuestion.setBounds(curQuestion.getX(), curQuestion.getY(), width, 15);
					curQuestion.setText("No More Flash Cards! See missed Questions Below:");
					showAnswer.setVisible(false);
					remove(showAnswer);
					
					//Make screens smaller
					for (int i = 0; i < 2; i++) {
						panels[i].setBounds(panels[i].getX(), panels[i].getY(), panels[i].getWidth(), panels[i].getHeight() - 205);
					}
					
					//Set the first panel to be the same width as the botom
					panels[0].setBounds(panels[0].getX(), panels[0].getY(), width - 40, panels[0].getHeight());
					
					//Hide the answers panel
					panels[1].setVisible(false);
					
					//Add Finish Screen
					finish();
				}

			}
		});
		panels[1].add(showAnswer);
		
	}
	
	public void finish()
	{
		results.setBounds(panels[0].getX(), panels[0].getY() + panels[0].getHeight() + 5, width, height - panels[0].getHeight() - panels[0].getY() - 70);
		results.setBackground(Color.WHITE);
		add(results);
		
		//This is where we add the questions and the amount of times missed.
		//Create a method that searches through the array for the question and returns the count.
		String master = "";
		for (Question e : myQuestions) {
			if (!master.contains(e.getQuestion())) {
				int missed = getQuestionMissedCount(e.getQuestion());
				if (missed > 0)
				{
					master += "Question: " + e.getQuestion() + " (" + getQuestionMissedCount(e.getQuestion()) + ")\n\n     Answer: " + e.getCorrect() + "\n\n";
				}
			}
		}
		if (master.equals("")) {
			//If all are correct
			master += "All Responses were correct! consider taking a test to really test your skills, save your results, and compare different attempts.\n\nThen, when you are really ready, create a cumulative final exam by clicking the \"Create Final\" button on the main menu. \n\n\nGood Luck!";
		}
		//Change height of curQuestion
		curQuestion.setBounds(curQuestion.getX(), 15, curQuestion.getWidth(), 15);
		scroll.setBounds(curQuestion.getBounds());
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		
		//Add Answer Text Area
		questionsMissed = new JTextArea(master.trim());
		questionsMissed.setBounds(curQuestion.getX(), curQuestion.getY() + 25, width -20, panels[0].getHeight() - 40);
		questionsMissed.setBackground(Color.WHITE);
		questionsMissed.setEditable(false);
		questionsMissed.setWrapStyleWord(true);
		questionsMissed.setLineWrap(true);
		missedScroll = new JScrollPane (questionsMissed, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panels[0].add(missedScroll);
		missedScroll.setBounds(questionsMissed.getBounds());
		missedScroll.setWheelScrollingEnabled(true);
		missedScroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	}
	
	public int getQuestionMissedCount(String question)
	{
		//There is no arraylist passed in because it is a "Class" Variable.
		//Instead, we are passing in the question
		int counter = 1;
		
		for (Question a : myQuestions) {
			if (a.getQuestion().contains(question)) {
				//Return an increase
				//System.out.println("Question " + question + " found!");
				counter++;
			}
		}
		return counter - 2; //Subtract 2 because by default, the question is already in there once
	}
	
	public void addCloseBtn(JPanel[] main)
	{
		//Set null layout to results panel
		results.setLayout(null);
		
		close = new JButton("Menu");
		
		//Set location
		close.setBounds(getWidth() - 145, 100, 100, 25);
		
		//Set action
		close.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Hide self
				setVisible(false);
				
				//Show panels passed in
				for (int i = 0; i < main.length; i++) {
					main[i].setVisible(true);
				}
			}
		});
		
		//add to third panel (results)
		results.add(close);
	}
	
	//add method to add a close btn to the main screen
	public void addMainClose()
	{
		close = new JButton("Menu");
		
		//Set location
		close.setBounds(5, panelHeight[0] - 100, 150, 25);
		
		//Set action
		close.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Hide self
				setVisible(false);
				
				//Show panels passed in
				for (int i = 0; i < myPanel.length; i++) {
					myPanel[i].setVisible(true);
				}
			}
		});
		
		//add to third panel (results)
		panels[0].add(close);
	}
	
	public void getQuestions()
	{
		//Create a filename
		String fileName = "Files/" + mySemester + "/" + myClass + "/" + myTest;
		
		GetStats gettingQuestions = new GetStats(myClass, myTest);
		String folders[] = gettingQuestions.getFolders(fileName);
		
		for (String i : folders) 
		{
			//System.out.println("Folders: " + i);
			TestQuestion testing = new TestQuestion(fileName + "/" + i);
			Question tempQ = new Question(testing.getType(), testing.getQuestion(), testing.getAnswers(), testing.getCorrect());
			myQuestions.add(tempQ);
		}
		
		//Shuffle questions
		Collections.shuffle(myQuestions);
	}
}