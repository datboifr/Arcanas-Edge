package objects.particles;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleManager {
    private List<Particle> particles = new ArrayList<>();
    private Random rand = new Random();

    public void spawn(float x, float y, int count, Color color, float size, float speed, float life, float gravity) {
        for (int i = 0; i < count; i++) {
            float angle = (float) (Math.toRadians(rand.nextInt(360)));
            float vx = (float) (Math.cos(angle) * speed * rand.nextFloat());
            float vy = (float) (Math.sin(angle) * speed * rand.nextFloat());
            particles.add(new Particle(x, y, vx, vy, life, color, size, gravity));
        }
    }

    public void update() {
        particles.removeIf(Particle::isDead); // Remove dead particles
        for (Particle p : particles) {
            p.update();
        }
    }

    public void draw(Graphics2D g) {
        for (Particle p : particles) {
            p.draw(g);
        }
    }
}
