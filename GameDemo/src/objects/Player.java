package objects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.KeyHandler;

public class Player extends Entity {
	final int RUNNING_FRAMES = 10;

	public BufferedImage[] runBack = new BufferedImage[RUNNING_FRAMES];
	public BufferedImage[] runForward = new BufferedImage[RUNNING_FRAMES];
	public BufferedImage[] runLeft = new BufferedImage[RUNNING_FRAMES];
	public BufferedImage[] runRight = new BufferedImage[RUNNING_FRAMES];
	public BufferedImage idleB, idleF, idleR, idleL;

	public int spriteCounter = 0;
	public int spriteNumber = 1;

	KeyHandler keyHandler;

	// Constructor
	public Player(int x, int y, int width, int height, KeyHandler keyHandler) {
		super(x, y, width, height);
		speed = 5; // Set default speed
		direction = "down";
		this.keyHandler = keyHandler; //

		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Handle input to update the direction
	public void update() {
		// Check movement flags and set direction accordingly
		if (keyHandler.upPressed == true || keyHandler.downPressed == true || keyHandler.leftPressed == true
				|| keyHandler.rightPressed == true
				|| keyHandler.upRightPressed || keyHandler.upLeftPressed == true) {

			if (keyHandler.upLeftPressed) {
				direction = "upLeft";
				y -= speed;
				x -= speed;
			} else if (keyHandler.upRightPressed) {
				direction = "upRight";
				y -= speed;
				x += speed;
			} else if (keyHandler.upPressed) {
				direction = "up";
				y -= speed;
			} else if (keyHandler.downPressed) {
				direction = "down";
				y += speed;
			} else if (keyHandler.leftPressed) {
				direction = "left";
				x -= speed;
			} else if (keyHandler.rightPressed) {
				direction = "right";
				x += speed;
			}

			// Update sprite animation
			spriteCounter++;
			if (spriteCounter > 5) {
				spriteCounter = 0; // Reset counter
				spriteNumber++; // Move to the next sprite
				if (spriteNumber > 10) {
					spriteNumber = 1; // Loop back to the first sprite
				}
			}
		}

		// Switch for setting the correct sprite based on direction and movement state
		switch (direction) {
			case "up":
				sprite = keyHandler.upPressed ? runBack[spriteNumber - 1] : idleB;
				break;
			case "down":
				sprite = keyHandler.downPressed ? runForward[spriteNumber - 1] : idleF;
				break;
			case "left":
				sprite = keyHandler.leftPressed ? runLeft[spriteNumber - 1] : idleL;
				break;
			case "right":
				sprite = keyHandler.rightPressed ? runRight[spriteNumber - 1] : idleR;
				break;
			case "upLeft":
				sprite = keyHandler.upLeftPressed ? runBack[spriteNumber - 1] : idleB; // Adjust for diagonal
				break;
			case "upRight":
				sprite = keyHandler.upRightPressed ? runForward[spriteNumber - 1] : idleF; // Adjust for diagonal
				break;
		}
	}

	public void loadPlayerImages() {
		try {
			for (int i = 0; i < RUNNING_FRAMES; i++) {
				runBack[i] = ImageIO.read(
						getClass().getResourceAsStream("/res/player/RunBack" + (i + 1) + ".png")); /* RunBack... */
				runForward[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/RunF" + (i + 1) + ".png"));
				runLeft[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/RunL" + (i + 1) + ".png"));
				runRight[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/RunR" + (i + 1) + ".png"));
			}
			idleB = ImageIO.read(getClass().getResourceAsStream("/res/player/idleBack.png"));
			idleF = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
			idleR = ImageIO.read(getClass().getResourceAsStream("/res/player/idleRight.png"));
			idleL = ImageIO.read(getClass().getResourceAsStream("/res/player/idleLeft.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}