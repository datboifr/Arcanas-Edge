package objects;

import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Enemy extends Entity {

    Object target;

    public Enemy(int x, int y, int width, int height, Object target) {
        super(x, y, width, height);
        this.target = target;
        this.speed = (float) 2;
        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/res/enemies/monster.png"));
        } catch (IOException ex) {
        }
    }

    public void update(double delta, ArrayList<Enemy> enemies) {

        if (! touching(target)) {
            // Calculate direction vector
            float directionX = target.x - x;
            float directionY = target.y - y;

            // Normalize direction and calculate new position
            int newX = (int) (x + (directionX / distanceTo(target)) * speed);
            int newY = (int) (y + (directionY / distanceTo(target)) * speed);

            // Check for collisions with other enemies
            for (Enemy other : enemies) {
                if (other != this && touching(other)) {
                    // Move the current enemy away from the other enemy
                    double angle = Math.atan2(other.y - newY, other.x - newX);
                    newX -= (int) (Math.cos(angle) * speed);
                    newY -= (int) (Math.sin(angle) * speed);
                }
            }

            // Update the position once
            x = newX;
            y = newY;
        }
    }

}
