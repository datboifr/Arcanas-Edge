package main;

import java.awt.*;

public class Button {
    private final String label;
    private final Rectangle bounds;
    private final Action action;
    private boolean isSelected;

    @FunctionalInterface
    public interface Action {
        void execute();
    }

    public Button(String label, int x, int y, int width, int height, Action action) {
        this.label = label;
        this.bounds = new Rectangle(x, y, width, height);
        this.action = action;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public void draw(Graphics2D g) {
        // Draw background
        g.setColor(isSelected ? Color.YELLOW : Color.WHITE);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Draw text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getAscent();
        int textX = bounds.x + (bounds.width - textWidth) / 2;
        int textY = bounds.y + (bounds.height + textHeight) / 2 - 5;
        g.drawString(label, textX, textY);
    }

    public void select() {
        if (action != null) {
            action.execute();
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
