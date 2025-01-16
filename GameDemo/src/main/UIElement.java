package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UIElement {

    private final BufferedImage sprite;
    private final Rectangle frame;

    public UIElement(String spritePath, int size, int x, int y) {
        this.frame = new Rectangle(x, y, size, size);
        this.sprite = loadImage(spritePath);
    }

    public UIElement(int x, int y, int width, int height) {
        this.frame = new Rectangle(x, y, width, height);
        this.sprite = null;
    }

    private BufferedImage loadImage(String spritePath) {
        try {
            return ImageIO
                    .read(getClass()
                            .getResourceAsStream("/res/" + spritePath + ".png"));
        } catch (IOException e) {
            System.out.print("Couldn't Fetch Sprite");
            return null;
        }
    }

    public void draw(Graphics2D g) {
        if (this.sprite != null) {
            g.drawImage(sprite, frame.x - frame.width / 2, frame.y - frame.height / 2, frame.width, frame.height, null);
        }
    }

    public Rectangle getFrame() {
        return this.frame;
    }

}
