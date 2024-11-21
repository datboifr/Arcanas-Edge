package objects;

import java.awt.image.BufferedImage;

public class Entity extends Object {
	int speed;
	String direction;
	int spriteCounter = 0; // Counts frames for animation timing
	int spriteIndex = 0;

	// Constructor
	public Entity(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

}