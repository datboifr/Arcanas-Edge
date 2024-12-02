package objects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import main.KeyHandler;

public class Player extends Entity {

	private final int RUNNING_FRAMES = 10;
	private final int ATTACK1_FRAMES = 8;
	private final int ATTACK2_FRAMES = 9;
	private BufferedImage[] runBack = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] runForward = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] runLeft = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] runRight = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] LmeleeR = new BufferedImage[ATTACK1_FRAMES];
	private BufferedImage[] LmeleeRD = new BufferedImage[ATTACK2_FRAMES];
	private BufferedImage idleB, idleF, idleR, idleL;
	private int spriteNumber;

	KeyHandler keyHandler;

	private final float DEFAULT_HEALTH = 100;
	private final float DEFAULT_AGILITY = 4;
	private final float DEFAULT_STRENGTH = 5;
	private final float DEFAULT_PROJECTILE_SPEED = 10;

	ArrayList<Projectile> projectiles;

	// Constructor
	public Player(int x, int y, int width, int height, KeyHandler keyHandler, ArrayList<Projectile> projectiles) {
		super(x, y, width, height);
		direction = "down";
		this.keyHandler = keyHandler;

		this.health = DEFAULT_HEALTH;
		this.speed = DEFAULT_AGILITY;
		this.strength = DEFAULT_STRENGTH;
		this.projectileSpeed = DEFAULT_PROJECTILE_SPEED;

		this.projectiles = projectiles;

		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
		} catch (IOException e) {
		}

		this.spriteNumber = 1;
		loadPlayerImages();
	}

	public void update() {

		handleMovement();

		if (keyHandler.bActive && !isAttacking) {
			System.out.println("Attacked!");
			attack();
		}

		// Handle attack state but allow movement to happen simultaneously
		if (isAttacking) {
			attacking();
			updateSprite();
			return;
		}

		// Handle movement if the player is moving
		if (isMoving()) {
			updateAnimation();
		}

		updateSprite();
	}

	private void attack() {
		isAttacking = true;
		projectiles.add(new Projectile(this, directionLiteral - 10));
		projectiles.add(new Projectile(this, directionLiteral));
		projectiles.add(new Projectile(this, directionLiteral + 10));
	}

	private boolean isMoving() {
		return keyHandler.upActive || keyHandler.downActive || keyHandler.leftActive || keyHandler.rightActive
				|| keyHandler.upRightPressed || keyHandler.upLeftPressed;
	}

	private void handleMovement() {
		if (keyHandler.upActive) {
			direction = "up";
			directionLiteral = 270;
			y -= speed;
		} else if (keyHandler.downActive) {
			direction = "down";
			directionLiteral = 90;
			y += speed;
		} else if (keyHandler.leftActive) {
			direction = "left";
			directionLiteral = 180;
			x -= speed;
		} else if (keyHandler.rightActive) {
			direction = "right";
			directionLiteral = 0;
			x += speed;
		}
	}

	private void updateAnimation() {
		spriteCounter++;
		if (spriteCounter > 5) {
			spriteCounter = 0;
			spriteNumber = (spriteNumber % 10) + 1; // Loops spriteNumber between 1 and 10
		}
	}

	private void updateSprite() {
		switch (direction) {
			case "up":
				sprite = keyHandler.upActive ? runBack[spriteNumber - 1] : idleB;
				break;
			case "down":
				sprite = keyHandler.downActive ? runForward[spriteNumber - 1] : idleF;
				break;
			case "left":
				sprite = keyHandler.leftActive ? runLeft[spriteNumber - 1] : idleL;
				break;
			case "right":
				if (isAttacking) {
					sprite = attacking2 ? LmeleeRD[spriteNumber - 1] : LmeleeR[spriteNumber - 1];
				} else {
					sprite = keyHandler.rightActive ? runRight[spriteNumber - 1] : idleR;
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

			for (int i = 0; i < ATTACK1_FRAMES; i++) {
				LmeleeR[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/LmeleeR" + (i + 1) + ".png"));
			}
			for (int i = 0; i < ATTACK2_FRAMES; i++) {
				LmeleeRD[i] = ImageIO.read(getClass().getResourceAsStream("/res/player/LmeleeRD" + (i + 1) + ".png"));
			}

		} catch (IOException e) {
		}
	}

	public void attacking() {
		spriteCounter++;

		// Determine the sprite frame based on spriteCounter
		spriteNumber = (spriteCounter - 1) / 5 + 1;

		if (spriteCounter > 39) {
			// Transition to the next phase or reset if attacking2 is active
			spriteCounter = 0;
			if (isAttacking) {
				isAttacking = false;
				attacking2 = true;
			} else if (attacking2) {
				attacking2 = false;
			}
		}
	}

	// all stats stuff

	public void upgradeHealth() {
		this.health += DEFAULT_HEALTH / 10; // increases by 10%
		System.out.println("Health Upgraded");
	}

	public void upgradeAgility() {
		this.speed += DEFAULT_STRENGTH / 20; // increases by 5%
		System.out.println("Speed Upgraded");
	}

	public void upgradeStrength() {
		this.strength += DEFAULT_STRENGTH / 10; // increases by 10%
		System.out.println("Strength Upgraded");
	}
}
