/*
   The JFrame class for Assignment Three.  You do not need to modify
   this class.

   An instance of the A3JPanel class is created and added to the content
   pane of the JFrame.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class A3JFrame extends JFrame{
	

	public A3JFrame(String title, int x, int y, int width, int height) {

		// Set the title, top left location, and close operation for the frame
		setTitle(title);
		setLocation(x, y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create an instance of the JPanel class, and set this to define the
		// content of the window
		JPanel frameContent = new Chess();
		Container visibleArea = getContentPane();
		visibleArea.add(frameContent);

		// Set the size of the content pane of the window, resize and validate the
		// window to suit, obtain keyboard focus, and then make the window visible
		frameContent.setPreferredSize(new Dimension(width, height));
		pack();
		frameContent.requestFocusInWindow();
		setVisible(true);
		setResizable(false);
		
		// t.start();
		
	}

			
			
		
}
