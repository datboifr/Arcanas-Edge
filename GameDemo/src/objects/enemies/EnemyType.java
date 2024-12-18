package objects.enemies;

public enum EnemyType {

    BASIC(25, 100, 2, 5, "monster.png");

    protected int width;
    protected int height;

    protected int health;
    protected int speed;
    protected int contactDamage;

    protected String spritePath;
    protected int animationLength = -1;

    EnemyType(int size, int health, int speed, int contactDamage, String spritePath) {
        this.width = size;
        this.height = size;

        this.health = health;
        this.speed = speed;
        this.contactDamage = contactDamage;

        this.spritePath = spritePath;
    }

    EnemyType(int size, int health, int speed, int contactDamage, String spritePath, int animationLength) {
        this.width = size;
        this.height = size;

        this.health = health;
        this.speed = speed;
        this.contactDamage = contactDamage;

        this.spritePath = spritePath;
        this.animationLength = animationLength;
    }

    // Getters

    public float getSpeed() {
        return speed;
    }

    public float getContactDamage() {
        return this.contactDamage;
    }

    public String getSpritePath() {
        return this.spritePath;
    }

    public int getAnimationLength() {
        return this.animationLength;
    }

}
