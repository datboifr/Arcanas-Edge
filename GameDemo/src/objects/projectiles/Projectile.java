package objects.projectiles;

import objects.GameObject;

public class Projectile extends GameObject {

    private ProjectileType type;
    private int cooldown;

    private final GameObject creator;

    public Projectile(GameObject creator, float directionLiteral, ProjectileType projectileType) {
        super(creator.getX(), creator.getY(), (int) ((projectileType.getWidth() * projectileType.getSize()) * creator.getProjectileSize()), 
        (int) ((projectileType.getHeight() * projectileType.getSize()) * creator.getProjectileSize()));

        this.creator = creator;
        this.type = projectileType;
        this.contactDamage = type.getContactDamage() * creator.getProjectileDamage();
        this.speed = type.getSpeed() * creator.getProjectileSpeed();

        this.cooldown = (int) type.getCooldown();
        this.directionLiteral = directionLiteral;
        this.animations = type.animations;

        this.setAnimation("default", true);
        type.created(this);
    }

    @Override
    public void update() {
        type.update(this);

        if (cooldown != -1) {
            this.cooldown--;
            if (cooldown == 0) {
                type.cooldownFinished(this);
                this.cooldown = (int) type.getCooldown();
            }
        }

        if (type.isAnimated()) { 
            updateAnimation(); 
        }
    }

    public void hit() {
        type.hit(this, creator);
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void checkPositionOnScreen(int screenWidth, int screenHeight) {
        if (this.x < 0 || this.x > screenWidth || this.y < 0 || this.y > screenHeight) {
            this.isDead = true;
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
