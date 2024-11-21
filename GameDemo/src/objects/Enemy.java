package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Entity {

    Object target;

    public Enemy(int x, int y, int width, int height, Object target) {
        super(x, y, width, height);
        this.target = target;
        this.speed = 1;
        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/res/enemies/monster.png"));
        } catch (IOException ex) {
        }
    }

    public void update(double delta) {
        // Calculate direction vector to the target
        double directionX = (target.x + target.width / 2) - (this.x + this.width / 2);
        double directionY = (target.y + target.height / 2) - (this.y + this.height / 2);

        // Normalize direction
        double length = Math.sqrt((directionX * directionX) + (directionY * directionY));
        if (length != 0) {
            directionX /= length;
            directionY /= length;
        }

        // Scale by speed and delta time
        double moveX = directionX * speed * delta;
        double moveY = directionY * speed * delta;
        this.x += moveX;
        this.y += moveY;

    }

    public void draw(Graphics2D g) {
        // Draw a placeholder rectangle if no sprite is set
        g.setColor(Color.RED);
        g.fillRect(x - (width / 2), y - (height / 2), width, height);

        g.setColor(Color.WHITE);

        double directionX = target.x - this.x;
        double directionY = target.y - this.y;

        g.drawLine(target.x, target.y, this.x, this.y);
        g.drawString(directionX + " " + directionY, x - width, y - height);

        // Normalize direction
        double length = Math.sqrt((directionX * directionX) + (directionY * directionY));
        if (length != 0) {
            directionX /= length;
            directionY /= length;
        }

    }
}
