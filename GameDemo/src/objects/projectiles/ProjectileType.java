package objects.projectiles;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import objects.Animation;
import objects.AnimationData;
import objects.GameObject;
import objects.particles.ParticleManager;

public enum ProjectileType implements ProjectileBehaviour, Animation {

    LIGHTNING(1, 5, 10, true, 0.2f, Map.of(
            "default", new AnimationData("lightning/LightningSpear", 6))) {
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
    EARTH(1.5f, 3, 35, true, -1, Map.of(
            "default", new AnimationData("earth/Drill", 3))) {
        @Override
        public void created(Projectile projectile) {
            projectile.vy = -10;
            projectile.vx = (random.nextFloat(-4, 4));
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
    FALCON(1.5f, 0.07f, 10, true, 5, Map.of(
            "default", new AnimationData("falcon/Falcon", 1),
            "break", new AnimationData("falcon/FalconBreak", 3))) {
        @Override
        public void created(Projectile projectile) {
            projectile.angle = random.nextInt(0, 360);
            projectile.radius = 1;
            projectile.centerX = projectile.getCreator().getX();
            projectile.centerY = projectile.getCreator().getY();
        }

        @Override
        public void update(Projectile projectile) {
            projectile.angle += projectile.getSpeed();

            if (projectile.radius < 70) {
                projectile.radius += 1;
                projectile.setPosition((int) (projectile.getX() + projectile.vx),
                        (int) (projectile.getY() + projectile.vy));
            }

            if (projectile.angle >= 2 * Math.PI) {
                projectile.angle -= 2 * Math.PI;
            }

            float newX = projectile.centerX + (float) Math.cos(projectile.angle) * projectile.radius;
            float newY = projectile.centerY + (float) Math.sin(projectile.angle) * projectile.radius;

            projectile.setPosition((int) newX, (int) newY);
            projectile.setDirection((float) Math.toDegrees(projectile.angle - 30));
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
            projectile.setAnimation("break", false);
            // projectile.die();
        }
    },
    FIRE(1, 5f, 10, false, 5, Map.of(
            "default", new AnimationData("fireArrow/FireArrowhead", 4))) {
        @Override
        public void created(Projectile projectile) {
            projectile.setAllowedOffscreen(false);
        }

        @Override
        public void update(Projectile projectile) {
            double dx = Math.cos(Math.toRadians(projectile.getDirection())) * speed;
            double dy = Math.sin(Math.toRadians(projectile.getDirection())) * speed;

            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
        }
    },
    BASIC(1, 5f, 20, false, 5, Map.of(
            "default", new AnimationData("chargedArrow/ChargedArrowHold", 4))) {

        @Override
        public void created(Projectile projectile) {
        }

        @Override
        public void update(Projectile projectile) {
            double dx = Math.cos(Math.toRadians(projectile.getDirection())) * speed;
            double dy = Math.sin(Math.toRadians(projectile.getDirection())) * speed;
            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
            // Handle the projectile's collision logic if necessary
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
            // Handle cooldown finishing logic if necessary
        }
    },
    SHARK(1, 5f, 20, false, 5, Map.of(
            "default", new AnimationData("shark/shark/Shark", 9))) {
        @Override
        public void created(Projectile projectile) {
            projectile.setAnimation("default", false);
        }

        @Override
        public void update(Projectile projectile) {
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
    protected float cooldown;

    Random random = new Random();
    ParticleManager particleManager = new ParticleManager();
    protected boolean animated = true;
    protected HashMap<String, BufferedImage[]> animations = new HashMap<>();

    // Constructor
    ProjectileType(float size, float speed, int damage, boolean canPierce, float cooldown,
            Map<String, AnimationData> animationDataMap) {
        this.size = size;
        this.speed = speed;
        this.contactDamage = damage;
        this.canPierce = canPierce;
        this.cooldown = cooldown * 60;

        for (Map.Entry<String, AnimationData> entry : animationDataMap.entrySet()) {
            loadAnimation(entry.getKey(), entry.getValue().getPath(), entry.getValue().getLength());
        }

        this.width = animations.get("default")[0].getWidth();
        this.height = animations.get("default")[0].getHeight();
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
        animations.put(name, loadedFrames);
    }

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
        return contactDamage;
    }

    public boolean canPierce() {
        return canPierce;
    }

    public float getCooldown() {
        return cooldown;
    }

    public boolean isAnimated() {
        return animated;
    }
}
