package main;

import javax.swing.*;

public class Frame extends JFrame {

	Frame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);

		GraphicsPanel graphicsPanel = new GraphicsPanel();
		this.add(graphicsPanel);
		this.pack();

		this.setVisible(true);

		graphicsPanel.startGameThread();
	}
}
