package objects.enemies;

public enum EnemyType {

    BASIC(25, 100, 2, 5);

    protected int width;
    protected int height;

    protected int health;
    protected int speed;
    protected int contactDamage;

    protected String spritePath;
    protected int animationLength;

    EnemyType(int size, int health, int speed, int contactDamage) {
    
    }

    
}
