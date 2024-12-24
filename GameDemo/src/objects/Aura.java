package objects;

public class Aura extends GameObject {

    private int value;

    public Aura(int x, int y) {
        super(x, y, 7, 7);
        this.value = 1;
        this.speed = 5;
    }

    @Override
    public void update() {
        if (target != null && target.isAlive()) {
            // Calculate the direction vector toward the target
            float dx = target.x - x;
            float dy = target.y - y;

            // Normalize the direction vector
            float magnitude = (float) Math.sqrt(dx * dx + dy * dy);
            if (magnitude > 0) {
                dx /= magnitude;
                dy /= magnitude;
            }

            // Update the position of the aura
            x += dx * speed;
            y += dy * speed;

            // Check if the aura has reached the target
            if (isTouching(target)) {
                if (target instanceof Player) {
                    target.collectAura(this);
                }
                die();
            }
        }
    }

    public void collect(GameObject other) {
        target = other;
    }

    public int getValue() {
        return this.value;
    }

}
