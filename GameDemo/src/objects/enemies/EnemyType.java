package objects.enemies;

public class EnemyType {

    protected int width;
    protected int height;

    protected float health;
    protected float speed;
    protected float contactDamage;

    protected String spritePath;
    protected int animationLength = -1;

    EnemyType(int size, float health, float speed, float contactDamage, String spritePath) {
        this.width = size;
        this.height = size;

        this.health = health;
        this.speed = speed;
        this.contactDamage = contactDamage;

        this.spritePath = spritePath;
    }

    EnemyType(int size, float health, float speed, float contactDamage, String spritePath, int animationLength) {
        this.width = size;
        this.height = size;

        this.health = health;
        this.speed = speed;
        this.contactDamage = contactDamage;

        this.spritePath = spritePath;
        this.animationLength = animationLength;
    }

    // Getters

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getHealth() {
        return this.health;
    }

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
