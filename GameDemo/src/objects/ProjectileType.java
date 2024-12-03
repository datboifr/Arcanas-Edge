package objects;

public enum ProjectileType implements ProjectileBehaviour {

    LIGHTNING(20, 10, 50, true, "lightning/Lightning", 5) {
        @Override
        public void update(Projectile projectile) {
            double dx = Math.cos(Math.toRadians(projectile.getDirection())) * this.speed;
            double dy = Math.sin(Math.toRadians(projectile.getDirection())) * this.speed;
            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));
        }
    };

    protected final int size; // Projectile size
    protected final float speed; // Projectile speed
    protected final float contactDamage;
    protected final boolean canPierce;
    protected final String spritePath;
    protected final int animationLength;

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
