package objects;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Projectile extends GameObject {

    private float direction;
    private boolean canPierce;
    private ProjectileType type;

    private GameObject creator;
    private int spriteNumber = 1;

    Projectile(Entity creator, float direction, ProjectileType projectileType) {
        super(creator.x, creator.y, (int) (projectileType.size * creator.getProjectileDamage()),
                (int) (projectileType.size * creator.getProjectileSize()));

        this.creator = creator;
        this.type = projectileType;
        this.contactDamage = type.getContactDamage() * creator.getProjectileDamage();
        this.canPierce = type.canPierce();
        this.direction = direction;
    }

    public void update() {
        type.update(this);
        if (type.animationLength != 0) {
            spriteNumber++;
            if (spriteNumber > type.getAnimationLength()) {
                spriteNumber = 1;
            }
        }
        setSprite();
    }

    public void checkPositionOnScreen(int screenWidth, int screenHeight) {
        if (this.x < 0 || this.x > screenWidth || this.y < 0 || this.y > screenHeight) {
            this.isDead = true;
        }
    }

    private void setSprite() {
        try {
            this.sprite = ImageIO
                    .read(getClass()
                            .getResourceAsStream("/res/projectiles/" + type.getSpritePath() + spriteNumber + ".png"));
        } catch (IOException e) {
            System.out.println("Couldn't load sprite");
            this.sprite = null;
        }
    }

    public void hit() {
        if (!this.canPierce) {
            this.isDead = true;
        }
    }

    public float getDirection() {
        return this.direction;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
