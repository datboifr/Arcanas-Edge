package objects.enemies;

import java.awt.Color;

import main.GamePanel;
import objects.Aura;
import objects.GameObject;
import objects.projectiles.Projectile;

public class Enemy extends GameObject {

    GameObject target;

    public Enemy(GamePanel gp, int x, int y, EnemyType type, GameObject target) {
        super(x, y, type.getWidth(), type.getHeight());
        this.target = target;
        this.panel = gp;

        this.maxHealth = type.getHealth();
        this.health = maxHealth;
        this.contactDamage = type.getContactDamage();
        this.speed = type.getSpeed();
    }

    public void update() {
        iFrames--;
        if (health <= 0) {
            die();
            panel.spawnParticles(this, PARTICLE_DAMAGE, 5, 1);
            panel.getAura().add(new Aura(x, y));
        } else {
            for (Projectile projectile : panel.getProjectiles()) {
                if (touching(projectile)) {
                    if (iFrames <= 0) {
                        projectile.hit();
                        this.health -= projectile.getContactDamage();
                        this.iFrames = I_FRAMES;
                        panel.getParticleManager().spawn(
                                projectile.getX(),
                                projectile.getY(),
                                1,
                                Color.BLACK,
                                10,
                                2,
                                80,
                                0f,
                                (int) projectile.getContactDamage());
                    }
                }
            }

            double distance = distanceTo(target); // Calculate distance once
            if (distance > 1) {
                // Calculate direction vector and normalize
                double directionX = (target.getX() - x) / distance;
                double directionY = (target.getY() - y) / distance;

                // Update position based on speed
                double newX = x + directionX * speed;
                double newY = y + directionY * speed;

                // Check for collisions with other enemies
                for (Enemy other : panel.getEnemies()) {
                    if (other != this && touching(other)) {
                        // Calculate angle of repulsion
                        double angle = Math.atan2(other.y - y, other.x - x);

                        // Move away from the collision
                        newX -= Math.cos(angle) * speed;
                        newY -= Math.sin(angle) * speed;
                    }
                }

                // Update the position once
                this.x = (int) newX;
                this.y = (int) newY;
            }
        }
    }

}
