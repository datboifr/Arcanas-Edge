package objects;

import java.awt.*;
import java.util.Random;

public class StoreItem {

    int x, y, width, height, cost;
    String name;

    Random random = new Random();

    String[] upgrades = { "damage", "speed", "range", "health" };
    String[] structures = { "turret", "medbay", "spiketrap" };

    public void draw(Graphics2D g, Rectangle store) {
        this.name = upgrades[random.nextInt(upgrades.length)];
    }

}
