package objects;

import java.awt.*;
import java.util.Random;

public class Slot {

    String name;
    Rectangle frame;
    boolean isSelected;

    Random random = new Random();

    String[] stats = { "damage", "speed", "range", "health" };
    String[] structures = { "turret", "medbay", "spiketrap" };

    public Slot(Rectangle frame) {
        this.name = stats[random.nextInt(stats.length)];
        this.frame = frame;
        this.isSelected = false;
    }

}
