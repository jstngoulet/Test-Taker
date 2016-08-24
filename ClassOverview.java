import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;



public class ClassOverview extends JPanel 
{
	//Init JPanels
	JPanel panels[] = new JPanel[3];
	JPanel test, game;
	
	//Init Labels
	JLabel classLabel, statsLabel, semesterLabel, testLabel, testDesLabel;
	JLabel statsBorder[] = new JLabel[5];
	JLabel testInfo[] = new JLabel[15];
	
	//Init JCombo Boxes
	JComboBox semesterBox, classBox, testBox;
	
	//Init Btns
	JButton addSemester, removeSemester, addClass, removeClass, addTest, removeTest, saveQuestion;
	JButton questionFun[] = new JButton[9];
	
	//Init vars
	String[] semesterList;
	String semesterInputByUser, selectedSemester, classInputByUser, selectedClass, testInputByUser, selectedTest, directory, curDirect, masterStr;
	
	File[] filesList;
	ArrayList <String> files = new ArrayList();
	
	//Arraylist of Type TestQuestion
	ArrayList <Question> content = new ArrayList <Question>();
	
	//JTextAreas
	JTextArea gradeOutput;
	
	//Scroll
	JScrollPane gradesScroll;
	
	int WID, HEI, num = 1;
	
	public ClassOverview(int width, int height)
	{
		setSize(width, height - 65);
		setBackground(Color.LIGHT_GRAY);
		setLayout(null);
		buildComp(width, height);
		WID = width;
		HEI = height;
		setBounds(20, 20, width - 40, getHeight());
		setVisible(true);
		
		//Just for testing purposes
		//testing();
	}
	
	public void testing()
	{
		semesterBox.setSelectedIndex(1);
		classBox.setSelectedIndex(4);
		testBox.setSelectedIndex(1);
	}
	
	public void buildComp(int width, int height)
	{
		//Build 3 Panels
		//	Two on top, one on bottom.
		//	The top ones will show hte classes on the left and the available tests on the right.
		//	The bottom panel will alow the user to edit the existing tests, add, or remove a test.
		
		//Add generic details to each of the panels
		//System.out.println("Width: " + width + "\tHeight: " + height);
		for (int i = 0; i < 3; i++) 
		{
			panels[i] = new JPanel();
			panels[i].setLayout(null);
			
			if (i < 2) {
				panels[i].setSize((int)(width/2 - 20), (int)(height/3 *2) - 20);
			}
			else {
				panels[i].setSize(panels[i-1].getWidth() + panels[i-2].getWidth() + 5, height - panels[i-1].getHeight() - panels[i-1].getY());
			}
			panels[i].setBackground(Color.WHITE);
			
			switch (i) {
				case 0:
					panels[i].setBounds(5, 5, panels[i].getWidth(), panels[i].getHeight());
					//panels[i].setBackground(Color.GREEN);
					addPanel1(panels[i].getWidth(), panels[i].getHeight());
					break;
					
				case 1:
					panels[i].setBounds(10 + panels[i-1].getWidth(), 5, panels[i].getWidth() - 15, panels[i].getHeight());
					//panels[i].setBackground(Color.BLUE);
					addPanel2(panels[i].getWidth(), panels[i].getHeight());
					break;
					
				case 2:
					panels[i].setBounds(5, panels[0].getHeight() + panels[0].getY() + 5, panels[i].getWidth(), panels[i].getHeight()-75);
					//panels[i].setBackground(Color.RED);
					addPanel3(panels[i].getWidth(), panels[i].getHeight());
					break;
				
				default:
					break;
			}
			
			add(panels[i]);
			
		}
	}
	
