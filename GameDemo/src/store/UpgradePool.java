package store;

import java.util.ArrayList;

public class UpgradePool {

    public static ArrayList<Upgrade> getUpgradePool() {
        ArrayList<Upgrade> upgradePool = new ArrayList<>();

        upgradePool.add(new Upgrade(
                "Damage",
                "/res/enemies/monster.png",
                100,
                () -> System.out.println("Damage increased by 10!")));

        return upgradePool;
    }

}
