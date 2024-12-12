package objects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.KeyHandler;

public class Player extends GameObject {

	private final int RUNNING_FRAMES = 10;
	private final int ATTACK_FRAMES = 9;
	private BufferedImage[] runBack = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] runForward = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] runLeft = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] runRight = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] LmeleeR = new BufferedImage[ATTACK_FRAMES];
	private BufferedImage[] LmeleeRD = new BufferedImage[ATTACK_FRAMES];
	private BufferedImage idleB, idleF, idleR, idleL;
	private int spriteNumber;

	KeyHandler keyHandler;

	private final float DEFAULT_HEALTH = 100;
	private final float DEFAULT_SPEED = 4;
	private final float DEFAULT_CONTACT_DAMAGE = 5;

	private final float DEFAULT_PROJECTILE_DAMAGE = 1;
	private final float DEFAULT_PROJECTILE_SPEED = 1;
	private final float DEFAULT_PROJECTILE_SIZE = 1;

	ArrayList<Projectile> projectiles;

	// Constructor
	public Player(int x, int y, int width, int height, KeyHandler keyHandler, ArrayList<Projectile> projectiles) {
		super(x, y, width, height);
		direction = "down";
		this.keyHandler = keyHandler;

		this.health = DEFAULT_HEALTH;
		this.speed = DEFAULT_SPEED;
		this.contactDamage = 0;

		this.projectileDamage = DEFAULT_PROJECTILE_DAMAGE;
		this.projectileSpeed = DEFAULT_PROJECTILE_SPEED;
		this.projectileSize = DEFAULT_PROJECTILE_SIZE;

		this.projectiles = projectiles;

		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
		} catch (IOException e) {
		}

		this.spriteNumber = 1;
		loadPlayerImages();
	}

	public void update() {

		if (health < 0) {
			this.isDead = true;
		} else {
			handleMovement();
			if (keyHandler.bActive && !isAttacking) {
				attack();
			}
			updateAnimation();
			updateSprite();
		}
	}

	public void hit(GameObject other) {
		this.health -= other.getContactDamage();
	}

	private void attack() {
		isAttacking = true;
		projectiles.add(new Projectile(this, directionLiteral, ProjectileType.LIGHTNING));
	}

	@SuppressWarnings("unused")
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
		frameCounter++;

		if (isAttacking) {
			spriteNumber = (frameCounter - 1) / SPRITES_PER_FRAME + 1;
			if (spriteNumber > ATTACK_FRAMES) {
				isAttacking = false;
				frameCounter = 0;
			}
		} else if (frameCounter > SPRITES_PER_FRAME) {
			frameCounter = 0;
			spriteNumber = (spriteNumber % 10) + 1; // Loops spriteNumber between 1 and 10
		}
	}

	private void updateSprite() {
		if (isAttacking) // add switch statement for directions later
			sprite = LmeleeR[spriteNumber - 1];
		else
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
					sprite = keyHandler.rightActive ? runRight[spriteNumber - 1] : idleR;
					break;
			}
	}

	public void loadPlayerImages() {
		try {
			// run sprites
			for (int i = 0; i < RUNNING_FRAMES; i++) {
				runBack[i] = ImageIO
						.read(getClass().getResourceAsStream("/res/player/run/back/RunB" + (i + 1) + ".png"));
				runForward[i] = ImageIO
						.read(getClass().getResourceAsStream("/res/player/run/front/RunF" + (i + 1) + ".png"));
				runLeft[i] = ImageIO
						.read(getClass().getResourceAsStream("/res/player/run/left/RunL" + (i + 1) + ".png"));
				runRight[i] = ImageIO
						.read(getClass().getResourceAsStream("/res/player/run/right/RunR" + (i + 1) + ".png"));
			}

			// idle sprites
			idleB = ImageIO.read(getClass().getResourceAsStream("/res/player/idleBack.png"));
			idleF = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
			idleR = ImageIO.read(getClass().getResourceAsStream("/res/player/idleRight.png"));
			idleL = ImageIO.read(getClass().getResourceAsStream("/res/player/idleLeft.png"));

			// attack sprites
			for (int i = 0; i < ATTACK_FRAMES; i++) {
				LmeleeR[i] = ImageIO
						.read(getClass().getResourceAsStream("/res/player/attack/right/LmeleeR" + (i + 1) + ".png"));
				LmeleeRD[i] = ImageIO
						.read(getClass().getResourceAsStream("/res/player/attack/right/LmeleeRD" + (i + 1) + ".png"));
			}

		} catch (IOException e) {
			System.out.println("Couldn't Fetch Sprite");
		}
	}

	// all stats stuff

	public void upgradeHealth() {
		this.health += DEFAULT_HEALTH / 10; // increases by 10%
		System.out.println("Health Upgraded");
	}

	public void upgradeAgility() {
		this.speed += DEFAULT_SPEED / 20; // increases by 5%
		System.out.println("Speed Upgraded");
	}

	public void upgradeContactDamage() {
		this.contactDamage += DEFAULT_CONTACT_DAMAGE; // increases by 5
		System.out.println("Contact Damage Upgraded");
	}

	public void upgradeProjectileDamage() {
		this.projectileDamage += DEFAULT_PROJECTILE_DAMAGE / 10; // increases by 10%
		System.out.println("Strength Upgraded");
	}

	public void upgradeProjectileSpeed() {
		this.projectileSpeed += DEFAULT_PROJECTILE_SPEED / 10; // increases by 10%
		System.out.println("Projectile Speed Upgraded");
	}

	public void upgradeProjectileSize() {
		this.projectileSize += DEFAULT_PROJECTILE_SIZE / 10; // increases by 10%
		System.out.println("Projectile Size Upgraded");
	}

}
