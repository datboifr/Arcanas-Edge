package upgrademenu;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Upgrade {

    private String title;
    private String description;
    private BufferedImage sprite;
    private UpgradeAction action;

    private int cost;
    private float costIncrease;

    private int buyableCount;
    private boolean available = true;

    public Upgrade(String title, String description, String spritePath, int cost, float costIncrease,
            UpgradeAction action, int buyableCount) {

        this.title = title;
        this.description = description;

        this.cost = cost;
        this.costIncrease = costIncrease;

        this.action = action;

        this.buyableCount = buyableCount;

        if (!spritePath.equals("")) {
            try {
                this.sprite = ImageIO
                        .read(getClass().getResourceAsStream("/res/icons/upgrades/" + spritePath + ".png"));
            } catch (IOException e) {
                System.out.println("Couldn't load sprite");
            }
        }
    }

    public Upgrade(String string, Object object, String string2, int i, int j, Object object2) {
    }

    public void buy() {
        buyableCount--;
        if (action != null) {
            action.execute();
        }
        if (buyableCount <= 0) {
            this.available = false;
            return;
        }
        cost += (int) (cost * costIncrease);
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

    public String getDescription() {
        return this.description;
    }

    public boolean isAvailable() {
        return this.available;
    }
}
