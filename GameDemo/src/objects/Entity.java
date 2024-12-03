package objects;

public class Entity extends GameObject {
	String direction;
	protected float directionLiteral; // temporary

	boolean isAttacking = false;

	protected float projectileDamage;
	protected float projectileSpeed;
	protected float projectileSize;

	// Constructor
	public Entity(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public float getProjectileDamage() {
		return this.projectileDamage;
	}

	public float getProjectileSpeed() {
		return this.projectileSpeed;
	}

	public float getProjectileSize() {
		return this.projectileSize;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

}