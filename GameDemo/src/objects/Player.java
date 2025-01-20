package objects;

import combat.Ability;
import combat.AbilityTypes;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends GameObject {

	private String literalDirection;
	private final int RUNNING_FRAMES = 10;
	private final int ATTACK_FRAMES = 9;
	private BufferedImage idleB, idleF, idleR, idleL;

	KeyHandler keyHandler;

	private ArrayList<Ability> abilities;

	private int money;
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
		this.keyHandler = gp.getKeyHandler();
		this.money = 0;
		this.pickupRange = DEFAULT_PICKUP_RANGE;

		this.abilities = new ArrayList<>();
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
		this.projectileBonus = 0;

		this.hasShadow = true;
		this.currentFrame = 1;
		loadPlayerImages();
		literalDirection = "down";
		setAnimation("rundown", true);
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

			if (moving()) {
				updateAnimation();
			}
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
		String lastDirection = literalDirection; // Keep track of the last direction

		if (moving()) {
			if (keyHandler.upActive) {
				deltaY -= speed;
				literalDirection = "up";
				super.direction = 270;
			}
			if (keyHandler.downActive) {
				deltaY += speed;
				literalDirection = "down";
				super.direction = 90;
			}
			if (keyHandler.leftActive) {
				deltaX -= speed;
				literalDirection = "left";
				super.direction = 180;
			}
			if (keyHandler.rightActive) {
				deltaX += speed;
				literalDirection = "right";
				super.direction = 0;
			}

			// Normalize diagonal movement speed
			if (deltaX != 0 && deltaY != 0) {
				float diagonalFactor = (float) Math.sqrt(2);
				deltaX /= diagonalFactor;
				deltaY /= diagonalFactor;
			}

			// Update position
			x += deltaX;
			y += deltaY;
		}

		// Handle animation
		if (moving()) {
			if (!lastDirection.equals(literalDirection)) {
				setAnimation("run" + literalDirection, true);
			}
		} else {
			// No keys are active, switch to idle animation
			switch (literalDirection) {
				case "up":
					sprite = idleB;
					break;
				case "down":
					sprite = idleF;
					break;
				case "left":
					sprite = idleL;
					break;
				case "right":
					sprite = idleR;
					break;
			}
		}
	}

	private boolean moving() {
		return keyHandler.upActive || keyHandler.downActive || keyHandler.leftActive
				|| keyHandler.rightActive;
	}

	private void collectNearbyAuras() {
		for (Pickup aura : panel.getPickups()) {
			if (distanceTo(aura) < pickupRange) {
				aura.setTarget(this);
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

	public void loadPlayerImages() {
		try {
			animations.add(new Animation("runup", "player/run/back/RunB", RUNNING_FRAMES));
			animations.add(new Animation("rundown", "player/run/front/RunF", RUNNING_FRAMES));
			animations.add(new Animation("runleft", "player/run/left/RunL", RUNNING_FRAMES));
			animations.add(new Animation("runright", "player/run/right/RunR", RUNNING_FRAMES));

			for (Animation anim : animations) {
				anim.load();
			}

			// idle sprites
			idleB = ImageIO.read(getClass().getResourceAsStream("/res/player/idleBack.png"));
			idleF = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
			idleR = ImageIO.read(getClass().getResourceAsStream("/res/player/idleRight.png"));
			idleL = ImageIO.read(getClass().getResourceAsStream("/res/player/idleLeft.png"));

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
		this.projectileDamage += DEFAULT_PROJECTILE_DAMAGE / 2; // 50%
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

	public void collectPickup(Pickup pickup) {
		this.money += pickup.getValue();
		if (pickup.doAction() != null) {
			pickup.doAction();
		}
	}

	public int getMoney() {
		return this.money;
	}

	public ArrayList<Ability> getAbilities() {
		return this.abilities;
	}

	public void newAbility(Ability ability) {
		abilities.add(ability);
	}

	public void addMoney(int i) {
		this.money += i;
	}

}
