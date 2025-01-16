package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UIElement {

    private final BufferedImage sprite;
    private final Rectangle frame;

    private final int BORDER = 2;
    private final int ROUNDNESS = 10;

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
        } else {
            // Fill the background of the stat display
            g.setColor(new Color(0f, 0f, 0f, 0.5f));
            g.fillRoundRect(frame.x, frame.y, frame.width, frame.height, ROUNDNESS, ROUNDNESS);

            g.setColor(new Color(1f, 1f, 1f, 0.5f));
            g.setStroke(new BasicStroke(BORDER));
            g.drawRoundRect(frame.x, frame.y, frame.width, frame.height, ROUNDNESS, ROUNDNESS);
        }
    }

    public Rectangle getFrame() {
        return this.frame;
    }

}
