package objects.projectiles;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import javax.imageio.ImageIO;
import objects.Animation;
import objects.GameObject;
import objects.particles.ParticleManager;

public enum ProjectileType implements ProjectileBehaviour, Animation {

    LIGHTNING(1, 5, 10, true, .2f, "lightning/LightningSpear", 6) {

        @Override
        public void created(Projectile projectile) {
        }

        @Override
        public void update(Projectile projectile) {
            int[] directions = { -45, 0, 45 };
            float jitter = projectile.getDirection() + directions[random.nextInt(directions.length)];
            double dx = Math.cos(Math.toRadians(jitter)) * this.speed;
            double dy = Math.sin(Math.toRadians(jitter)) * this.speed;
            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));

            projectile.setContactDamage(this.contactDamage * (projectile.getWidth() / (float) this.size));
            if (projectile.getWidth() <= 6) {
                projectile.setState(true);
            }
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
            projectile.setSize(random.nextFloat(0.3f, 0.5f));
        }
    },
    EARTH(2, 7, 10, true, -1, "earth/Drill", 3) {

        @Override
        public void created(Projectile projectile) {
            // Initial upward velocity, simulate launch
            projectile.vy = -15;  // Negative value to launch upwards
            projectile.xv = 0;    // Optional: Set horizontal velocity to zero if no horizontal movement is needed
            projectile.setDirection(projectile.getDirection() + random.nextInt(-15, 15));
        }
        
        @Override
        public void update(Projectile projectile) {
            // Apply gravity (this will make the projectile fall after initial upward motion)
            projectile.vy += this.GRAVITY;  // Gravity pulls the projectile down

            // Update projectile's position based on horizontal and vertical velocity
            int newX = (int) (projectile.getX() + projectile.xv);  // Horizontal movement
            int newY = (int) (projectile.getY() + projectile.vy);  // Vertical movement with gravity
            projectile.setPosition(newX, newY);  // Update the position of the projectile

            // Calculate the angle based on velocity (in radians)
            double angle = Math.atan2(projectile.vy, projectile.xv);  // atan2 returns the angle in radians
            projectile.setDirection((float)  angle);  // Set the rotation of the projectile

            // Optional: You can add additional checks for screen bounds, collisions, etc.

            // If the projectile should not rotate or have animations, you can skip that logic here.
        }
        
        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
        }
    };

    final float GRAVITY = 1f;
    protected float size;
    protected int width, height;
    protected float speed;
    protected float contactDamage;
    protected boolean canPierce;
    protected float cooldown; // in seconds

    Random random = new Random();
    ParticleManager particleManager = new ParticleManager();
    protected boolean animated = true;
    protected HashMap<String, BufferedImage[]> animations = new HashMap<>();

    // Constructor - animated
    ProjectileType(float size, float speed, int damage, boolean canPierce, float cooldown, String spritePath,
            int animationLength) {
        this.size = size;
        this.speed = speed;
        this.contactDamage = damage;
        this.canPierce = canPierce;

        this.cooldown = cooldown * 60; // seconds * fps = frames

        this.loadAnimation("default", spritePath, animationLength);

        this.width = this.animations.get("default")[0].getWidth();
        this.height = this.animations.get("default")[0].getHeight();
    }

    // Constructor - not animated
    ProjectileType(float size, float speed, int damage, boolean canPierce, float cooldown, String spritePath) {
        this.size = size;
        this.speed = speed;
        this.contactDamage = damage;
        this.canPierce = canPierce;

        this.cooldown = cooldown * 60; // seconds * fps = frames
        this.animated = false;
        this.loadAnimation("default", spritePath, 1);
    }

    @Override
    public void loadAnimation(String name, String path, int animationLength) {
        BufferedImage[] loadedFrames = new BufferedImage[animationLength];
        for (int i = 0; i < animationLength; i++) {
            try {
                loadedFrames[i] = ImageIO
                        .read(getClass().getResourceAsStream("/res/projectiles/" + path + (i + 1) + ".png"));
            } catch (IOException e) {
                System.out.println("Error loading frame: " + path + (i + 1) + ".png");
            }
        }
        this.animations.put(name, loadedFrames);
    }

    // Getters

    public float getSize() {
        return size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public boolean isAnimated() {
        return this.animated;
    }

}
