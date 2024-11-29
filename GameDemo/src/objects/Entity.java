package objects;

public class Entity extends GameObject {
	String direction;
	int spriteCounter = 0; // Counts frames for animation timing
	// int spriteIndex = 0;
	boolean attacking = false;
	boolean attacking2 = false;

	public int health;
	public int damage;
	public int range;
	public float speed;

	// Constructor
	public Entity(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.health = 100;
	}

}