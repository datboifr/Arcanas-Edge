package objects.projectiles;

import java.awt.Color;
import java.awt.PageAttributes;
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
            float jitter = projectile.getdirectionLiteral() + directions[random.nextInt(directions.length)];
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
