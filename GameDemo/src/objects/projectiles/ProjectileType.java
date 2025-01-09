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

    LIGHTNING(1, 5, 20, true, .2f, "lightning/LightningSpear", 6) {

        @Override
        public void created(Projectile projectile) {
        }

        @Override
        public void update(Projectile projectile) {
            int[] directions = { -15, 0, 15 };
            projectile.setDirection(projectile.getDirection() + directions[random.nextInt(directions.length)]);

            double dx = Math.cos(Math.toRadians(projectile.getDirection())) * speed;
            double dy = Math.sin(Math.toRadians(projectile.getDirection())) * speed;
            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));

            projectile.setContactDamage(contactDamage * (projectile.getWidth() / (float) width));
            if (projectile.getWidth() <= 6) {
                projectile.die();
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
    EARTH(1.5f, 3, 35, true, -1, "earth/Drill", 3) {

        @Override
        public void created(Projectile projectile) {
            // Initial upward velocity, simulate launch
            projectile.vy = -10; // Negative value to launch upwards
            projectile.vx = (random.nextFloat(-4, 4)); // Random slight horizontal velocity (-2 or 2)
        }

        @Override
        public void update(Projectile projectile) {
            projectile.vy += GRAVITY * this.speed;

            projectile.setPosition((int) (projectile.getX() + projectile.vx),
                    (int) (projectile.getY() + projectile.vy));

            projectile.setDirection((float) Math.toDegrees(Math.atan2(projectile.vy, projectile.vx)));
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
        }
    },
    FALCON(1.5f, 0.05f, 20, true, 5, "falcon/Falcon") {

        @Override
        public void created(Projectile projectile) {
            // Initial circular motion parameters
            projectile.angle = random.nextInt(0, 360); // Starting angle (in radians)
            projectile.radius = 70; // Radius of the circular path
            // Calculate new X and Y positions based on circular motion equations
            projectile.centerX = projectile.getCreator().getX(); // The X position of the creator
            projectile.centerY = projectile.getCreator().getY(); // The Y position of the creator
        }

        @Override
        public void update(Projectile projectile) {
            // Increment the angle to create smooth circular motion
            projectile.angle += projectile.getSpeed();

            // Ensure the angle stays within 0 to 2*pi radians for smooth circular motion
            if (projectile.angle >= 2 * Math.PI) {
                projectile.angle -= 2 * Math.PI;
            }

            float newX = projectile.centerX + (float) Math.cos(projectile.angle) * projectile.radius;
            float newY = projectile.centerY + (float) Math.sin(projectile.angle) * projectile.radius;

            // Update projectile's position
            projectile.setPosition((int) newX, (int) newY);

            // Optional: Adjust the direction based on the current angle for smooth rotation
            projectile.setDirection((float) Math.toDegrees(projectile.angle - 30));
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
            projectile.die();
        }
    },
    FIRE(1, 2.5f, 20, false, 5, "fireArrow/FireArrowhead", 4) {

        @Override
        public void created(Projectile projectile) {
        }

        @Override
        public void update(Projectile projectile) {
            // Calculate the movement using direction and speed
            double dx = Math.cos(Math.toRadians(projectile.getDirection())) * speed;
            double dy = Math.sin(Math.toRadians(projectile.getDirection())) * speed;

            // Update projectile's position
            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
        }
    };

    final float GRAVITY = 0.2f;
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
        this.width = this.animations.get("default")[0].getWidth();
        this.height = this.animations.get("default")[0].getHeight();
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
