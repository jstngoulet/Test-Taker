import java.util.*;
import java.awt.*;
import java.io.*;




public class ReadFileContents 
{

   	ArrayList <String> content = new ArrayList <String>();
	String [] words = null;

	public ReadFileContents(String fileName)
	{
		Scanner inputStream = null;
	      try
	      {
	      //Read in scores and names
	         inputStream = new Scanner(new FileInputStream("Files/" + fileName));
	
			if (inputStream.hasNext()) 
			{
				while (inputStream.hasNext())
			      {
			      //Get the names and scores
			         String temp = inputStream.nextLine();
			         
			         //add items to an arraylist
			         content.add(temp);
			      }
				
			//Close the stream
			inputStream.close();
			
			}
			else {
				String temp = "< List Empty >";
				content.add(temp);
			}
		      
	      }
	      
	      catch(FileNotFoundException e)
	      {
				//If no file is created, create one
				try {
					//Create Directory
					File f = new File("Files");
					try
					{
					    if(f.mkdir()) { 
					        System.out.println("Directory <Files> Created");
					    } else {
					        System.out.println("Directory is not created");
					    }
					} 
					catch(Exception z){
					    z.printStackTrace();
					} 
					
					System.out.println("Creating File: " + fileName);
					PrintWriter out = new PrintWriter(new FileOutputStream("Files/" + fileName));
					out.println(fileName.substring(fileName.length() - 3, fileName.length()));
				} catch (Exception a) {
					System.out.println(a);
				}
	      }
	   
	      
	      //Sort the words alphebetically
	      Collections.sort(content);
	      
	      //Put items in an array for sorting
	      words = content.toArray(new String[content.size()]);
	
	   }
	
	public String[] getContents()
	{
		if (getSize() == 0) {
			content.add("< List Empty >");
		}
		//System.out.println("Return Word List");
		words = content.toArray(new String[content.size()]);
		//System.out.println("Array Size: " + content.size());
		
		return words;
	}
	
	public int getSize()
	{
		return content.size();
	}
}


