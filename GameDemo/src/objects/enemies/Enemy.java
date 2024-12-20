package objects.enemies;

import java.util.ArrayList;
import objects.GameObject;
import objects.projectiles.Projectile;

public class Enemy extends GameObject {

    GameObject target;
    private final int I_FRAMES = 5;
    int iFrames = I_FRAMES;

    public Enemy(int x, int y, int width, int height, GameObject target) {
        super(x, y, width, height);
        this.target = target;

        this.health = 100;
        this.maxHealth = 100;
        this.contactDamage = 1;
        this.speed = 2;

    }

    public void update(double delta, ArrayList<Enemy> enemies, ArrayList<Projectile> projectiles) {
        iFrames--;
        if (this.health <= 0) {
            this.isDead = true;
        } else {
            for (Projectile projectile : projectiles) {
                if (isTouching(projectile)) {
                    if (this.iFrames <= 0) {
                        projectile.hit();
                        this.health -= projectile.getContactDamage();
                        this.iFrames = I_FRAMES;
                    }
                }
            }

            double distance = distanceTo(target); // Calculate distance once
            if (distance > 1) {
                // Calculate direction vector and normalize
                double directionX = (target.getX() - this.x) / distance;
                double directionY = (target.getY() - this.y) / distance;

                // Update position based on speed
                double newX = x + directionX * this.speed;
                double newY = y + directionY * this.speed;

                // Check for collisions with other enemies
                for (Enemy other : enemies) {
                    if (other != this && isTouching(other)) {
                        // Calculate angle of repulsion
                        double angle = Math.atan2(other.y - y, other.x - x);

                        // Move away from the collision
                        newX -= Math.cos(angle) * this.speed;
                        newY -= Math.sin(angle) * this.speed;
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
