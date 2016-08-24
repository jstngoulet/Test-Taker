//package TeamCreator;

/**
   Justin Goulet Contest Spring 2014 Application
   File Name:          GroupStudents.java
   Programmer:         Justin Goulet
   Date Last Modified: Apr. 22, 2014

   Problem Statement: (what you want the code to do)
   
   This application is designed to allow a user to enter a series of student
   names, ratings and commments, then provide a way to view the student info
   entered. From the main screen in the GUI, the user has the option to 
   select a name from the list, create groups, or add a new student.
   
   The program is going to, after the students have been entered, create groups
   based on the students rating. The groups will be based on a range of the class
   rating so the teams will be relatively even. After the groups are created, they
   will be output to a text file where the user can then print it easily. For their 
   Convienence, the file will open as the groups are completed.
    
   Overall Plan (step-by-step, how you want the code to make it happen):
	(See attached Algorthim)

   Classes needed and Purpose (Input, Processing, Output)
   main class - GroupStudents.java
   
   classes used to accomplish task - 
   Students.java
   getContact.java
   groupsCreator.java
   openFile.java
   sortStudentNames.java
   
   imported classes:
   javax.swing.* - Includes several classes including
      JFrame, JPanel, Container, JComboBox, JLabel, JTextField,
      JButton, JTextArea
   java.awt.* - Includes several classes including BoxLayout,
      setAlignmentx, setLineWrap, Desktop
   java.awt.event.* - Includes several classes including ActionEvent,
      ActionListener
   java.util.ArrayList - uses the ArrayList class
   java.io.* - Includes several classes including PrintWriter, 
      FileOutputStream, FileInputStream, File
   java.util.Scaner - Includes several classes including Scanner
   
   

*/


import java.awt.Desktop;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class openFile 
{
/**
   *openFile() - default constructor. Opens a file by using the Desktop Class
   *and checking to see if the OS is supported.
   *
   */
   public openFile(String file) 
   {
      try 
      {
         File txtFile = new File(file);
         
         if (txtFile.exists()) 
         {
            if (Desktop.isDesktopSupported()) 
            {
               Desktop.getDesktop().open(txtFile);
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