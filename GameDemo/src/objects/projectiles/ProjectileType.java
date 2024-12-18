package objects.projectiles;

import objects.GameObject;

public enum ProjectileType implements ProjectileBehaviour {

    LIGHTNING(20, 5, 10, true, .2f, "lightning/Lightning", 5) {

        @Override
        public void created(Projectile projectile) {
        }

        @Override
        public void update(Projectile projectile) {
            double dx = Math.cos(Math.toRadians(projectile.getdirectionLiteral())) * this.speed;
            double dy = Math.sin(Math.toRadians(projectile.getdirectionLiteral())) * this.speed;
            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));

            projectile.setContactDamage(this.contactDamage * (projectile.getWidth() / (float) this.size));
            if (projectile.getWidth() <= 5)
                projectile.setState(true);
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
            projectile.setSize(0.5f);
        }
    },
    EARTH(40, 7, 10, true, -1, "earth/Drill", 3) {

        @Override
        public void created(Projectile projectile) {
        }

        @Override
        public void update(Projectile projectile) {
            double dx = Math.cos(Math.toRadians(projectile.getdirectionLiteral())) * this.speed;
            double dy = Math.sin(Math.toRadians(projectile.getdirectionLiteral())) * this.speed;
            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
        }
    };

    protected int size;
    protected float speed;
    protected float contactDamage;
    protected boolean canPierce;
    protected float cooldown; // in seconds

    protected String spritePath;
    protected int animationLength = -1;

    // Constructor
    ProjectileType(int size, float speed, int damage, boolean canPierce, float cooldown, String spritePath,
            int animationLength) {
        this.size = size;
        this.speed = speed;
        this.contactDamage = damage;
        this.canPierce = canPierce;

        this.cooldown = cooldown * 60; // seconds * fps = frames

        this.spritePath = spritePath;
        this.animationLength = animationLength;
    }

    // Constructor, no animation
    ProjectileType(int size, float speed, int damage, boolean canPierce, float cooldown, String spritePath) {
        this.size = size;
        this.speed = speed;
        this.contactDamage = damage;
        this.canPierce = canPierce;
        this.spritePath = spritePath;
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

    public float getCooldown() {
        return this.cooldown;
    }

    public String getSpritePath() {
        return this.spritePath;
    }

    public int getAnimationLength() {
        return this.animationLength;
    }
}
