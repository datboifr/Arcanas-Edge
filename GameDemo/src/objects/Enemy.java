package objects;

import java.util.ArrayList;

public class Enemy extends Entity {

    GameObject target;

    public Enemy(int x, int y, int width, int height, GameObject target) {
        super(x, y, width, height);
        this.target = target;

        this.health = 100;
        this.speed = 2;
        this.strength = 5;
    }

    public void update(double delta, ArrayList<Enemy> enemies) {
        double distance = distanceTo(target); // Calculate distance once

        if (distance > 1) {
            // Calculate direction vector and normalize
            double directionX = (target.x - this.x) / distance;
            double directionY = (target.y - this.y) / distance;

            // Update position based on speed
            double newX = x + directionX * this.speed * delta;
            double newY = y + directionY * this.speed * delta;

            // Check for collisions with other enemies
            for (Enemy other : enemies) {
                if (other != this && touching(other)) {
                    // Calculate angle of repulsion
                    double angle = Math.atan2(other.y - y, other.x - x);

                    // Move away from the collision
                    newX -= Math.cos(angle) * this.speed * delta;
                    newY -= Math.sin(angle) * this.speed * delta;
                }
            }

            // Update the position once
            this.x = (int) newX;
            this.y = (int) newY;
        } else
            this.health -= 10;
    }

}
