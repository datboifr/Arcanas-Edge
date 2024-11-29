package store;

import java.awt.*;

public class Slot {

    private Upgrade upgrade;
    private Rectangle frame;
    boolean isSelected;

    public Slot(Rectangle frame, Upgrade upgrade) {
        this.frame = frame;
        this.upgrade = upgrade;
        this.isSelected = false;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fill(frame);

        g.setColor(Color.WHITE);
        g.drawString(upgrade.getTitle(), frame.x, frame.y);

        if (isSelected) {
            g.setStroke(new BasicStroke(2));
            g.draw(frame);
        }
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }
}