	public void addPanel1(int width, int height)
	{
		//Add a label
		semesterLabel = addJLabel(panels[0], "Select Semester:", 10, 10, width, 10);
		
		//Add a jCombo
		addSemesterCombo(width);
		
		semester(width);
		String[] opti = {"Please Select Class"};
		
		classLabel = addJLabel(panels[0], "Select Class:", 10, 100, width, 10);
		classBox = new JComboBox(opti);
		classBox.setBounds(5, 120, width - 10, 25);
		classBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				testBox.removeAllItems();
				testBox.addItem("Please Select Test");
				
				for (String i : getFolders("Files/" + semesterBox.getSelectedItem() + "/" + classBox.getSelectedItem())) {
					testBox.addItem(i);
				}
				//selectedClass = classBox.getSelectedItem().toString();
				if ((!semesterBox.getSelectedItem().equals("Please Select Semester"))) {
					showTest(true);
				}
				else {
					showTest(false);
					showClass(false);
				}
			}
		});
		panels[0].add(classBox);
		
		addClass = new JButton("Add Class");
		addClass.setBounds(addSemester.getX(), 150, addSemester.getWidth(), addSemester.getHeight());
		addClass.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				JFrame dia = new JFrame();
				dia.setSize(400, 200);
				
				//This will create a JDiolog box that will allow the person to input the name of their semester
				if ((testDesLabel.getText().contains("Please Select Semester"))) {
					JOptionPane.showMessageDialog(new JFrame(), "Please Select Semester");
				}
				else {
					classInputByUser = JOptionPane.showInputDialog("Please name your class: ");
				}
				
				//Once done, append Semester File
				if (classInputByUser != null) 
				{
					
							File f = new File("Files/" + semesterBox.getSelectedItem() + "/" + classInputByUser);
							try{
							    if(f.mkdir()) 
								{ 
							        //System.out.println("Directory <" + classInputByUser + "> Created");
							
										//After Created, add to list
										classBox.removeAllItems();
										classBox.addItem("Please Select Class");
										
										for (String i : getFolders("Files/" + semesterBox.getSelectedItem())) 
										{
											classBox.addItem(i);
										}
										classBox.setSelectedItem(classInputByUser);
								} 
							else {
							        //JOptionPane.showMessageDialog(new JFrame(), "Directory is not created");
							    }
							} catch(Exception z){
							    z.printStackTrace();
							}
				}

			}
		});
		panels[0].add(addClass);
		
		removeClass = new JButton("Remove Class");
		removeClass.setBounds(removeSemester.getX(), 150, removeSemester.getWidth(), removeSemester.getHeight());
		removeClass.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				File directory = new File("Files/" + semesterBox.getSelectedItem() + "/" + classBox.getSelectedItem());
				if (directory.exists()) {
					try
					{
						//System.out.println("Deleting Directory: " + directory);
	               	delete(directory);
						//Now, delete the item from the list
						classBox.removeItem(classBox.getSelectedItem());
						//hideTest();
	           	}
					catch(IOException o)
					{
	               	o.printStackTrace();
	           	}
				}
			}
		});
		panels[0].add(removeClass);
		
		
		//Add test combo and info
		testLabel = addJLabel(panels[0], "Select Test:", 10, 190, width, 10);
		testBox = new JComboBox();
		testBox.setBounds(classBox.getX(), 210, classBox.getWidth(), classBox.getHeight());
		testBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updatePanel3();
			}
		});
		panels[0].add(testBox);
		
		addTest = new JButton("Add Test");
		addTest.setBounds(addSemester.getX(), 240, addSemester.getWidth(), addSemester.getHeight());
		addTest.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						JFrame dia = new JFrame();
						dia.setSize(400, 200);
						
						//This will create a JDiolog box that will allow the person to input the name of their semester
						if ((testDesLabel.getText().contains("Please Select Semester"))) {
							JOptionPane.showMessageDialog(new JFrame(), "Please Select Semester");
						}
						else if ((testDesLabel.getText().contains("Please Select Class"))) {
							JOptionPane.showMessageDialog(new JFrame(), "Please Select Class");
						}
						else {
							testInputByUser = JOptionPane.showInputDialog("Please name your test: ");
						}
						
						
						//Once done, append Semester File
						if (testInputByUser != null) 
						{
							
									File f = new File("Files/" + semesterBox.getSelectedItem() + "/" + classBox.getSelectedItem() + "/" + testInputByUser);
									try{
									    if(f.mkdir()) 
										{ 
									        //System.out.println("Directory <" + testInputByUser + "> Created");
									
												//After Created, add to list
												testBox.removeAllItems();
												testBox.addItem("Please Select Test");
												
												for (String i : getFolders("Files/" + semesterBox.getSelectedItem() + "/" + classBox.getSelectedItem())) 
												{
												     testBox.addItem(i);
												}
												testBox.setSelectedItem(testInputByUser);
										} 
									else {
									        //JOptionPane.showMessageDialog(new JFrame(), "Directory is not created");
									    }
									} catch(Exception z){
									    z.printStackTrace();
									}
						}

					}
				});
		panels[0].add(addTest);
		
		removeTest = new JButton("Remove Test");
		removeTest.setBounds(removeClass.getX(), 240, removeClass.getWidth(), removeClass.getHeight());
		removeTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				File directory = new File("Files/" + semesterBox.getSelectedItem() + "/" + classBox.getSelectedItem() + "/" + testBox.getSelectedItem());
				if (directory.exists()) {
					try
					{
						System.out.println("Deleting Directory: " + directory);
	               	delete(directory);
						//Now, delete the item from the list
						testBox.removeItem(testBox.getSelectedItem());
	           	}
					catch(IOException o)
					{
	               	o.printStackTrace();
	           	}
				}
			}
		});
		
		panels[0].add(removeTest);
		
		showTest(false);
		showClass(false);
		
	}
	
	public void updatePanel3()
	{
		if (semesterBox.getSelectedItem().toString() != null) {
			testDesLabel.setText(semesterBox.getSelectedItem().toString());
		}
		if (classBox.getSelectedItem() != null) {
			testDesLabel.setText(semesterBox.getSelectedItem() + " ~ " + classBox.getSelectedItem());
			directory = semesterBox.getSelectedItem() + "/" + classBox.getSelectedItem();
		}
		if (testBox.getSelectedItem() != null) 
		{
			testDesLabel.setText(semesterBox.getSelectedItem() + " ~ " + classBox.getSelectedItem() + " ~ " + testBox.getSelectedItem());
			directory = semesterBox.getSelectedItem() + "/" + classBox.getSelectedItem() + "/" + testBox.getSelectedItem();
			if (testBox.getSelectedIndex() != 0) 
			{
				//Get stats
				GetStats s = new GetStats(classBox.getSelectedItem().toString(), testBox.getSelectedItem().toString());
				String q = "Questions:\t\t\t\t";
				String w = "Attempts:\t\t\t\t\t";
				String e = "Best Grade:    ";
				String r = "Avg. Grade:   ";
				
				
				//Update Text Fields (Questions, Attempts, best grade)
				testInfo[5].setText( s.getQuestionCount(semesterBox.getSelectedItem().toString(), classBox.getSelectedItem().toString(), testBox.getSelectedItem().toString()));
				testInfo[6].setText(s.getAttemptCount());
				testInfo[7].setText(s.getBestGrade());
				testInfo[8].setText(s.getAverageGrade());
				testInfo[4].setText(testBox.getSelectedItem().toString() + " Grades :" );
				
				String grades[] = s.getGrades();
				String master = "";
				for (int i = 0; i < grades.length; i++) {
					master += "  Attempt " + (i + 1) + ": \t     " + grades[i] + "\n";
				}
				gradeOutput.setText("  " + master.trim());
				gradeOutput.setCaretPosition(0);
			}
		}
		testDesLabel.setForeground(Color.BLACK);
		if (testDesLabel.getText().endsWith("Please Select Test")) {
			testDesLabel.setForeground(Color.RED);
			hidePanel3();
			hidePanel2();
			testDesLabel.setVisible(true);
	
		}
		else {
			testDesLabel.setForeground(Color.BLACK);
			showPanel3();
			showPanel2();
		}
	}
	
	
	public void showPanel2()
	{
		for (int i = 0; i < testInfo.length; i++) {
			testInfo[i].setVisible(true);
		}
	}
	
	public void hidePanel2()
	{
		for (int i = 0; i < testInfo.length; i++) {
			testInfo[i].setVisible(false);
		}
	}
	
	public void showPanel3()
	{
		testDesLabel.setVisible(true);
		for (int i = 0; i < 6; i++) {
			questionFun[i].setVisible(true);
		}
		gradesScroll.setVisible(true);
	}
	
	public void hidePanel3()
	{
		testDesLabel.setVisible(false);
		for (int i = 0; i < 6; i++) {
			questionFun[i].setVisible(false);
		}
		//Hide the stats
		gradesScroll.setVisible(false);
	}
	
	public void addPanel3(int width, int height)
	{
		//Add the title label
		testDesLabel = new JLabel();
		testDesLabel.setBounds(5, 5, width, 15);
		testDesLabel.setForeground(null);
		panels[2].add(testDesLabel);
		
		//Add the stats about the test: (Header Labels directly followed by the stat labels(Same y)
		
		/*for (int i = 0; i < questionFun.length; i++) {
			questionFun[i] = new JButton();
		} */
		
		questionFun[0] = new JButton("Add Question");
		questionFun[0].setBounds(width/4 - 85, height/5 + 5, width/4, 25);
		questionFun[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//Hide all panels
				//setVisible(false);
				for (int i = 0; i < 3; i++) {
					panels[i].setVisible(false);
				}
				
				//Create a new panel of "Create Question"
				CreateQuestion create = new CreateQuestion(getWidth(), getHeight());
				//Add a button to clase the panel
				JButton close = new JButton("Cancel");
				close.setBounds(create.getWidth() - 105, 5, 100, 25);
				create.add(close);
				close.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e)
					{
						//Hide the panel
						create.setVisible(false);
						
						//Remove the panel
						remove(create);
						
						//Show original screen
						for (int i = 0; i < 3; i++) {
							panels[i].setVisible(true);
						}
					}
				});
				
				//Add A Save Button for questions
				saveQuestion = new JButton("Save");
				saveQuestion.setBounds(5, create.getPanelHeight() - 30, 100, 25);
				saveQuestion.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						//Get information from questionPanel from Class (CreateQuestion):create
						String type = create.getType();
						String ques = create.getQuestion();
						String correctAnswer;
						int ansPos = 0;
						boolean tm = false;
						if (type.equals("Multiple Choice")) {
							ansPos = 4;
							tm = true;
						}
						else if (type.equals("Translation")) {
							ansPos = 4;
							tm = true;
						}
						else if (type.equals("Definition")) {
							ansPos = 4;
							tm = true;
						}
						else if (type.equals("Conjugate Verb")) {
							ansPos = 4;
							tm = true;
						}
						else if (type.equals("True or False")) {
							ansPos = 2;
							tm = true;
						}
						
						String ans[] = create.getAnswers(ansPos);
						
						if (tm) {
							correctAnswer = create.getCorrectAnswer(ansPos);
						}
						else {
							correctAnswer = create.getCorrectAnswer(0);
						}
						
						boolean full = false;
						
						for (int i = 0; i < ans.length; i++) {
							if ((ans[i].equals("blank")) || (ans[i].equals(null)) || (ans[i].equals(" ")) || (ans[i].equals(""))){
								full = false;
								break;
							}
							else {
								//System.out.println("Ans: " + ans[i]);
								full = true;
							}
						}
						if (type.equals("Multiple Choice") || (type.equals("True or False")) || (type.equals("Definition")) || (type.equals("Translation")) || (type.equals("Conjugate Verb"))) 
						{
							if ((type != null) && (ques != null) && (full) && (correctAnswer != null)) {
								//If there is information in each one
								//System.out.println("Saving Information");
								
								//Give console info
								//System.out.println("Question: \t" + ques);
								//System.out.println("Type: \t" + type);
								for (String i : ans) {
									//System.out.println("Answer: " + i);
								}
								//System.out.println("Correct Answer: " + correctAnswer);
								
								//Save information
								//System.out.println("Directory: " + directory);
								Questions quest = new Questions(directory, type, ques.trim(), ans, correctAnswer);
								
								//Below highlighted is irrelevant
								/*
								//Now, Create a file for Terms and Definitions (Hidden)
								if (type.equals("Definition")) {
									
									createDirectory("Files/" + semesterBox.getSelectedItem().toString() + "/" + 
									classBox.getSelectedItem().toString(), type);
									
									for (String i : ans) {
										System.out.println("Editing");
										AppendFile append = new AppendFile(semesterBox.getSelectedItem().toString() + "/" +
										classBox.getSelectedItem().toString() + "/" + type, i);
									}
								}
								else if (type.equals("Translation")) 
								{
									createDirectory("Files/" + semesterBox.getSelectedItem().toString() + "/" + 
									classBox.getSelectedItem().toString(), type);
									
									for (String i : ans) 
									{		
										System.out.println("editing");											AppendFile append = new AppendFile(semesterBox.getSelectedItem().toString() + "/" + 
										classBox.getSelectedItem().toString() + "/" + type, i);
									}
								}
								*/
								
								//Hide the panel
								create.setVisible(false);
								
								//Remove the panel
								remove(create);
								
								//Show original screen
								for (int i = 0; i < 3; i++) {
									panels[i].setVisible(true);
								}
									updatePanel3();
							}
							else {
								//Break it down 
								if (type == null) {
									JOptionPane.showMessageDialog(create, "Please Select Type!");
								}
								else if ((ques.trim() == null) || (ques.trim().equals(""))) {
									JOptionPane.showMessageDialog(create, "Please Add a Question!");
								}
								else if (!full) {
									JOptionPane.showMessageDialog(create, "Please Fill All Answer Fields!");
								}
								else if (correctAnswer == null) {
									JOptionPane.showMessageDialog(create, "Please Select the Correct Answer!");
								}
								else {
									JOptionPane.showMessageDialog(create, "Please fill all fields!");
								}
							}
						}
						/*else if (type.equals("True or False")) {
							System.out.println("Creating True or False");
						}*/
						else {
							JOptionPane.showMessageDialog(create, "Please Select Question Type or Click Cancel!");
						}
					}
				});
				create.add(saveQuestion);
				
				add(create);
			}
			
		});
		
		questionFun[1] = new JButton("Create Final");
		questionFun[1].setBounds(questionFun[0].getX(), testInfo[0].getY() + 22, width/4, 25);
		questionFun[1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Check to see if test box has "Final Exam" in it.
				boolean testCreated = false, reCreate = false;
				
				for (int i = 0; i < testBox.getItemCount(); i++) {
					if (testBox.getItemAt(i).toString().equalsIgnoreCase("Final Exam")) 
					{
						testCreated = true;
						break;
					}
				}
				if (testCreated) {
					//Show user that there is already a final exam there. They are given the option to overwrite existing or cancel
					int reply = JOptionPane.showConfirmDialog(null, "Would you like to overwrite your existing Final Exam to create a new one?\nPlease note that your existing exam, previous scores, and STATS will not be saved. \nThis task cannot be undone!", "NOTICE:", JOptionPane.YES_NO_OPTION);
					
					if (reply == JOptionPane.YES_OPTION) {
						reCreate = true;
						testCreated = false;
					}
					else {
						reCreate = false;
					}
				}
				if (!testCreated) {
					
					if (!reCreate) {
						//If created and already added
						testBox.addItem("Final Exam");
						JOptionPane.showMessageDialog(new JFrame(), "Final Exam Created!");
					}
					//Delete all questions under final exam
					try {
						delete(new File("Files/" + semesterBox.getSelectedItem() + "/" + classBox.getSelectedItem() + "/Final Exam"));
					} catch (Exception s) {
						System.out.println(s);
					}
					testBox.setSelectedItem("Final Exam");
					
					//If test is not created, add one.
					createDirectory("Files/" + semesterBox.getSelectedItem().toString() + "/" + classBox.getSelectedItem().toString(), "Final Exam");
					CreateFinal creating = new CreateFinal(semesterBox.getSelectedItem().toString(), classBox.getSelectedItem().toString());
					//Remove stats for final
					String[] statsFile = getFolders("Previous Tests/" + classBox.getSelectedItem().toString());
					for (String fileDeleted : statsFile) 
					{
						fileDeleted = "Previous Tests/" + classBox.getSelectedItem().toString() + "/" + fileDeleted;
						if (fileDeleted.startsWith("Previous Tests/" + classBox.getSelectedItem().toString() + "/Final Exam")) {
							try {
								System.out.println("File to Delete: " + fileDeleted);
								delete(new File(fileDeleted));
							} catch (Exception x) {
								JOptionPane.showMessageDialog(new JFrame(), x);
							}
						}
					}
					
				}
				updatePanel3();
				if (reCreate) {
					JOptionPane.showMessageDialog(new JFrame(), "Final Exam Has been Recreated!");
				}
			}
		});
		
		questionFun[2] = new JButton("View Questions");
		questionFun[2].setBounds(width/2 - (width/4)/2 + 2, questionFun[0].getY(), width/4, 25);
		questionFun[2].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				parseDir(new File("Files/" + semesterBox.getSelectedItem().toString() + "/" + classBox.getSelectedItem().toString() + "/"  + testBox.getSelectedItem().toString()));
				num = 1;
				masterStr = "";
				
				for (Question z : content) 
				{
					if (!masterStr.contains(": " + z.getQuestion() + "\n\n     Answer: " + z.getCorrect() + "\n\n")) {
						masterStr += "Question " + num + ": " + z.getQuestion() + "\n\n     Answer: " + z.getCorrect() + "\n\n";
						num++;
					}
				}
				
				//Pass info to ViewQuestion Class
				ViewQuestions view = new ViewQuestions(panels[0].getWidth() + panels[1].getWidth() + 5, panels[0].getHeight() + panels[2].getHeight() + 5, content, masterStr, semesterBox.getSelectedItem().toString(), classBox.getSelectedItem().toString(), testBox.getSelectedItem().toString());
				
				//Hide all panels
				showAll(false);
				view.setVisible(true);
				
				//add a close btn to return to main screen
				JButton x = new JButton("Quit");
				x.setBounds(panels[0].getWidth() + panels[1].getWidth() + 5 - 105, 15, 100, 25);
				x.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						view.setVisible(false);
						masterStr = "";
						num = 0;
						remove(view);
						showAll(true);
						content.clear();
						updatePanel3();
					}
				});
				view.add(x);
				
				add(view);
				
				
			}
		});
		
		questionFun[3] = new JButton("Flash Cards");
		questionFun[3].setBounds(questionFun[2].getX(), questionFun[1].getY(), width/4, 25);
		questionFun[3].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Check Question Quantity
				if (testInfo[5].getText().contains("None")) {
					JOptionPane.showMessageDialog(new JFrame(), "Please add a question to the test first!");
				}
				else
				{
					//Create a new Flash Cards Frame within main
					FlashCards flashy = new FlashCards(WID - 10, HEI - 10, semesterBox.getSelectedItem().toString(), classBox.getSelectedItem().toString(), testBox.getSelectedItem().toString(), panels);
					
					//Add to frame
					add(flashy);
					
					//Show Flashy
					flashy.setVisible(true);
					
					//Pass through the panels
					flashy.addCloseBtn(panels);
					
					//Hide main
					showAll(false);
				}
			}
		});
		
		questionFun[4] = new JButton("Study Jam!");
		questionFun[4].setBounds(questionFun[2].getX() + questionFun[0].getWidth() + 20, questionFun[0].getY(), width/4, 25);
		questionFun[4].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Get amount of questions
				String tempStr = testInfo[5].getText().toString();
				System.out.println("Questions: " + tempStr);
				
				int tempInt = 0;
				
				if (!tempStr.equalsIgnoreCase("None")) {
					tempInt = Integer.parseInt(tempStr);
				}
				else {
					tempInt = 0;
				}
				
				
				if (tempInt >= 10) 
				{
					game = new StudyGame(semesterBox.getSelectedItem().toString(), classBox.getSelectedItem().toString(), testBox.getSelectedItem().toString(), getWidth(), getHeight(), panels);
					add(game);
					
					showAll(false);
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(), "Please add " + (10 - tempInt) + " more questions to Play!");
				}
			};
		});
		
		questionFun[5] = new JButton("Take Test!");
		questionFun[5].setBounds(questionFun[4].getX(), questionFun[1].getY(), width/4, 25);
		//for the test screen:
		//Create curDirectory
		JButton close = new JButton("Quit");
		close.setBounds(getWidth() - 130, 10, 75, 25);
		close.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
					test.setVisible(false);
					remove(test);
					
					//Show original screen
					for (int i = 0; i < 3; i++) {
						panels[i].setVisible(true);
					}
					
					//Update panels
					updatePanel3();
			}
		});
		
		//For the action
		questionFun[5].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if (testInfo[5].getText().contains("None")) {
					JOptionPane.showMessageDialog(new JFrame(), "Please Add Questions Before You Take the Test!");
				}
				else
				{
					curDirect = "Files/" + semesterBox.getSelectedItem().toString() + "/" + classBox.getSelectedItem().toString()  +"/" + testBox.getSelectedItem().toString();
						
					//Only do this action if there is more than one question
					//Get amount of questions
					if(getFolderCount(curDirect) > 0)
					{
						
						GetStats s = new GetStats(classBox.getSelectedItem().toString(), testBox.getSelectedItem().toString());
						
						int temp = Integer.parseInt(s.getQuestionCount(semesterBox.getSelectedItem().toString(), classBox.getSelectedItem().toString(), testBox.getSelectedItem().toString()));
						int quesTot = 0;
						//System.out.println("temp: " + temp);
						
						if (temp > 10) {
							//System.out.println("Updating");
							quesTot = getUpdatedCount(temp);
							if (quesTot != 0) {
								test = new TakeTest(getWidth(), getHeight(), curDirect, testBox.getSelectedItem().toString(), classBox.getSelectedItem().toString(), quesTot);
								add(test);
								
								//Add cancel btn
								test.add(close);
								
								//Show the test Screen
								test.setVisible(true);
								
								//Hide original screen
								for (int i = 0; i < 3; i++) {
									panels[i].setVisible(false);
								}
							}
						}
						else {
							test = new TakeTest(getWidth(), getHeight(), curDirect, testBox.getSelectedItem().toString(), classBox.getSelectedItem().toString(), temp);
							add(test);
							
							//Add cancel btn
							test.add(close);
							
							//Show the test Screen
							test.setVisible(true);
							
							//Hide original screen
							for (int i = 0; i < 3; i++) {
								panels[i].setVisible(false);
							}
						}
					}
					else {
						//Tell user to input question first
						JOptionPane.showMessageDialog(new JFrame(), "Please input a question first!");
					}
				}
			}
		});
		
		for (int i = 0; i < 6; i++) {
			panels[2].add(questionFun[i]);
		}
		
		hidePanel3();
	}
	
	public int getUpdatedCount(int quesTot)
	{
		int userInput = 0;
		//System.out.println("Question Count: " + quesTot);
		
		//Ask the user how many questions they want on the exam (if more than ten)
		if (quesTot > 10) {
			//System.out.println("Changing Count...");
			//Ask
			while (userInput < 10) {
				String input = JOptionPane.showInputDialog("Question Count is higher then 10.\nPlease set your question count");
				
				if (input != null) {
					try {
						userInput = Integer.parseInt(input);
						if ((userInput >= 10) && (userInput < quesTot)){
							quesTot = userInput;
						}
						else {
							userInput = 0;
							JOptionPane.showMessageDialog(new JFrame(), "Please select a valid number from 10 - " + quesTot);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(new JFrame(), "Please select a valid number from 10 - " + quesTot);
						userInput = 0;
					}
				}
				else {
					userInput = 0;
					break;
				}
			}
		}
		else {
			userInput = quesTot;
		}
		return userInput;
	}

	
	public void semester(int width)
	{
		//Add a btn to add a semester
				addSemester = new JButton("Add Semester");
				addSemester.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JFrame dia = new JFrame();
						dia.setSize(400, 200);
						
						//This will create a JDiolog box that will allow the person to input the name of their semester
						semesterInputByUser = JOptionPane.showInputDialog("Please name your semester: ");
						createDirectory("Files", "");
						
						//Once done, append Semester File
						if (semesterInputByUser != null) 
						{
							
							File f = new File("Files/" + semesterInputByUser);
							try{
							    if(f.mkdir()) 
								{ 
							        //System.out.println("Directory <" + semesterInputByUser + "> Created");
							
										//After Created, add to list
										semesterBox.removeAllItems();
										
										for (String i : getFolders("Files")) 
										{
											semesterBox.addItem(i);
										}
										semesterBox.setSelectedItem(semesterInputByUser);
								} 
							else {
							        //JOptionPane.showMessageDialog(new JFrame(), "Directory is not created");
							    }
							} catch(Exception z){
							    z.printStackTrace();
							} 
						}
						
					}
				});
				addSemester.setBounds(width - (width/2) - 5, 60, width/2, 30);
				panels[0].add(addSemester);
				
				removeSemester = new JButton("Remove Semester");
				removeSemester.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						File directory = new File("Files/" + selectedSemester);
						if (directory.exists()) {
							try
							{
								System.out.println("Deleting Directory: " + directory);
			               	delete(directory);
								//Now, delete the item from the list
								semesterBox.removeItem(selectedSemester);
								classBox.removeAllItems();
								testBox.removeAllItems();
			           	}
							catch(IOException o)
							{
			               	o.printStackTrace();
			           	}
						}
					}
				});
				removeSemester.setBounds(3, 60, width/2, 30);
				panels[0].add(removeSemester);
	}
	
	public void addPanel2(int width, int height)
	{
		panels[1].setLayout(null);
		
		//Add Stats Label
		statsLabel = new JLabel("Stats:")	;
		statsLabel.setBounds(10, 10, width - 200, 10);
		panels[1].add(statsLabel);
		
		for (int i = 0; i < 4; i++) {
			statsBorder[i] = new JLabel("Testing");
			statsBorder[i].setBackground(Color.BLACK);
			statsBorder[i].setOpaque(true);
			
			switch (i) {
				case 0:
				//top
					statsBorder[i].setBounds(52, 15, width - statsLabel.getWidth(), 1);
					//System.out.println("Making Border");
					break;
				case 1:
				//Left
					statsBorder[i].setBounds(12,25,1, height - (statsBorder[i-1].getY()*2) - 10);
					break;
				case 2:
				//Bottom
					statsBorder[i].setBounds(statsBorder[i-1].getX(),statsBorder[i-1].getY() + statsBorder[i-1].getHeight(),width - 24,1);
					break;
				case 3:
				//Right
					statsBorder[i].setBounds(statsBorder[0].getX() + statsBorder[0].getWidth(),statsBorder[0].getY(),1,statsBorder[1].getHeight() + 10);
					break;
				
				default:
					break;
			}
			
			panels[1].add(statsBorder[i]);
		}
		
		//Add all stats you wish to add
		
		//Create default labels
		for (int i = 0; i < testInfo.length; i++) {
			testInfo[i] = new JLabel();
			
			//Set font 
			testInfo[i].setFont(new Font("Serif", Font.PLAIN, 15));
		}
		testInfo[0].setText("Questions: ");
		testInfo[0].setBounds(20, 30, width - 40, 15);
		panels[1].add(testInfo[0]);
		
		//For the distance from teh right
		int x = 16;
		
		//Amount
		testInfo[5].setBounds(testInfo[0].getX() + (width/2) - x, 30, width - 40, 15);
		panels[1].add(testInfo[5]);
		
		testInfo[1].setText("Attempts: ");
		testInfo[1].setBounds(20, testInfo[0].getY() + testInfo[0].getHeight() + 5, width - 40, 15);
		panels[1].add(testInfo[1]);
		
		//Amount
		testInfo[6].setBounds(testInfo[1].getX() + (width/2) - x, testInfo[0].getY() + testInfo[0].getHeight() + 5, width - 40, 15);
		panels[1].add(testInfo[6]);
		
		testInfo[2].setText("Best Grade: ");
		testInfo[2].setBounds(20, testInfo[1].getY() + testInfo[1].getHeight() + 5, width - 40, 15);
		panels[1].add(testInfo[2]);
		
		//Amount
		testInfo[7].setBounds(testInfo[2].getX() + (width/2) - x, testInfo[1].getY() + testInfo[0].getHeight() + 5, width - 40, 15);
		panels[1].add(testInfo[7]);
		
		testInfo[3].setText("Avg. Grade: ");
		testInfo[3].setBounds(20, testInfo[2].getY() + testInfo[2].getHeight() + 5, width - 40, 15);
		panels[1].add(testInfo[3]);
		
		//Amount
		testInfo[8].setBounds(testInfo[3].getX() + (width/2) - x, testInfo[2].getY() + testInfo[0].getHeight() + 5, width - 40, 15);
		panels[1].add(testInfo[8]);
		
		testInfo[4].setText(classBox.getSelectedItem().toString() + " Grades :" );
		testInfo[4].setBounds(20, testInfo[3].getY() + testInfo[3].getHeight() + 5, width - 40, 15);
		panels[1].add(testInfo[4]);
		
		//Add all grades
		//Add Text Area
		gradeOutput = new JTextArea();
		gradeOutput.setEditable(false);
		gradeOutput.setBounds(statsBorder[2].getX() + 5, testInfo[3].getY() + (testInfo[3].getHeight() * 2) + 14, width - (statsBorder[2].getX() * 2) - 10, height/2 - 10);
		//gradeOutput.setBackground(Color.LIGHT_GRAY);
		gradeOutput.setEditable(false);
		gradeOutput.setWrapStyleWord(true);
		gradeOutput.setLineWrap(true);
		gradeOutput.setBorder(null);
		gradesScroll = new JScrollPane (gradeOutput, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panels[1].add(gradesScroll);
		gradesScroll.setBounds(gradeOutput.getBounds());
		gradesScroll.setWheelScrollingEnabled(true);
		gradeOutput.setFont(new Font("Serif", Font.PLAIN, 16));
		String master = "";
		gradeOutput.setText("\n" + master);
		gradesScroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		gradesScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		hidePanel2();
	}
	
	public void addSemesterCombo(int width)
	{
		/*
		ReadFileContents read = new ReadFileContents("Semesters.txt");
		semesterList = read.getContents();
		
		if (read.getSize() == 0) {
			System.out.println("List Empty");
		}*/
		ArrayList <String> f = new ArrayList <String>();
		f.add("Please Select Semester");
		
		for (String i : getFolders("Files")) {
			f.add(i);
		}
		
		String ar[] = f.toArray(new String[f.size()]);
		
		semesterBox = new JComboBox(ar);
		semesterBox.setBounds(5, 30, width - 10, 25);
		semesterBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int index = semesterBox.getSelectedIndex();
				if (index >= 0) {
					selectedSemester = semesterBox.getSelectedItem().toString();
					filesList = pullFiles(selectedSemester);
					
					//Erase contents of test and class boxes
					classBox.removeAllItems();
					classBox.addItem("Please Select Class");
					//Add Items
					for (String i : getFolders("Files/" + semesterBox.getSelectedItem())) {
						classBox.addItem(i);
					}
					
					//Hide Test box (Only show semester and classes)
					
					if (!semesterBox.getSelectedItem().toString().equals("Please Select Semester")) {
						showTest(false);
						showClass(true);
					}
				}
			}
		});
		panels[0].add(semesterBox);
		semesterBox.setVisible(true);
	}
	
	public JLabel addJLabel(JPanel pane, String title, int x, int y, int width, int height)
	{
		JLabel lbl = new JLabel(title);
		lbl.setBounds(x, y, width, height);
		pane.add(lbl);
		
		return lbl;
	}
	
	public File[] pullFiles(String dirName)
	{
	    	File dir = new File(dirName);

	    	return dir.listFiles(new FilenameFilter() 
			{ 
    	       public boolean accept(File dir, String filename)
    	       { 
					return filename.endsWith(".txt"); 
				}
	    	} );

    }

	public static void delete(File file)
    	throws IOException{
 
    	if(file.isDirectory()){
 
    		//directory is empty, then delete it
    		if(file.list().length==0){
 
    		   file.delete();
    		   //System.out.println("Directory is deleted : "  + file.getName());
 
    		}else{
 
    		   //list all the directory contents
        	   String files[] = file.list();
 
        	   for (String temp : files) {
        	      //construct the file structure
        	      File fileDelete = new File(file, temp);
 
        	      //recursive delete
        	     delete(fileDelete);
        	   }
 
        	   //check the directory again, if empty then delete it
        	   if(file.list().length==0){
           	     file.delete();
        	     //System.out.println("Directory is deleted : " + file.getAbsolutePath());
        	   }
    		}
 
    	}else{
    		//if file, then delete it
    		file.delete();
    		//System.out.println("File is deleted : " + file.getAbsolutePath());
    	}
    }

	public void createDirectory(String location, String file)
	{
		File f = new File(location + "/" + file);
		try{
		    if(f.mkdir()) { 
		        //System.out.println("Directory <" + file + "> Created");
		    } else {
		        //JOptionPane.showMessageDialog(new JFrame(), "Directory " + location + "/" + file + " was not created");
		    }
		} 
		catch(Exception z)
		{
		    z.printStackTrace();
		} 
	}
	
	public String[] getFolders(String dirName)
	{
		File dir = new File(dirName);
		filesList = dir.listFiles();
		files.clear();
		if (filesList != null) {
			for( File f : dir.listFiles())
			{
				if (!f.getName().toString().startsWith(".")) {
					files.add(f.getName().toString());
					//System.out.println("Found: " + f.getName() );
				}
				
			}
		}
		
		
		//Add arraylist items to array
		String[] stringArray = files.toArray(new String[files.size()]);
		Arrays.sort(stringArray);
		panels[0].repaint();
		return stringArray;
	}
	
	public int getFolderCount(String dirName)
	{
		int a = 0;
		File dir = new File(dirName);
		filesList = dir.listFiles();
		files.clear();
		if (filesList != null) {
			for( File f : dir.listFiles())
				{
					if (!f.getName().toString().startsWith(".")) {
						a++;
					}
				}
		}
		return a;
	}
	
	public void showTest(boolean a)
	{
		testLabel.setVisible(a);
		testBox.setVisible(a);
		addTest.setVisible(a);
		removeTest.setVisible(a);
	}
	
	//hide the correct JCombo, btns, and labels
	public void showSemester(boolean a)
	{
		semesterBox.setVisible(a);
		semesterLabel.setVisible(a);
		addSemester.setVisible(a);
		removeSemester.setVisible(a);
	}
	
	public void showClass(boolean a)
	{
		classBox.setVisible(a);
		classLabel.setVisible(a);
		addClass.setVisible(a);
		removeClass.setVisible(a);
		
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
						Collections.shuffle(Arrays.asList(an));						String c = t.getCorrect();
						String ty = t.getType();
						content.add(new Question(ty, q, an, c));
							
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

	public void showAll(boolean a)
	{
		for (int i = 0; i < 3; i++) {
			panels[i].setVisible(a);
		}
	}


}