package objects.projectiles;

import objects.GameObject;

public class Projectile extends GameObject {

    private boolean canPierce;
    private ProjectileType type;
    private int cooldown;

    private final GameObject creator;

    public Projectile(GameObject creator, float directionLiteral, ProjectileType projectileType) {
        super(creator.getX(), creator.getY(), (int) (projectileType.getSize() * creator.getProjectileDamage()),
                (int) (projectileType.getSize() * creator.getProjectileSize()));

        this.creator = creator;
        this.type = projectileType;
        this.canPierce = type.canPierce();

        this.frameCounter = FRAMES_PER_SPRITE;
        
        if (cooldown == 0) this.cooldown = (int) type.getCooldown();
        this.directionLiteral = directionLiteral;

        this.contactDamage = type.getContactDamage() * creator.getProjectileDamage();
        this.speed = type.getSpeed() * creator.getProjectileSpeed();

        this.spritePath = "projectiles/" + type.getSpritePath() + this.spriteIterator;
        type.created(this);
    }

    @Override
    public void update() {

        type.update(this);

        // projectile cooldown
        if (cooldown != -1) {
            this.cooldown--;
            if (cooldown == 0) {
                type.cooldownFinished(this);
                this.cooldown = (int) type.getCooldown();
            }
        }

        // projectile animation
        if (type.getAnimationLength() != -1) {
            updateAnimation(type.getAnimationLength()); 
            this.spritePath = "projectiles/" + type.getSpritePath() + this.spriteIterator;
            setSprite(spritePath);
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
