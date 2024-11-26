package objects;

import java.util.ArrayList;

public class Enemy extends Entity {

    GameObject target;

    public Enemy(int x, int y, int width, int height, GameObject target) {
        super(x, y, width, height);
        this.target = target;
        this.speed = (float) 2;
    }

    public void update(double delta, ArrayList<Enemy> enemies) {
        double distance = distanceTo(target); // Calculate distance once

        if (distance > 1) {
            // Calculate direction vector and normalize
            double directionX = (target.x - x) / distance;
            double directionY = (target.y - y) / distance;

            // Update position based on speed
            double newX = x + directionX * speed * delta;
            double newY = y + directionY * speed * delta;

            // Check for collisions with other enemies
            for (Enemy other : enemies) {
                if (other != this && touching(other)) {
                    // Calculate angle of repulsion
                    double angle = Math.atan2(other.y - y, other.x - x);

                    // Move away from the collision
                    newX -= Math.cos(angle) * speed * delta;
                    newY -= Math.sin(angle) * speed * delta;
                }
            }

            // Update the position once
            x = (int) newX;
            y = (int) newY;
        }
        else health -= 10;
    }

}
