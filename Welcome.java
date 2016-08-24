import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;



public class Welcome extends JPanel
{
	public JPanel main = new JPanel();
	public ImageIcon mainImage = new ImageIcon("_Logo.png");
	
	//Initi vars
	public static int _WIDTH, _HEIGHT;
	
	public Welcome(int width, int height, String btnText)
	{
		_WIDTH = width;
		_HEIGHT = height;
		
		setBackground(Color.WHITE);
		setLayout(null);
		setSize(width, height);
		
		//Add Label
		JLabel logoLabel = new JLabel();
		logoLabel.setBounds(0, -50, _WIDTH, _HEIGHT);
		logoLabel.setIcon(mainImage);
		
		//Add Image to Label
		add(logoLabel);
				
		JButton btn = new JButton(btnText);
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//Hide the objects in this panel
				btn.setVisible(false);
				logoLabel.setVisible(false);
				
				//Add the function screen
						ClassOverview clas = new ClassOverview(width, height);
						add(clas);
			}
		});
		
		float center = _WIDTH/2, btnWidth = _WIDTH/3, btnHeight = _HEIGHT/8;
		
		btn.setBounds((int)center - ((int)btnWidth/2), _HEIGHT - 100, (int)btnWidth, (int)btnHeight);
		add(btn);
		
		setVisible(true);
	}
	
	public boolean checkForVisible()
	{
		if (this.isVisible()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
}