package objects;

import java.awt.*;
import main.KeyHandler;

public class Store {

    Slot[][] slots;
    Rectangle frame;
    float margin;
    int selection;
    int rows, cols;
    KeyHandler keyHandler;

    public Store(Rectangle frame, KeyHandler keyHandler) {
        this.slots = new Slot[2][3]; // 2 rows, 3 columns
        this.frame = frame;
        this.margin = 2f;
        this.selection = 0;
        this.rows = slots.length;
        this.cols = slots[0].length;
        this.keyHandler = keyHandler;
    }

    public void fillStore() {
        int horizontalMargin = (int) (frame.width * margin / 100.0);
        int verticalMargin = (int) (frame.height * margin / 100.0);

        int width = (frame.width - (horizontalMargin * (cols + 1))) / cols;
        int height = (frame.height - (verticalMargin * (rows + 1))) / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = frame.x + horizontalMargin + col * (width + horizontalMargin);
                int y = frame.y + verticalMargin + row * (height + verticalMargin);
                Rectangle slotFrame = new Rectangle(x, y, width, height);
                slots[row][col] = new Slot(slotFrame);
            }
        }
    }

    public void update() {

        int currentRow = selection / cols;
        int currentCol = selection % cols;

        // Move selection based on key presses
        if (keyHandler.upActive) {
            currentRow = (currentRow - 1 + rows) % rows; // Wrap to last row if moving up from first
            keyHandler.upActive = false; // Prevent continuous movement
        }
        if (keyHandler.downActive) {
            currentRow = (currentRow + 1) % rows; // Wrap to first row if moving down from last
            keyHandler.downActive = false;
        }
        if (keyHandler.leftActive) {
            currentCol = (currentCol - 1 + cols) % cols; // Wrap to last column if moving left from first
            keyHandler.leftActive = false;
        }
        if (keyHandler.rightActive) {
            currentCol = (currentCol + 1) % cols; // Wrap to first column if moving right from last
            keyHandler.rightActive = false;
        }

        // Update selection
        selection = currentRow * cols + currentCol;

        // Highlight the selected slot
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                slots[row][col].isSelected = (row == currentRow && col == currentCol);
            }
        }
    }

    public void draw(Graphics2D g) {
        Color background = new Color(0f, 0f, 0f, .5f);

        // draw frame
        g.setColor(background);
        g.fill(frame);

        // draw slots
        for (Slot[] row : slots) {
            for (Slot slot : row) {
                g.setColor(background);
                // all this will be replaced in the slot class
                g.fill(slot.frame);
                if (slot.isSelected) {
                    g.setStroke(new BasicStroke(2));
                    g.setColor(Color.WHITE);
                    g.draw(slot.frame);
                }
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("Press 'P' key to close", 10, 10);
        g.drawString("Selected Slot: " + selection, 10, 20);
    }
}
