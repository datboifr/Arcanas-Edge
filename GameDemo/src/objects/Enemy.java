package objects;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Entity {

    Object target;

    public Enemy(int x, int y, int width, int height, Object target) {
        super(x, y, width, height);
        this.target = target;
        this.speed = 2;
        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/res/enemies/monster.png"));
        } catch (IOException ex) {
        }
    }

    public void update(double delta) {
        // Calculate direction vector
        float directionX = target.x - x;
        float directionY = target.y - y;
        
        // Calculate length
        float length = (float) Math.sqrt(Math.pow(directionX, 2) + Math.pow(directionY, 2));
        
        // Calculate normalized direcon vector
        directionX /= length;
        directionY /= length;

        System.err.println(directionX);
        System.err.println(directionY);

        x += directionX * speed;
        y += directionY * speed;
       
    }
}
