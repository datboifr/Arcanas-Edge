package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UIElement {

    private final BufferedImage sprite;
    private final int x, y, size;

    public UIElement(String spritePath, int size, int x, int y) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.sprite = loadImage(spritePath);
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
            g.drawImage(sprite, x - size / 2, y - size / 2, size, size, null);
        } else {
            // Draw a placeholder if no sprite is set
            g.setColor(Color.WHITE);
            g.fillRect(x - size / 2, y - size / 2, size, size);
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getSize() {
        return this.size;
    }

}
