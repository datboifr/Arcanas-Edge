package objects.enemies;

import java.awt.Color;

import main.GamePanel;
import objects.Aura;
import objects.GameObject;
import objects.projectiles.Projectile;

public class Enemy extends GameObject {

    private static final int PARTICLE_COUNT_ON_DEATH = 5;
    private static final int PARTICLE_LIFESPAN = 80;
    private static final float COLLISION_REPULSION_FACTOR = 0.5f;

    private GameObject target;

    public Enemy(GamePanel gp, int x, int y, EnemyType type, GameObject target, float healthMultiplier) {
        super(x, y, type.getWidth(), type.getHeight());
        this.target = target;
        this.panel = gp;

        this.maxHealth = type.getHealth() * healthMultiplier;
        this.health = maxHealth;
        this.contactDamage = type.getContactDamage();
        this.speed = type.getSpeed();
    }

    @Override
    public void update() {
        if (health <= 0) {
            die();
            panel.spawnParticles(this, PARTICLE_DAMAGE, PARTICLE_COUNT_ON_DEATH, 1);
            panel.getAura().add(new Aura(x, y));
        }

        if (iFrames > 0) {
            iFrames--;
        }

        checkProjectileCollisions();
        moveTowardsTarget();
    }

    private void checkProjectileCollisions() {
        for (Projectile projectile : panel.getProjectiles()) {
            if (touching(projectile) && iFrames <= 0) {
                takeProjectileDamage(projectile);
            }
        }
    }

    private void takeProjectileDamage(Projectile projectile) {
        projectile.hit();
        this.health -= projectile.getContactDamage();
        this.iFrames = I_FRAMES;

        panel.spawnDamageIndicator(this, projectile.getContactDamage());

    }

    private void moveTowardsTarget() {
        // Use the directionTo method to get the angle towards the target
        float angleToTarget = directionTo(target);

        // Ensure angle is always between 0 and 360 degrees
        angleToTarget = (angleToTarget + 360) % 360;

        // Calculate movement components based on the direction angle
        double deltaX = Math.cos(Math.toRadians(angleToTarget)) * speed;
        double deltaY = Math.sin(Math.toRadians(angleToTarget)) * speed;

        // Update the new position based on the calculated movement
        double newX = x + deltaX;
        double newY = y + deltaY;

        // Handle collisions with other enemies
        resolveCollisionsWithEnemies(newX, newY);

        // Set the direction of the enemy to face the target
        setDirection(angleToTarget);
    }

    private void resolveCollisionsWithEnemies(double newX, double newY) {
        for (Enemy other : panel.getEnemies()) {
            if (other == this || !touching(other))
                continue;

            // Calculate repulsion vector
            double angle = Math.atan2(other.y - y, other.x - x);
            newX -= Math.cos(angle) * speed * COLLISION_REPULSION_FACTOR;
            newY -= Math.sin(angle) * speed * COLLISION_REPULSION_FACTOR;
        }

        // Update position after collision resolution
        this.x = (int) newX;
        this.y = (int) newY;
    }
}
