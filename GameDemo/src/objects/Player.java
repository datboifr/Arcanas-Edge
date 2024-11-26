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
	public BufferedImage[] LmeleeR = new BufferedImage[RUNNING_FRAMES];
	public BufferedImage idleB, idleF, idleR, idleL;

	public int spriteNumber = 1;

	KeyHandler keyHandler;

	// Constructor
	public Player(int x, int y, int width, int height, KeyHandler keyHandler) {
		super(x, y, width, height);
		speed = 5; // Set default speed
		direction = "down";
		this.keyHandler = keyHandler;

		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
		} catch (IOException e) {
		}
	}

	public void update() {
		 if (keyHandler.IPressed){
			attacking = true;
		}
		 if (attacking == true){
			attacking();

		 }
		// Check movement flags and set direction accordingly
		else if (keyHandler.upPressed == true || keyHandler.downPressed == true || keyHandler.leftPressed == true
				|| keyHandler.rightPressed == true
				|| keyHandler.upRightPressed || keyHandler.upLeftPressed == true|| keyHandler.IPressed == true) {

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
			}else if (keyHandler.IPressed){
				attacking = true;
			}
			
			

			// Update sprite animation
			spriteCounter++;
			if (spriteCounter > 5) {
				spriteCounter = 0;
				spriteNumber++;
				if (spriteNumber > 10) {
					spriteNumber = 1;
				}
			}
		}

		// Switch for setting the correct sprite based on direction and movement state
		switch (direction) {
			case "up" -> sprite = keyHandler.upPressed ? runBack[spriteNumber - 1] : idleB;
			case "down" -> sprite = keyHandler.downPressed ? runForward[spriteNumber - 1] : idleF;
			case "left" -> sprite = keyHandler.leftPressed ? runLeft[spriteNumber - 1] : idleL;
			case "right" ->{
			 sprite = keyHandler.rightPressed ? runRight[spriteNumber - 1] : idleR;
			 if(attacking == true){
				sprite =LmeleeR[spriteNumber - 1] ;
			 }
			}
			
		}
	}

	public void loadPlayerImages() {
		try {
			for (int i = 0; i < RUNNING_FRAMES; i++) {
				runBack[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/RunBack" + (i + 1) + ".png"));
				runForward[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/RunF" + (i + 1) + ".png"));
				runLeft[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/RunL" + (i + 1) + ".png"));
				runRight[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/RunR" + (i + 1) + ".png"));
				
			}
			idleB = ImageIO.read(getClass().getResourceAsStream("/res/player/idleBack.png"));
			idleF = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
			idleR = ImageIO.read(getClass().getResourceAsStream("/res/player/idleRight.png"));
			idleL = ImageIO.read(getClass().getResourceAsStream("/res/player/idleLeft.png"));

		} catch (IOException e) {
		}
	}
	public void attacking(){

		
		for (int i = 0; i < 40; i+=5) {
			spriteNumber = 1;
			spriteNumber++;

		}
		
		// if (spriteCounter <=5) {
		// 	spriteNumber =1;
		// }
		// if(spriteCounter > 5 &&  spriteCounter < 10) {
		// 	spriteNumber = 2;
		// }
		// if (spriteCounter > 10 && spriteCounter < 15) {
		// 	spriteNumber = 3;
		// }
		// if (spriteCounter > 15 && spriteCounter < 20) {
		// 	spriteNumber = 4;
		// }
	}

	public void loadPlayerAttackImages() {
		try {
		for (int i = 0; i < RUNNING_FRAMES; i++) {
		LmeleeR[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/LmeleeR" + (i + 1) + ".png"));
		}
	}
	catch (IOException e) {
	}

	}
}