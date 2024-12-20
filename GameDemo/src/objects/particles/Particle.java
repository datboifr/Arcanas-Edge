package objects.particles;

import java.awt.*;
import java.util.Random;

public class Particle {
    private float x, y;
    private float vx, vy; // Velocity
    private float life; // Remaining lifespan
    private Color color;
    private float size;
    private float gravity;

    public Particle(float x, float y, float vx, float vy, float life, Color color, float size, float gravity) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.life = life;
        this.color = color;
        this.size = size;
        this.gravity = gravity;
    }

    public void update() {
        x += vx;
        y += vy;
        vy += gravity; // Simulate gravity
        life -= 1; // Reduce life
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * (life / 100))));
        g.fillOval((int) (x - size / 2), (int) (y - size / 2), (int) size, (int) size);
    }

    public boolean isDead() {
        return life <= 0;
    }
}
