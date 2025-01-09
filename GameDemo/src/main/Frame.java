package main;

import javax.swing.*;

public class Frame extends JFrame {

	public Frame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setUndecorated(false);
		openGame(); // Start with the menu
		this.setVisible(true);
	}

	public void openGame() {
		this.getContentPane().removeAll();
		GamePanel gamePanel = new GamePanel();
		this.add(gamePanel);
		this.revalidate();
		this.repaint();
		this.pack();
	}
}
