package objects;

public class Entity extends GameObject {
	String direction;
	protected float directionLiteral;
	int spriteCounter = 0; // Counts frames for animation timing
	// int spriteIndex = 0;
	boolean isAttacking = false;
	boolean attacking2 = false;

	protected float health;
	protected float speed;
	protected float strength;
	protected float projectileSpeed;

	// Constructor
	public Entity(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public float getHealth() {
		return this.health;
	}

	public float getSpeed() {
		return this.speed;
	}

	public float getStrength() {
		return this.strength;
	}

	public float getProjectileSpeed() {
		return this.projectileSpeed;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

}