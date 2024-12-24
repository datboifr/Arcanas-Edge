package objects.enemies;

import java.awt.Color;
import java.util.ArrayList;

import objects.Aura;
import objects.GameObject;
import objects.particles.ParticleManager;
import objects.projectiles.Projectile;

public class Enemy extends GameObject {

    GameObject target;

    ParticleManager particleManager;
    ArrayList<Projectile> projectiles;
    ArrayList<Aura> aura;
    ArrayList<Enemy> enemies;

    public Enemy(int x, int y, int width, int height, GameObject target, ParticleManager particleManager,
            ArrayList<Projectile> projectiles, ArrayList<Aura> aura, ArrayList<Enemy> enemies) {
        super(x, y, width, height);
        this.target = target;
        this.particleManager = particleManager;
        this.projectiles = projectiles;
        this.aura = aura;
        this.enemies = enemies;

        this.maxHealth = 20;
        this.health = maxHealth;
        this.contactDamage = 1;
        this.speed = 2;
    }

    public void update(double delta) {
        iFrames--;
        if (health <= 0) {
            die();
            particleManager.spawn(
                    x,
                    y,
                    10,
                    deathColor,
                    8,
                    2,
                    30,
                    0f);
            aura.add(new Aura(x, y));
        } else {
            for (Projectile projectile : projectiles) {
                if (isTouching(projectile)) {
                    if (iFrames <= 0) {
                        projectile.hit();
                        this.health -= projectile.getContactDamage();
                        this.iFrames = I_FRAMES;
                        particleManager.spawn(
                                x,
                                y,
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
                for (Enemy other : enemies) {
                    if (other != this && isTouching(other)) {
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
            } else {
                target.hit(this);
            }
        }
    }

}
