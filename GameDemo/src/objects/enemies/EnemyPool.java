package objects.enemies;

import java.util.ArrayList;

import main.GamePanel;

public class EnemyPool {

    GamePanel graphicsPanel;
    ArrayList<EnemyType> enemyPool;

    public EnemyPool(GamePanel graphicsPanel) {
        this.graphicsPanel = graphicsPanel;
        enemyPool = new ArrayList<>();
        updatePool();
    }

    public void updatePool() {
        if (graphicsPanel.getWave() >= 0) {
            // weak
            enemyPool.add(new EnemyType(20, 15, 1.5f, 5, "monster.png"));
            // basic
            enemyPool.add(new EnemyType(25, 20, 1.5f, 5, "monster.png"));
            // strong
            enemyPool.add(new EnemyType(35, 30, 1.5f, 10, "monster.png"));
        }
    }

    public GamePanel getGraphicsPanel() {
        return this.graphicsPanel;
    }

    public ArrayList<EnemyType> getEnemyPool() {
        return this.enemyPool;
    }

}
