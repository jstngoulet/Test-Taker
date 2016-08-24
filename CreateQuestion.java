import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class CreateQuestion extends JPanel
{
	//Labels
	JLabel questionTitle = new JLabel ("New Question");
	JLabel borders[] = new JLabel[5];
	JLabel quesType, questionPanel;
	
	//For Mult Choice
	JLabel question, answers, answer;
	JTextArea questionInput;
	JTextArea answersInput[] = new JTextArea[5];
	JScrollPane scroll;
	JScrollPane answersScroll[] = new JScrollPane[5];
	JRadioButton answerSelect[] = new JRadioButton[5];
	ButtonGroup gp;
	
	//Panels
	
	//Combo Box
	JComboBox quesTypeBox;

	
	//Ints
	int WIDTH, HEIGHT;
	
	public CreateQuestion(int width, int height)
	{
		WIDTH = width - 20;
		HEIGHT = height - 20;
		setLayout(null);
		setBounds(10, 10, width - 20, height - 20);
		setBackground(Color.WHITE);
		buildLayout();
		setVisible(true);
	}
	
	public void buildLayout()
	{
		//Add Title
		questionTitle.setBounds(10, 5, WIDTH, 15);
		questionTitle.setFont(new Font("Arial Black", Font.PLAIN, 18));
		add(questionTitle);
		
		//Add Borders
		for (int i = 0; i < 4; i++) 
		{
			borders[i] = new JLabel();
			borders[i].setBackground(Color.BLACK);
			borders[i].setOpaque(true);
			
			switch (i) 
			{
				case 0:
				//Top
				borders[i].setBounds(160, questionTitle.getY() + (questionTitle.getHeight()/2), WIDTH - questionTitle.getX() - 260, 1);
					break;
					
				case 1:
				//Left
				borders[i].setBounds(questionTitle.getX(), questionTitle.getY() + questionTitle.getHeight() + 5, 1, HEIGHT - questionTitle.getHeight() - 45);
					break;
					
				case 2: 
				//Bottom
				borders[i].setBounds(questionTitle.getX() + 100, borders[1].getY() + borders[1].getHeight() + 20, WIDTH - borders[1].getX() - 110, 1);
					break;
					
				case 3:
				//Right
				borders[i].setBounds(WIDTH - borders[1].getX(), borders[1].getY() + 15, 1, borders[1].getHeight() + 6);
					break;
					
				default:
					break;
			}
			add(borders[i]);
		}
		
		
		//Now add the JLabel and JCombo box to inform and allow the user to select question type. For now, it is just Multiple choice
		quesType = new JLabel("Question Type: ");
		quesType.setBounds(questionTitle.getX() + 25, questionTitle.getY() + questionTitle.getHeight() + 10, WIDTH, 15);
		add(quesType);
		
		String[] options = {"----", "Definition", "Translation", "Conjugate Verb", "Multiple Choice", "True or False"};
		quesTypeBox = new JComboBox(options);
		quesTypeBox.setBounds(quesType.getX() + 105, quesType.getY() - 5, 150, 25);
		quesTypeBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//Get selected item then create the appropriate panel
				String resp = quesTypeBox.getSelectedItem().toString();
				if (resp.equals("Multiple Choice")) {
					createMultChoice();
				}
				else if (resp.equals("True or False")) {
					createTrueFalse();
				}
				else if (resp.equals("Definition")) {
					createMultChoice();
					
					//Now that the form is added, now fill in some text
					questionInput.setText("     Define - ");
				}
				else if (resp.equals("Translation")) {
					createMultChoice();
					
					//Now that the form is added, now fill in some text
					questionInput.setText("     Translate - ");
				}
				else if (resp.equals("Conjugate Verb")) {
					createMultChoice();
					questionInput.setText("     Conjugate - ");
				}
				else 
				{
					//Hide panel
					questionPanel.setVisible(false);
				}
			}
		});
		add(quesTypeBox);
		addQuestionPanel();
	}
	
	public void createMultChoice()
	{
		questionPanel.setVisible(false);
		remove(questionPanel);
		addQuestionPanel();
		//System.out.println("Creating Mult Choice");
		questionPanel.setVisible(true);
		
		//Add Question Label (Next to text Area)
		question = new JLabel("Question: ");
		question.setBounds(0, 0, questionPanel.getWidth()/2, 20);
		questionPanel.add(question);
		
		//Add Text Area
		questionInput = new JTextArea(5, 0);
		questionInput.setBounds(question.getX() + question.getWidth()/2 - 25, 0, questionPanel.getWidth() - question.getWidth()/2 + 20, question.getHeight() *5);
		//questionInput.setBackground(Color.LIGHT_GRAY);
		questionInput.setWrapStyleWord(true);
		questionInput.setLineWrap(true);
		scroll = new JScrollPane (questionInput, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		questionPanel.add(scroll);
		scroll.setBounds(questionInput.getBounds());
		scroll.setWheelScrollingEnabled(true);
		//scroll.setBackground(Color.LIGHT_GRAY);
		
		//Add Answer fields
		int offset = 0;
		
		//Create a group
		gp = new ButtonGroup();
		
		for (int i = 0; i < 4; i++) {
			answersInput[i] = new JTextArea();
			answersInput[i].setBounds(scroll.getX(), questionInput.getY() + questionInput.getHeight() + 8 + offset, questionInput.getWidth(), 38);
			//answersInput[i].setBackground(Color.ORANGE);
			offset += 40;
			answersInput[i].setWrapStyleWord(true);
			answersInput[i].setLineWrap(true);
			answersScroll[i] = new JScrollPane (answersInput[i], 
					   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			answersScroll[i].setBounds(answersInput[i].getBounds());
			answersScroll[i].setWheelScrollingEnabled(true);
			questionPanel.add(answersScroll[i]);
			
			//Now add JRadioButtons
			answerSelect[i] = new JRadioButton();
			answerSelect[i].setBounds(answersScroll[i].getX() - 25, answersScroll[i].getY(), answersScroll[i].getWidth(), answersScroll[i].getHeight());
			questionPanel.add(answerSelect[i]);
			gp.add(answerSelect[i]);

		}
		
		//Add Answer Label
		answer = new JLabel("Answers: ");
		answer.setBounds(0, answersScroll[0].getY(), questionPanel.getWidth()/2, 20);
		questionPanel.add(answer);

		
		showMultChoice();
	}
	
	public void createTrueFalse()
	{
		questionPanel.setVisible(false);
		remove(questionPanel);
		addQuestionPanel();
		questionPanel.setVisible(true);
		
		//Add Question Label (Next to text Area)
		question = new JLabel("Question: ");
		question.setBounds(0, 0, questionPanel.getWidth()/2, 20);
		questionPanel.add(question);
		
		//Add Text Area
		questionInput = new JTextArea(5, 0);
		questionInput.setBounds(question.getX() + question.getWidth()/2 - 25, 0, questionPanel.getWidth() - question.getWidth()/2 + 20, question.getHeight() *5);
		//questionInput.setBackground(Color.LIGHT_GRAY);
		questionInput.setWrapStyleWord(true);
		questionInput.setLineWrap(true);
		scroll = new JScrollPane (questionInput, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		questionPanel.add(scroll);
		scroll.setBounds(questionInput.getBounds());
		scroll.setWheelScrollingEnabled(true);
		
		
		//Add Answer fields
		int offset = 0;
		
		//Create a group
		gp = new ButtonGroup();
		
		for (int i = 0; i < 2; i++) {
			answersInput[i] = new JTextArea();
			answersInput[i].setBounds(scroll.getX(), questionInput.getY() + questionInput.getHeight() + 8 + offset, questionInput.getWidth(), 38);
			//answersInput[i].setBackground(Color.ORANGE);
			answersInput[i].setEditable(false);
			offset += 40;
			answersInput[i].setWrapStyleWord(true);
			answersInput[i].setLineWrap(true);
			answersScroll[i] = new JScrollPane (answersInput[i], 
					   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			answersScroll[i].setBounds(answersInput[i].getBounds());
			answersScroll[i].setWheelScrollingEnabled(true);
			questionPanel.add(answersScroll[i]);
			switch (i) {
				case 0:
					answersInput[i].setText("True");
					break;
					
				case 1:
					answersInput[i].setText("False");
					break;
				
				default:
					break;
			}
			
			//Now add JRadioButtons
			answerSelect[i] = new JRadioButton();
			answerSelect[i].setBounds(answersScroll[i].getX() - 25, answersScroll[i].getY(), answersScroll[i].getWidth(), answersScroll[i].getHeight());
			questionPanel.add(answerSelect[i]);
			gp.add(answerSelect[i]);

		}
		
		//Add Answer Label
		answer = new JLabel("Answers: ");
		answer.setBounds(0, answersScroll[0].getY(), questionPanel.getWidth()/2, 20);
		questionPanel.add(answer);
		
	}
	
	public void addQuestionPanel()
	{
		questionPanel = new JLabel();
		questionPanel.setBounds(quesType.getX(), quesType.getY() + quesType.getHeight() + 25, WIDTH - 70, HEIGHT - 100);
		questionPanel.setBackground(Color.YELLOW);
		//questionPanel.setOpaque(true);
		add(questionPanel);
		questionPanel.setVisible(false);
		
		//Add Save button to the panel from ClassOverView
		
	}
	
	public void showMultChoice()
	{
		question.setVisible(true);
		scroll.setVisible(true);
		questionInput.setVisible(true);
		for (int i = 0; i < answersInput.length; i++) {
			
		}
		
	}
	
	public int getPanelHeight()
	{
		return getHeight();
	}
	
	//To return Question Type
	public String getType()
	{
		if (quesTypeBox.getSelectedIndex() != 0) {
			return quesTypeBox.getSelectedItem().toString();
		}
		else {
			return "blank";
		}
	}
	
	//To return Question
	public String getQuestion()
	{
		if (!getType().equals("blank")) {
			//System.out.println("Getting Question");
			if ((!questionInput.getText().trim().equals(null) || (!questionInput.getText().trim().equals("")))) {
				return questionInput.getText() + "\n";
			}
			else {
				return "blank";
			}
		}
		else {
			return "blank";
		}
	}
	
	//To return Answers
	public String[] getAnswers(int pos)
	{
		boolean full = false;
		
		for (int i = 0; i < pos; i++) 
		{
			if (answersInput[i] == null) 
			{
				full = false;
				break;
			}
			else {
				full = true;
			}
		}
		if (full == true) 
		{
			
			if (pos == 2) {
				String ans[] = {answersInput[0].getText(), answersInput[1].getText()};
				//Now, check to see if a new line character was found
				for (int i = 0; i < pos; i++) {
					answersInput[i].setText(answersInput[i].getText().replace("\n", " || "));
					System.out.println("Answers: " + answersInput[i]);
				}
				return ans;
			}
			else if (pos == 4) {
				String ans[] = {
				answersInput[0].getText().substring(0, 1).toUpperCase() + answersInput[0].getText().substring(1, answersInput[0].getText().length()), 
				answersInput[1].getText().substring(0, 1).toUpperCase() + answersInput[1].getText().substring(1, answersInput[1].getText().length()), 
				answersInput[2].getText().substring(0, 1).toUpperCase() + answersInput[2].getText().substring(1, answersInput[2].getText().length()), 
				answersInput[3].getText().substring(0, 1).toUpperCase() + answersInput[3].getText().substring(1, answersInput[3].getText().length())};
				
				//Now, check to see if a new line character was found
				for (int i = 0; i < pos; i++) {
					answersInput[i].setText(answersInput[i].getText().replace("\n", " || "));
					System.out.println("Answers: " + answersInput[i].getText());
				}
				
				return ans;
			}
			
			return null;
		}
		else {
			String ans[] = {"blank", "blank"};
			return ans;
		}
	}
	
	public String getCorrectAnswer(int amount)
	{
		int selected = 0;
		boolean flg = false;
		if ((!getType().equals("blank") && (answerSelect != null))) 
		{	
			for (int i = 0; i < amount; i++) 
			{
				if (answerSelect[i].isSelected()) 
				{
					flg = true;
					break;
				}	
				else{
					flg = false;
				}
				selected = i+1;
			}
			if (flg) {
				return answersInput[selected].getText().substring(0, 1).toString().toUpperCase() + answersInput[selected].getText().substring(1, answersInput[selected].getText().length()).toString();
			}
			else {
				return null;
			}
		}
		
		else {
			return null;
		}
	}
	
	
}