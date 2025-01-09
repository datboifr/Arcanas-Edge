package objects.particles;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Particle {
    private float x, y;
    private float vx, vy; // Velocity
    private float life; // Remaining lifespan
    private Color color;
    private float size;
    private float gravity;
    private String text;

    private boolean active = true;

    public Particle(float life, Color color, float size, float gravity) {
        this.life = life;
        this.color = color;
        this.size = size;
        this.gravity = gravity;
    }

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

    public Particle(float x, float y, float vx, float vy, float life, Color color, float size, float gravity,
            String text) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.life = life;
        this.color = color;
        this.size = size;
        this.gravity = gravity;
        this.text = text;
    }

    public void update() {
        if (active) {
            x += vx;
            y += vy;
            vy += gravity; // Simulate gravity
        }
        life -= 1; // Reduce life
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * (life / 100))));
        if (text == null) {
            g.fillRect((int) (x - size / 2), (int) (y - size / 2), (int) size, (int) size);
        } else {
            g.setFont(new Font("Impact", Font.BOLD, (int) size));
            AffineTransform transform = new AffineTransform();
            transform.translate(x, y);
            g.drawString(text, (int) x, (int) y);
        }
    }

    public boolean isDead() {
        return life <= 0;
    }

    public float getLife() {
        return this.life;
    }

    public Color getColor() {
        return this.color;
    }

    public float getSize() {
        return this.size;
    }

    public float getGravity() {
        return this.gravity;
    }
}
