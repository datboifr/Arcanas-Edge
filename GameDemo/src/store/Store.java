package store;

import java.awt.*;
import java.util.*;

import main.KeyHandler;

public class Store {

    private Slot[][] slots;
    private Rectangle frame;
    private ArrayList<Upgrade> upgradePool;
    private float margin;
    private int selection;
    private int rows, cols;
    private KeyHandler keyHandler;

    public Store(Rectangle frame, KeyHandler keyHandler) {

        this.slots = new Slot[2][3];
        this.frame = frame;
        this.margin = 2f;
        this.selection = 0;
        this.rows = slots.length;
        this.cols = slots[0].length;
        this.keyHandler = keyHandler;

        this.upgradePool = UpgradePool.getUpgradePool();

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

                Random random = new Random();
                slots[row][col] = new Slot(slotFrame, upgradePool.get(random.nextInt(upgradePool.size())));
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

        if (keyHandler.zActive)
            slots[currentRow][currentCol].purchase();
    }

    public void draw(Graphics2D g) {

        // draw frame
        g.setColor(new Color(0f, 0f, 0f, .5f));
        g.fill(frame);

        // draw slots
        for (Slot[] row : slots) {
            for (Slot slot : row) {
                slot.draw(g);
            }
        }

        // debugging
        g.setColor(Color.WHITE);
        g.drawString("Press 'P' key to close", 10, 10);
        g.drawString("Selected Slot: " + selection, 10, 20);
    }

}
