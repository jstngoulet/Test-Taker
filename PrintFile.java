import java.awt.Desktop;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;


public class PrintFile 
{
	public PrintFile(String file)
	{
      try 
      {
         File txtFile = new File(file);
         
         if (txtFile.exists()) 
         {
            if (Desktop.isDesktopSupported()) 
            {
               Desktop.getDesktop().print(txtFile);
            } 
            else 
            {
               JOptionPane.showMessageDialog(new JFrame(), "This Operating System is not supported!");
            }
         } 
         else 
         {
            System.out.println("File does not exist!");
         }
         //System.out.println("File " + txtFile + " opened");
      } 
      catch (Exception ex) {
         ex.printStackTrace();
      }
	}
	
}