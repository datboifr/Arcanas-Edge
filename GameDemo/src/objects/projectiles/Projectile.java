package objects.projectiles;

import main.GamePanel;
import objects.GameObject;

public class Projectile extends GameObject {

    private ProjectileType type;
    private int cooldown;
    public float vx, vy;
    public float angle, radius;
    public float angularSpeed;
    public int centerX, centerY;

    private boolean allowedOffScreen = true;
    private boolean active = true;

    private final GameObject creator;

    public Projectile(GameObject creator, float direction, ProjectileType projectileType) {
        super(creator.getX(), creator.getY(),
                (int) ((projectileType.getWidth() * projectileType.getSize()) * creator.getProjectileSize()),
                (int) ((projectileType.getHeight() * projectileType.getSize()) * creator.getProjectileSize()));

        this.creator = creator;
        this.type = projectileType;

        this.contactDamage = type.getContactDamage() * creator.getProjectileDamage();
        this.speed = type.getSpeed() * creator.getProjectileSpeed();

        this.cooldown = (int) type.getCooldown();
        this.direction = direction;
        this.animations = type.animations;
        this.rotates = true;

        this.setAnimation("default", true);
        updateAnimation();
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
        if (!type.canPierce) {
            die();
        }
        type.hit(this, creator);
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public GameObject getCreator() {
        return this.creator;
    }

    public void checkPositionOnScreen(int screenWidth, int screenHeight) {
        if (this.x < 0 || this.x > screenWidth || this.y < 0 || this.y > screenHeight) {
            if (!allowedOffScreen) {
                die();
            }
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setAllowedOffscreen(boolean allowedOffScreen) {
        this.allowedOffScreen = allowedOffScreen;
    }
}
