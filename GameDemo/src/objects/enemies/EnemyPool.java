package objects.enemies;

import java.util.ArrayList;
import java.util.Random;

import main.GamePanel;

public class EnemyPool {

    static GamePanel graphicsPanel;
    static ArrayList<EnemyType> enemyPool;

    public static EnemyType getRandomEnemy(GamePanel panel) {
        enemyPool = new ArrayList<>();
        if (panel.getWave() >= 0) {
            // weak
            enemyPool.add(new EnemyType(20, 15, 1.5f, 5, "monster.png"));
            // basic
            enemyPool.add(new EnemyType(25, 20, 1.5f, 5, "monster.png"));
            // strong
            enemyPool.add(new EnemyType(35, 30, 1.5f, 10, "monster.png"));
        }

        Random random = new Random();
        return enemyPool.get(random.nextInt(enemyPool.size()));
    }
}
