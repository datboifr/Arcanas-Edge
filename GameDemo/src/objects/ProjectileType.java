package objects;

import java.util.Random;

public enum ProjectileType implements ProjectileBehaviour {

    LIGHTNING(20, 5, 50, true, "lightning/Lightning", 5) {
        @Override
        public void update(Projectile projectile) {
            double dx = Math.cos(Math.toRadians(projectile.getDirection() + random.nextInt(-25, 25))) * this.speed;
            double dy = Math.sin(Math.toRadians(projectile.getDirection() + random.nextInt(-25, 25))) * this.speed;
            projectile.setSize(0.9f);
            if (projectile.getWidth() <= 1) projectile.isDead = true;
            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));
        }
    },

    STRUCTURE_TRAP(100, 10, 50, true, "lightning/Lightning", 5) {
        @Override
        public void update(Projectile projectile) {
        }
    };

    protected int size;
    protected float speed;
    protected float contactDamage;
    protected boolean canPierce;

    protected String spritePath;
    protected int animationLength;

    Random random = new Random();

    // Constructor
    ProjectileType(int size, float speed, int damage, boolean canPierce, String spritePath, int animationLength) {
        this.size = size;
        this.speed = speed;
        this.contactDamage = damage;
        this.canPierce = canPierce;
        this.spritePath = spritePath;
        this.animationLength = animationLength;
    }

    // Getters

    public int getSize() {
        return size;
    }

    public float getSpeed() {
        return speed;
    }

    public float getContactDamage() {
        return this.contactDamage;
    }

    public boolean canPierce() {
        return this.canPierce;
    }

    public String getSpritePath() {
        return this.spritePath;
    }

    public int getAnimationLength() {
        return this.animationLength;
    }
}
