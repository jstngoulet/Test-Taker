import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;


public class ViewQuestions extends JPanel
{
	public int width, height;
	public JTextArea questionInput;
	public JScrollPane scroll;
	String master, mySemester, myClass, myTest;
	ArrayList <String> questions = new ArrayList <String>();
	ArrayList <String> files = new ArrayList <String>();
	ArrayList <Question> q = new ArrayList<Question>();
	String[] questionsArray, aArray;
	JComboBox removeBox;
	
	public ViewQuestions(int w, int h, ArrayList <Question> quest, String list, String curSemester, String curClass, String curTest)
	{
		master = "";
		width = w;
		height = h;
		q = quest;
		
		if ((master == null) || (master.equals("")))
		{
			//Create a master string
			int counter = 0;
			for (Question e : q) 
			{
				//Get the questions and answers
				master += "Question " + (counter + 1) + ": " + q.get(counter).getQuestion() + "\n\n     Answer: " + q.get(counter).getCorrect().substring(0, 1).toUpperCase() + q.get(counter).getCorrect().substring(1, q.get(counter).getCorrect().length()) + "\n\n";
				counter++;
			}
		}
		else {
			master += "No Questions Yet!";
		}
		
		mySemester = curSemester;
		myClass = curClass;
		myTest = curTest;
		
		//Create Panel
		setBounds(5, 5, width, height);
		setLayout(null);
		addComp();
		setBackground(Color.WHITE);
		setVisible(true);
	}
	
	public void addComp()
	{
		//Add Text Area
		questionInput = new JTextArea();
		questionInput.setBounds(0, 50, getWidth(), getHeight() - 100);
		//questionInput.setBackground(Color.LIGHT_GRAY);
		questionInput.setEditable(false);
		questionInput.setWrapStyleWord(true);
		questionInput.setLineWrap(true);
		questionInput.setFont(new Font("Arial", Font.PLAIN, 14));
		scroll = new JScrollPane (questionInput, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroll);
		scroll.setBounds(questionInput.getBounds());
		scroll.setWheelScrollingEnabled(true);
		questionInput.setText("\n" + master.trim() + "\n");
		if (questionInput.getText().trim().equals("")) {
			questionInput.setText("\nNo Questions Yet!");
		}
		questionInput.setCaretPosition(0);
		
		//Add a Label to inform the user to select to remove
		JLabel removeLbl = new JLabel("Remove Question: ");
		removeLbl.setBounds(5, height - 30, width/4, 15);
		add(removeLbl);
		
		//Add a label to display the title of the current screen
		JLabel titleLabel = new JLabel("Questions Currently in Exam: ");
		titleLabel.setFont(new Font("Arial Black", Font.PLAIN, 18));
		titleLabel.setBounds(5, questionInput.getY() - 25, width - 10, 25);
		add(titleLabel);
		
		//Add A JCombo
		questions.clear();
		
		for (int i = 0; i < q.size(); i++) {
			questions.add(q.get(i).getQuestion());
		}
		questionsArray = questions.toArray(new String[questions.size()]);
		
		removeBox = new JComboBox(questionsArray);
		removeBox.setBounds(width/2 - width/4, removeLbl.getY() - 5, (width/2), 25);
		add(removeBox);
		
		//Add a button to remove a question
		JButton removeBtn = new JButton("Remove");
		removeBtn.setBounds(removeBox.getX() + removeBox.getWidth() + 5, removeBox.getY(), removeLbl.getWidth() - 10, removeBox.getHeight());
		removeBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				GetStats s = new GetStats(myClass, myTest);
				
				//Delete Question
				try {
					aArray = getFolders("Files/" + mySemester + "/" + myClass + "/" + myTest + "/");
					
					//After folders were obtained, now delete
					for (String w : aArray) {
						if (w.contains(removeBox.getSelectedItem().toString())) {
							//System.out.println("W: " + w);
							delete(new File("Files/" + mySemester + "/" + myClass + "/" + myTest + "/" + w));
						}
					}
				} catch (Exception d) {
					JOptionPane.showMessageDialog(new JFrame(), d);
				}
				//Remove from ArrayList
				int a = 0;
				for (Question i : q) {
					if (q.get(a).getQuestion().contains(removeBox.getSelectedItem().toString())) 
					{
						//System.out.println("Removing Question: " + removeBox.getSelectedItem().toString());
						removeBox.removeItem(removeBox.getSelectedItem().toString());
						
						//Remove from arraylist
						q.remove(a);
						
						//Now, update master
						
						//Update the text Field
						//Create a master string
						int counter = 0;
						master = "";
						questions.clear();
						removeBox.removeAllItems();
						
						for (Question t : q) 
						{
							//Get the questions and answers
							master += "Question " + (counter + 1) + ": " + q.get(counter).getQuestion() + "\n\n     Answer: " + q.get(counter).getCorrect() + "\n\n";
							//Add question to arraylist
							questions.add(q.get(counter).getQuestion().toString());
							
							//Update combo
							//System.out.println("Question adding: " + q.get(counter).getQuestion().toString());
							removeBox.addItem(q.get(counter).getQuestion().toString());
							
							counter++;
						}
						
						if ((master != null) && (!master.equals("")))
						{
							//Update the text
							questionInput.setText("\n" + master.trim() + "\n");
						}
						else {
							questionInput.setText("\nNo More Questions!");
						}
						break;
					}
					a++;
				}	
			}
		});
		add(removeBtn);
	}
	
	public static void delete(File file)
    	throws IOException{
 
    	if(file.isDirectory()){
 
    		//directory is empty, then delete it
    		if(file.list().length==0){
 
    		   file.delete();
    		   //System.out.println("Directory is deleted : " + file.getAbsolutePath());
 
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
        	     JOptionPane.showMessageDialog(new JFrame(), "Question is deleted : " 
                                                  + file.getPath());
        	   }
    		}
 
    	}else{
    		//if file, then delete it
    		file.delete();
    		JOptionPane.showMessageDialog(new JFrame(), "Question is deleted: " + file.getPath().substring(6, file.getPath().toString().length()));
    	}
    }
	
	public String[] getFolders(String dirName)
	{
		File dir = new File(dirName);
		File[] filesList = dir.listFiles();
		files.clear();
		//System.out.println("CurDirect: " + dirName);
		if (filesList != null) {
			for( File f : dir.listFiles())
			{
				if ((!f.getName().toString().startsWith(".")) && (f.getName().toString().endsWith(".txt"))){
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
	
	
}