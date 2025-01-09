package upgrademenu;

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

        if (!spritePath.equals("")) {
            try {
                this.sprite = ImageIO
                        .read(getClass().getResourceAsStream("/res/icons/upgrades/" + spritePath + ".png"));
            } catch (IOException e) {
                System.out.println("Couldn't load sprite");
            }
        }
    }

    public void buy() {
        if (action != null) {
            action.execute();
        }
    }

    // getters

    public String getTitle() {
        return title;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public int getCost() {
        return cost;
    }

    public UpgradeAction getUpgradeAction() {
        return action;
    }
}
