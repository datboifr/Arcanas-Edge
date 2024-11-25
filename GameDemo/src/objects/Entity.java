package objects;

public class Entity extends GameObject {
	float speed;
	String direction;
	int spriteCounter = 0; // Counts frames for animation timing
	int spriteIndex = 0;
	boolean attacking = false;

	// Constructor
	public Entity(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

}