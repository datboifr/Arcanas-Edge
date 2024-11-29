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
	public BufferedImage[] LmeleeRD = new BufferedImage[9]; // Adjusted for the number of frames
	public BufferedImage idleB, idleF, idleR, idleL;

	public int spriteNumber = 1;

	KeyHandler keyHandler;

	// Constructor
	public Player(int x, int y, int width, int height, KeyHandler keyHandler) {
		super(x, y, width, height);
		direction = "down";
		this.keyHandler = keyHandler;

		speed = 5; // Set default speed
		health = 100;
		damage = 1;

		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
		} catch (IOException e) {
		}

		loadPlayerImages();
	}

	public void update() {
		if (keyHandler.bActive) {
			attacking = true;
		}
		if (attacking == true) {
			attacking();
		}
		// Check movement flags and set direction accordingly
		else if (keyHandler.upActive || keyHandler.downActive || keyHandler.leftActive || keyHandler.rightActive
				|| keyHandler.upRightPressed || keyHandler.upLeftPressed || keyHandler.bActive) {

			if (keyHandler.upLeftPressed) {
				direction = "upLeft";
				y -= speed;
				x -= speed;
			} else if (keyHandler.upRightPressed) {
				direction = "upRight";
				y -= speed;
				x += speed;
			} else if (keyHandler.upActive) {
				direction = "up";
				y -= speed;
			} else if (keyHandler.downActive) {
				direction = "down";
				y += speed;
			} else if (keyHandler.leftActive) {
				direction = "left";
				x -= speed;
			} else if (keyHandler.rightActive) {
				direction = "right";
				x += speed;
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
			case "up":
				if (!keyHandler.upActive && !attacking) {
					sprite = idleB;
				} else if (keyHandler.upActive && !attacking) {
					sprite = runBack[spriteNumber - 1];
				}
				break;
			case "down":
				if (!keyHandler.downActive) {
					sprite = idleF;
				} else {
					sprite = runForward[spriteNumber - 1];
				}
				break;
			case "left":
				if (!keyHandler.leftActive) {
					sprite = idleL;
				} else {
					sprite = runLeft[spriteNumber - 1];
				}
				break;
			case "right":
				if (!keyHandler.rightActive && !attacking) {
					sprite = idleR;
				} else if (keyHandler.rightActive && !attacking) {
					sprite = runRight[spriteNumber - 1];
				} else if (attacking) {
					sprite = LmeleeR[spriteNumber - 1];
				}
				if (attacking2) {
					sprite = LmeleeRD[spriteNumber - 1];
				}
				break;
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

			for (int i = 0; i < 8; i++) {
				LmeleeR[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/LmeleeR" + (i + 1) + ".png"));
			}
			for (int i = 0; i < 9; i++) {
				LmeleeRD[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/LmeleeRD" + (i + 1) + ".png"));
			}

		} catch (IOException e) {
		}
	}

	public void attacking() {
		spriteCounter++;

		if (spriteCounter <= 5) {
			spriteNumber = 1;
		} else if (spriteCounter <= 10) {
			spriteNumber = 2;
		} else if (spriteCounter <= 15) {
			spriteNumber = 3;
		} else if (spriteCounter <= 20) {
			spriteNumber = 4;
		} else if (spriteCounter <= 25) {
			spriteNumber = 5;
		} else if (spriteCounter <= 30) {
			spriteNumber = 6;
		} else if (spriteCounter <= 35) {
			spriteNumber = 7;
		} else if (spriteCounter <= 39) {
			spriteNumber = 8;
		}

		if (spriteCounter > 39) {
			spriteNumber = 1;
			spriteCounter = 0;
			attacking = false;
			attacking2 = true;
		}

		if (attacking2) {
			spriteCounter++;
			if (spriteCounter <= 5) {
				spriteNumber = 1;
			} else if (spriteCounter <= 10) {
				spriteNumber = 2;
			} else if (spriteCounter <= 15) {
				spriteNumber = 3;
			} else if (spriteCounter <= 20) {
				spriteNumber = 4;
			} else if (spriteCounter <= 25) {
				spriteNumber = 5;
			} else if (spriteCounter <= 30) {
				spriteNumber = 6;
			} else if (spriteCounter <= 35) {
				spriteNumber = 7;
			} else if (spriteCounter <= 39) {
				spriteNumber = 8;
			}
			if (spriteCounter > 39) {
				spriteNumber = 1;
				spriteCounter = 0;
				attacking = false;
				attacking2 = false;
			}
		}
	}

	public void upgradeSpeed() {
		System.out.print("Speed Upgraded");
	}

	public void upgradeDamage() {
		System.out.print("Damage Upgraded");
	}

	public void upgradeHealth() {
		System.out.print("Health Upgraded");
	}
}
