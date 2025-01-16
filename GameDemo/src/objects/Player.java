package objects;

import combat.Ability;
import combat.AbilityTypes;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;
import objects.enemies.Enemy;

public class Player extends GameObject {

	private String literalDirection;
	private final int RUNNING_FRAMES = 10;
	private final int ATTACK_FRAMES = 9;
	private BufferedImage[] runBack = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] runForward = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] runLeft = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] runRight = new BufferedImage[RUNNING_FRAMES];
	private BufferedImage[] LmeleeR = new BufferedImage[ATTACK_FRAMES];
	private BufferedImage[] LmeleeRD = new BufferedImage[ATTACK_FRAMES];
	private BufferedImage idleB, idleF, idleR, idleL;

	KeyHandler keyHandler;

	private ArrayList<Ability> abilities;

	private int aura;
	private int pickupRange;
	private final int DEFAULT_PICKUP_RANGE = 50;

	private final float DEFAULT_MAX_HEALTH = 100;
	private final float DEFAULT_RECOVERY = 0.01f;
	private final float DEFAULT_SPEED = 4;

	private final float DEFAULT_CONTACT_DAMAGE = 0;
	private final float DEFAULT_ABILITY_COOLDOWN = 1;

	private final float DEFAULT_PROJECTILE_DAMAGE = 1;
	private final float DEFAULT_PROJECTILE_SPEED = 1;
	private final float DEFAULT_PROJECTILE_SIZE = 1;

	// Constructor
	public Player(GamePanel gp, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.panel = gp;
		literalDirection = "down";
		this.keyHandler = gp.getKeyHandler();
		this.aura = 0;
		this.pickupRange = DEFAULT_PICKUP_RANGE;

		this.abilities = new ArrayList<>();
		abilities.add(AbilityTypes.fire);
		abilities.add(AbilityTypes.electric);
		abilities.add(AbilityTypes.earth);
		abilities.add(AbilityTypes.falcon);
		abilities.add(AbilityTypes.blast);

		this.maxHealth = DEFAULT_MAX_HEALTH;
		this.health = maxHealth;
		this.recovery = DEFAULT_RECOVERY;
		this.speed = DEFAULT_SPEED;

		this.contactDamage = DEFAULT_CONTACT_DAMAGE;
		this.abilityCooldown = DEFAULT_ABILITY_COOLDOWN;

		this.projectileDamage = DEFAULT_PROJECTILE_DAMAGE;
		this.projectileSpeed = DEFAULT_PROJECTILE_SPEED;
		this.projectileSize = DEFAULT_PROJECTILE_SIZE;
		this.projectileBonus = 2;

		this.hasShadow = true;

		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
		} catch (IOException e) {
			System.out.println("Couldn't Fetch Sprite");
		}

		this.currentFrame = 1;
		loadPlayerImages();
	}

	public void update() {
		if (health <= 0) {
			die();
		} else {
			iFrames--;
			if (health < maxHealth) {
				health += recovery;
			}

			handleMovement();
			collectNearbyAuras();

			if (iFrames <= 0) {
				checkCollisionWithEnemies();
			}

			updateThisAnimation();
			updateSprite();

			for (Ability ability : abilities) {
				if (ability != null) {
					ability.updateCooldown(this);
				}
			}
		}
	}

	private void handleMovement() {
		float deltaX = 0;
		float deltaY = 0;

		if (keyHandler.upActive) {
			literalDirection = "up";
			super.direction = 270;
			if (y > (0 + (height / 2))) {
				deltaY -= speed;
			}
		}
		if (keyHandler.downActive) {
			literalDirection = "down";
			super.direction = 90;
			if (y < (panel.getHeight() - (height / 2))) {
				deltaY += speed;
			}
		}
		if (keyHandler.leftActive) {
			literalDirection = "left";
			super.direction = 180;
			if (x > (0 + (width / 2))) {
				deltaX -= speed;
			}
		}
		if (keyHandler.rightActive) {
			literalDirection = "right";
			super.direction = 0;
			if (x < (panel.getWidth() - (width / 2))) {
				deltaX += speed;
			}
		}

		// Normalize diagonal movement speed
		if (deltaX != 0 && deltaY != 0) {
			float diagonalFactor = (float) Math.sqrt(2);
			deltaX /= diagonalFactor;
			deltaY /= diagonalFactor;
		}

		// Apply movement
		x += deltaX;
		y += deltaY;
	}

	private void collectNearbyAuras() {
		for (Pickup aura : panel.getMoney()) {
			if (distanceTo(aura) < pickupRange) {
				aura.collect(this);
			}
		}
	}

	private void checkCollisionWithEnemies() {
		for (GameObject enemy : panel.getEnemies()) {
			if (touching(enemy)) {
				this.iFrames = I_FRAMES;
				enemy.hit(this);
				this.health -= enemy.getContactDamage();
				panel.spawnParticles(this, PARTICLE_DAMAGE, 5, 1.5f);
			}
		}
	}

	private void updateThisAnimation() {
		animationCounter++;

		if (isAttacking) {
			currentFrame = (animationCounter - 1) / FRAMES_PER_SPRITE + 1;
			if (currentFrame > ATTACK_FRAMES) {
				isAttacking = false;
				animationCounter = 0;
			}
		} else if (animationCounter > FRAMES_PER_SPRITE) {
			animationCounter = 0;
			currentFrame = (currentFrame % 10) + 1; // Loops spriteNumber between 1 and 10
		}
	}

	private void updateSprite() {
		if (isAttacking) {
			sprite = LmeleeR[currentFrame - 1];
		} else {
			switch (literalDirection) {
				case "up":
					sprite = keyHandler.upActive ? runBack[currentFrame - 1] : idleB;
					break;
				case "down":
					sprite = keyHandler.downActive ? runForward[currentFrame - 1] : idleF;
					break;
				case "left":
					sprite = keyHandler.leftActive ? runLeft[currentFrame - 1] : idleL;
					break;
				case "right":
					sprite = keyHandler.rightActive ? runRight[currentFrame - 1] : idleR;
					break;
			}
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

	// Upgrade methods

	public void upgradeMaxHealth() {
		this.maxHealth += DEFAULT_MAX_HEALTH / 10; // 10%
	}

	public void upgradeRecovery() {
		this.recovery += DEFAULT_RECOVERY;
	}

	public void upgradeAgility() {
		this.speed += DEFAULT_SPEED / 20; // 5%
	}

	public void upgradeContactDamage() {
		this.contactDamage += 5;
	}

	public void upgradeProjectileDamage() {
		this.projectileDamage += DEFAULT_PROJECTILE_DAMAGE / 5; // 20%
	}

	public void upgradeProjectileSpeed() {
		this.projectileSpeed += DEFAULT_PROJECTILE_SPEED / 10; // 10%
	}

	public void upgradeProjectileSize() {
		this.projectileSize += DEFAULT_PROJECTILE_SIZE / 10; // 10%
	}

	public void upgradeAbilityCooldown() {
		this.abilityCooldown -= DEFAULT_ABILITY_COOLDOWN / 10; // 10%
	}

	public void upgradeProjectileBonus() {
		this.projectileBonus += 1;
	}

	public void upgradePickupRange() {
		this.pickupRange += DEFAULT_PICKUP_RANGE / 4; // 25%
	}

	// Setters and Getters

	public void collectAura(Pickup aura) {
		this.aura += aura.getValue();
	}

	public int getAura() {
		return this.aura;
	}

	public ArrayList<Ability> getAbilities() {
		return this.abilities;
	}

	public void newAbility(Ability ability) {
		abilities.add(ability);
	}

	public void addAura(int i) {
		this.aura += i;
	}

}
