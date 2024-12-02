package upgrademenu;

import java.util.ArrayList;

import objects.Player;

public class UpgradePool {

        public static ArrayList<Upgrade> getUpgradePool(Player player) {
                ArrayList<Upgrade> upgradePool = new ArrayList<>();

                upgradePool.add(new Upgrade(
                                "Health",
                                "/res/enemies/monster.png",
                                100,
                                () -> player.upgradeHealth()));

                upgradePool.add(new Upgrade(
                                "Agility",
                                "/res/enemies/monster.png",
                                100,
                                () -> player.upgradeAgility()));

                upgradePool.add(new Upgrade(
                                "Strength",
                                "/res/enemies/monster.png",
                                100,
                                () -> player.upgradeStrength()));

                return upgradePool;

        }

}
