package upgrademenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import main.GamePanel;
import main.KeyHandler;

public class UpgradeMenu {

    private GamePanel gp;
    private KeyHandler keyHandler;

    private Slot[][] slots;
    private Rectangle frame;
    private ArrayList<Upgrade> upgradePool;

    private Slot selectedSlot;
    private int selection;
    private int rows, cols;

    private boolean selectActive = false;

    private final int MARGIN = 1; // Margin as a percentage of frame width/height

    public final int BORDER = 3; // pixels
    public final int COST_MARGIN = 5;

    public final Color TITLE_COLOR = new Color(255, 255, 255);
    public final Font TITLE_TEXT = new Font("Inria Serif", Font.BOLD, 20);
    public final Color DESCRIPTION_COLOR = new Color(200, 200, 200);
    public final Font DESCRIPTION_TEXT = new Font("Inria Serif", Font.BOLD, 10);

    public UpgradeMenu(Rectangle frame, ArrayList<Upgrade> upgradePool, GamePanel gp, int cols, int rows) {
        this.gp = gp;
        this.keyHandler = gp.getKeyHandler();

        this.cols = cols;
        this.rows = rows;
        this.frame = frame;
        this.slots = new Slot[rows][cols];
        this.selection = 0;

        this.upgradePool = upgradePool;

        fillSlots(); // Populate the slots during initialization
    }

    /**
     * Fills the slots with unique upgrades from the upgrade pool.
     */
    public void fillSlots() {
        int horizontalMargin = (int) (frame.width * MARGIN / 100.0);
        int verticalMargin = (int) (frame.height * MARGIN / 100.0);

        int slotWidth = (frame.width - (horizontalMargin * (cols + 1))) / cols;
        int slotHeight = (frame.height - (verticalMargin * (rows + 1))) / rows;

        Collections.shuffle(upgradePool);

        int upgradeIndex = 0; // Tracks the index in the shuffled pool

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = frame.x + horizontalMargin + col * (slotWidth + horizontalMargin);
                int y = frame.y + verticalMargin + row * (slotHeight + verticalMargin);
                Rectangle slotFrame = new Rectangle(x, y, slotWidth, slotHeight);

                // Get the next upgrade from the shuffled pool
                Upgrade upgrade = upgradePool.get(upgradeIndex);
                slots[row][col] = new Slot(this, slotFrame, upgrade);

                // Increment the index, reset if we exhaust the pool
                upgradeIndex = (upgradeIndex + 1) % upgradePool.size();
            }
        }
    }

    /**
     * Updates the menu logic, including selection navigation and cancel logic.
     */
    public void update() {
        int currentRow = selection / cols;
        int currentCol = selection % cols;

        // Handle navigation
        if (keyHandler.upActive) {
            currentRow = (currentRow - 1 + rows) % rows;
            keyHandler.upActive = false;
        }
        if (keyHandler.downActive) {
            currentRow = (currentRow + 1) % rows;
            keyHandler.downActive = false;
        }
        if (keyHandler.leftActive) {
            currentCol = (currentCol - 1 + cols) % cols;
            keyHandler.leftActive = false;
        }
        if (keyHandler.rightActive) {
            currentCol = (currentCol + 1) % cols;
            keyHandler.rightActive = false;
        }

        // Update the selected slot
        selection = currentRow * cols + currentCol;
        selectedSlot = slots[currentRow][currentCol];

        // Highlight the selected slot
        highlightSelectedSlot();

        // Handle purchase logic
        if (keyHandler.zActive) {
            if (!selectActive) {
                attemptPurchase();
                selectActive = true;
            }
        } else {
            selectActive = false;
        }

        if (keyHandler.xActive) {
            gp.closeUpgradeMenu();
        }
    }

    /**
     * Highlights the selected slot in the menu.
     */
    private void highlightSelectedSlot() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                slots[row][col].isSelected = (slots[row][col] == selectedSlot);
            }
        }
    }

    /**
     * Attempts to purchase the selected upgrade.
     */
    private void attemptPurchase() {
        if (selectedSlot != null && gp.getPlayer().getAura() >= selectedSlot.getUpgrade().getCost()) {
            gp.getPlayer().addAura(-selectedSlot.getUpgrade().getCost());
            selectedSlot.purchase();
        }
    }

    /**
     * Draws the upgrade menu, slots, and the cancel button.
     * 
     * @param g Graphics2D object for rendering
     */
    public void draw(Graphics2D g) {
        // Draw menu frame
        g.setColor(new Color(0f, 0f, 0f, 0.5f));
        g.fill(frame);

        // Draw each slot
        for (Slot[] row : slots) {
            for (Slot slot : row) {
                slot.draw(g);
            }
        }
    }
}
