package objects;

import java.awt.image.BufferedImage;

public class Entity extends Object {
	int speed;
	String direction;
	BufferedImage[] animationFrames;
	int spriteCounter = 0; // Counts frames for animation timing
	int spriteIndex = 0;

	// Constructor
	public Entity(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	// Set the animation frames for this entity
	public void setAnimationFrames(BufferedImage[] frames) {
		this.animationFrames = frames;
		if (frames != null && frames.length > 0) {
			sprite = frames[0]; // Default to the first frame
		}
	}

}