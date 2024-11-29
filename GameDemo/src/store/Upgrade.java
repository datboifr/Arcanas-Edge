package store;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Upgrade {
    private String title;
    private BufferedImage sprite;
    private int cost;
    private UpgradeAction action;

    public Upgrade(String title, String spritePath, int cost, UpgradeAction action) {
        this.title = title;
        this.cost = cost;
        this.action = action;

        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream(spritePath));
        } catch (IOException e) {
            System.out.println("Couldn't load sprite");
        }
    }

    public String getTitle() {
        return title;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public int getCost() {
        return cost;
    }

    public void buy() {
        if (action != null) {
            action.execute();
        }
    }
}
