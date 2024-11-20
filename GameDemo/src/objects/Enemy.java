package objects;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Entity {

    Object target;

    public Enemy(int x, int y, int width, int height, Object target) {
        super(x, y, width, height);
        this.target = target;
        this.speed = 1;
        try { this.sprite = ImageIO.read(getClass().getResourceAsStream("/res/enemies/monster.png"));
        } catch (IOException ex) {}
    }

    public void moveTowardTarget(double delta) {
        double directionX = (target.x + target.width / 2) - (this.x + this.width / 2);
        double directionY = (target.y + target.height / 2) - (this.y + this.height / 2);
        // Normalize direction
        double length = Math.sqrt((directionX * directionX) + (directionY * directionY));
        if (length != 0) {
            directionX /= length;
            directionY /= length;
        }

        // Update position based on speed
        x += directionX * speed;
        y += directionY * speed;
        // System.out.println("directionX: " + directionX + ", directionY: " +
        // directionY);
    }
}
