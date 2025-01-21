package objects.projectiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import objects.Animation;
import objects.GameObject;

/**
 * A type of projectile
 */
public enum ProjectileType implements ProjectileBehaviour {

    LIGHTNING(1, 5, 10, true, 0.2f, new ArrayList<>(List.of(
            new Animation("default", "JarOfLightning/LightningSpear", 6)))) {
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
    ROCK(1.5f, 3, 35, true, -1, new ArrayList<>(List.of(
            new Animation("default", "Rock/Rock", 1)))) {
        @Override
        public void created(Projectile projectile) {
            projectile.vy = -10;
            projectile.vx = (random.nextFloat(-4, 4));

        }

        @Override
        public void update(Projectile projectile) {
            float GRAVITY = 0.2f;
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
    FALCON(1.5f, 0.07f, 10, true, 5, new ArrayList<>(List.of(
            new Animation("default", "falcon/Falcon", 1),
            new Animation("break", "falcon/FalconBreak", 3)))) {
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
    FIRE(1, 5f, 25, true, 5, new ArrayList<>(List.of(
            new Animation("default", "MaskOfFlames/FireArrowhead", 4)))) {

        @Override
        public void created(Projectile projectile) {
        }

        @Override
        public void update(Projectile projectile) {
            double dx = Math.cos(Math.toRadians(projectile.getDirection())) * speed;
            double dy = Math.sin(Math.toRadians(projectile.getDirection())) * speed;

            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));

            if (projectile.isOffscreen()) {
                projectile.die();
            }
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
        }
    },
    BASIC(1, 5f, 30, false, 5, new ArrayList<>(List.of(
            new Animation("default", "chargedArrow/ChargedArrowHold", 4)))) {

        @Override
        public void created(Projectile projectile) {
        }

        @Override
        public void update(Projectile projectile) {
            double dx = Math.cos(Math.toRadians(projectile.getDirection())) * speed;
            double dy = Math.sin(Math.toRadians(projectile.getDirection())) * speed;
            projectile.setPosition((int) (projectile.getX() + dx), (int) (projectile.getY() + dy));

            if (projectile.isOffscreen()) {
                projectile.die();
            }
        }

        @Override
        public void hit(Projectile projectile, GameObject other) {
            // Handle the projectile's collision logic if necessary
        }

        @Override
        public void cooldownFinished(Projectile projectile) {
            // Handle cooldown finishing logic if necessary
        }
    };

    protected float size;
    protected int width, height;
    protected float speed;
    protected float contactDamage;
    protected boolean canPierce;
    protected float cooldown;

    Random random = new Random();
    protected boolean animated = true;
    protected ArrayList<Animation> animations = new ArrayList<>();

    // Constructor
    ProjectileType(float size, float speed, int damage, boolean canPierce, float cooldown,
            ArrayList<Animation> animationsList) {
        this.size = size;
        this.speed = speed;
        this.contactDamage = damage;
        this.canPierce = canPierce;
        this.cooldown = cooldown * 60;

        for (Animation entry : animationsList) {
            Animation animation = new Animation(entry.getName(), "projectiles/" + entry.getPath(), entry.getLength());
            animations.add(animation);
            animation.load();
        }

        this.width = animations.get(0).getFrame(0).getWidth();
        this.height = animations.get(0).getFrame(0).getHeight();
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

    public ArrayList<Animation> getAnimations() {
        return this.animations;
    }

    public boolean isAnimated() {
        return animated;
    }
}
