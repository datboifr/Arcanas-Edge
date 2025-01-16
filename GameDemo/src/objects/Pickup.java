package objects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Pickup extends GameObject {

    interface Action {
        void onCollect(GameObject other);
    }

    public enum Type implements Action {
        DIAMOND("diamond", 9),
        GOLD("gold", 6),
        SILVER("silver", 3);

        private final int value;
        private final BufferedImage sprite;
        private Action action;

        Type(String spritePath, int value, Action action) {
            this.value = value;
            this.sprite = setSprite(spritePath);
            this.action = action;
        }

        Type(String spritePath, int value) {
            this.value = value;
            this.sprite = setSprite(spritePath);
        }

        public BufferedImage setSprite(String spritePath) {
            try {
                return ImageIO
                        .read(getClass()
                                .getResourceAsStream("/res/pickups/" + spritePath + ".png"));
            } catch (IOException e) {
                System.out.print("Couldn't Fetch Sprite");
                return null;
            }
        }

        @Override
        public void onCollect(GameObject other) {
        }

        public int getValue() {
            return value;
        }

        public BufferedImage getSprite() {
            return this.sprite;
        }

        public Action getAction() {
            return this.action;
        }
    }

    private final Type type;
    private final int value;
    private Action action;

    // Constructor for initializing a specific pickup
    public Pickup(int x, int y, Type type) {
        super(x, y, 14, 14);
        this.type = type;
        this.value = type.getValue();
        this.action = type.getAction();
        this.sprite = type.getSprite();

        this.width = (int) (sprite.getWidth() / 1.5);
        this.height = (int) (sprite.getHeight() / 1.5);

        this.speed = 5;
    }

    // Update logic for pickup
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

            // Update position of the pickup
            x += dx * speed;
            y += dy * speed;

            // Check if the pickup has reached the target
            if (touching(target)) {
                if (target instanceof Player) {
                    target.collectPickup(this);
                }
                die();
            }
        }
    }

    public Action doAction() {
        return this.action;
    }

    public void setTarget(GameObject other) {
        target = other;
    }

    public int getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public static Type getRandomPickup() {
        Random random = new Random();
        // Randomly pick a type from the enum values
        Type[] types = Type.values();
        return types[random.nextInt(types.length)];
    }
}