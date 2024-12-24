package objects;

import combat.Ability;
import combat.AbilityTypes;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.KeyHandler;
import objects.projectiles.Projectile;

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

	private Ability[] abilities;

	private int currentAura;
	private int requiredAura;
	private int level;

	private final float DEFAULT_HEALTH = 100;
	private final float DEFAULT_SPEED = 4;

	private final float DEFAULT_CONTACT_DAMAGE = 0;
	private final float DEFAULT_ABILITY_COOLDOWN = 1;

	private final float DEFAULT_PROJECTILE_DAMAGE = 1;
	private final float DEFAULT_PROJECTILE_SPEED = 1;
	private final float DEFAULT_PROJECTILE_SIZE = 1;

	ArrayList<Projectile> projectiles;
	ArrayList<Aura> aura;

	// Constructor
	public Player(int x, int y, int width, int height, KeyHandler keyHandler, ArrayList<Projectile> projectiles,
			ArrayList<Aura> aura) {
		super(x, y, width, height);
		literalDirection = "down";
		this.keyHandler = keyHandler;

		this.level = 1;
		this.currentAura = 0;
		this.requiredAura = 5;

		this.abilities = new Ability[3];
		abilities[0] = AbilityTypes.electric;
		abilities[1] = AbilityTypes.falcon;
		abilities[2] = AbilityTypes.earth;

		this.health = DEFAULT_HEALTH;
		this.speed = DEFAULT_SPEED;

		this.contactDamage = DEFAULT_CONTACT_DAMAGE;
		this.abilityCooldown = DEFAULT_ABILITY_COOLDOWN;

		this.projectileDamage = DEFAULT_PROJECTILE_DAMAGE;
		this.projectileSpeed = DEFAULT_PROJECTILE_SPEED;
		this.projectileSize = DEFAULT_PROJECTILE_SIZE;
		this.projectileBonus = 0;

		this.projectiles = projectiles;
		this.aura = aura;

		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/res/player/idleFront.png"));
		} catch (IOException e) {
		}

		this.currentFrame = 1;
		loadPlayerImages();
	}

	public void update() {

		if (health < 0) {
			die();
		} else {

			if (currentAura >= requiredAura) {
				levelUp();
			}

			handleMovement();

			for (Aura aura : aura) {
				if (distanceTo(aura) < 50) {
					aura.collect(this);
				}
			}

			updateThisAnimation();
			updateSprite();
			for (Ability ability : abilities) {
				if (ability != null) {
					ability.update(this, projectiles);
				}
			}
		}

	}

	public void levelUp() {
		level++;
		this.currentAura = 0;
		requiredAura = 5 * level;
	}

	public void hit(GameObject other) {
		this.health -= other.getContactDamage();
	}

	@SuppressWarnings("unused")
	private boolean isMoving() {
		return keyHandler.upActive || keyHandler.downActive || keyHandler.leftActive || keyHandler.rightActive
				|| keyHandler.upRightPressed || keyHandler.upLeftPressed;
	}

	private boolean inputDetected() {
		return keyHandler.aActive || keyHandler.bActive || keyHandler.cActive;
	}

	private void handleMovement() {
		if (keyHandler.upActive) {
			literalDirection = "up";
			super.direction = 270;
			y -= speed;
		} else if (keyHandler.downActive) {
			literalDirection = "down";
			super.direction = 90;
			y += speed;
		} else if (keyHandler.leftActive) {
			literalDirection = "left";
			super.direction = 180;
			x -= speed;
		} else if (keyHandler.rightActive) {
			literalDirection = "right";
			super.direction = 0;
			x += speed;
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
		if (isAttacking) // add switch statement for directions later
			sprite = LmeleeR[currentFrame - 1];
		else
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
		this.contactDamage += 5; // increases by 5
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

	public void upgradeAbilityCooldown() {
		this.abilityCooldown -= DEFAULT_ABILITY_COOLDOWN / 20; // decreases by 5%
		System.out.println("Additional Projectile");
	}

	public void upgradeProjectileBonus() {
		this.projectileBonus += 1; // 1 more projectile
		System.out.println("Additional Projectile");
	}

	public Ability[] getAbilities() {
		return this.abilities;
	}

	public void collectAura(Aura aura) {
		this.currentAura += aura.getValue();
	}

	// getters

	public int getLevel() {
		return this.level;
	}

	public int getCurrentAura() {
		return this.currentAura;
	}

	public int getRequiredAura() {
		return this.requiredAura;
	}

}
