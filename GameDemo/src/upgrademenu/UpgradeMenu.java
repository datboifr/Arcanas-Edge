package upgrademenu;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

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

    // graphical settings

    protected BufferedImage costIcon;
    protected BufferedImage unavailableIcon;
    protected final int COST_ICON_SIZE = 20;
    protected final int SPRITE_SIZE = 150;

    protected final int MARGIN = 1; // Margin as a percentage of frame width/height
    protected final int BORDER = 3; // pixels

    protected final Color TITLE_COLOR = new Color(255, 255, 255);
    protected final Font TITLE_TEXT = new Font("Inria Serif", Font.BOLD, 20);
    protected final Color DESCRIPTION_COLOR = new Color(200, 200, 200);
    protected final Font DESCRIPTION_TEXT = new Font("Inria Serif", Font.BOLD, 10);

    public UpgradeMenu(Rectangle frame, ArrayList<Upgrade> upgradePool, GamePanel gp, int cols, int rows) {
        this.gp = gp;
        this.keyHandler = gp.getKeyHandler();

        this.cols = cols;
        this.rows = rows;
        this.frame = frame;
        this.slots = new Slot[rows][cols];
        this.selection = 0;

        this.upgradePool = upgradePool;

        // Load the cost icon
        try {
            costIcon = ImageIO.read(getClass().getResourceAsStream("/res/pickups/gold.png"));
        } catch (IOException e) {
            costIcon = null;
            System.out.println("Couldn't fetch cost icon sprite.");
        }

        // Load the cost icon
        try {
            unavailableIcon = ImageIO.read(getClass().getResourceAsStream("/res/icons/Unavailable.png"));
        } catch (IOException e) {
            unavailableIcon = null;
            System.out.println("Couldn't fetch unavailable sprite.");
        }

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

        // Shuffle the pool and make a copy of available upgrades
        ArrayList<Upgrade> shuffledPool = new ArrayList<>();
        for (Upgrade upgrade : upgradePool) {
            if (upgrade.isAvailable()) {
                shuffledPool.add(upgrade);
            }
        }

        Collections.shuffle(shuffledPool);

        int totalSlots = rows * cols;
        if (shuffledPool.size() < totalSlots) {
            throw new IllegalStateException("Not enough available upgrades to fill all slots.");
        }

        int upgradeIndex = 0; // Index for the shuffled pool

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = frame.x + horizontalMargin + col * (slotWidth + horizontalMargin);
                int y = frame.y + verticalMargin + row * (slotHeight + verticalMargin);
                Rectangle slotFrame = new Rectangle(x, y, slotWidth, slotHeight);

                // Assign the next upgrade in the shuffled pool
                Upgrade upgrade = shuffledPool.get(upgradeIndex);
                slots[row][col] = new Slot(this, slotFrame, upgrade);

                upgradeIndex++;
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
        if (selectedSlot != null && gp.getPlayer().getMoney() >= selectedSlot.getUpgrade().getCost()
                && selectedSlot.getUpgrade().isAvailable()) {
            gp.getPlayer().addMoney(-selectedSlot.getUpgrade().getCost());
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
