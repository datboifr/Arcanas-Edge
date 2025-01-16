package main;

import javax.swing.*;

public class Frame extends JFrame {

	public Frame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setUndecorated(true);
		openGame(); // Start with the menu
		this.setVisible(true);
	}

	public void openGame() {
		this.getContentPane().removeAll();
		GamePanel gamePanel = new GamePanel(this);
		this.add(gamePanel);
		this.revalidate();
		this.repaint();
		this.pack();
	}

	public void restartGame() {
		this.getContentPane().removeAll(); // Remove the current panel
		GamePanel gamePanel = new GamePanel(this); // Create a new instance of GamePanel
		this.add(gamePanel); // Add the new panel
		this.revalidate(); // Refresh the frame layout
		this.repaint(); // Repaint the frame
		this.pack(); // Adjust the frame to fit the new panel
	}
}
