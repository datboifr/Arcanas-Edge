package upgrademenu;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Slot {

    private Upgrade upgrade;
    private Rectangle frame;
    boolean isSelected;
    BufferedImage sprite;

    private final int TITLE_SIZE = 20;
    private final int BORDER = 3; // pixels

    public Slot(Rectangle frame, Upgrade upgrade) {
        this.frame = frame;
        this.upgrade = upgrade;
        this.isSelected = false;
        setSprite("cards/CardSpawnUnindexed_23");
    }

    public void setSprite(String spritePath) {
        try {
            this.sprite = ImageIO
                    .read(getClass()
                            .getResourceAsStream("/res/" + spritePath + ".png"));
        } catch (IOException e) {
            this.sprite = null;
            System.out.print("Couldn't Fetch Sprite");
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);

        g.fill(frame);
                g.drawImage(sprite, frame.x, frame.y, 150, 150, null);
        // title initalization
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, TITLE_SIZE));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        // Calculate the text width and height
        int textWidth = metrics.stringWidth(upgrade.getTitle());
        int textHeight = metrics.getHeight();

        int titleX = (frame.x + (frame.width / 2) - (textWidth / 2));
        int titleY = frame.y + textHeight + 10;
        g.drawString(upgrade.getTitle(), titleX, titleY);

        Image sprite = upgrade.getSprite(); // Assuming Upgrade has a method to get the sprite
        if (sprite != null) {
            // Desired sprite size (modify as needed)
            int spriteWidth = 150; // Example: half the frame width
            int spriteHeight = 150; // Example: one-third of the frame height

            // Calculate the position to center the resized sprite
            int spriteX = frame.x + (frame.width / 2) - (spriteWidth / 2);
            int spriteY = titleY + 10; // Place it below the title with padding

            g.drawImage(sprite, spriteX, spriteY, spriteWidth, spriteHeight, null);
        }

        if (isSelected) {
            g.setStroke(new BasicStroke(BORDER));
            g.draw(frame);
        }
    }

    public void purchase() {
        upgrade.buy();
    }
}
