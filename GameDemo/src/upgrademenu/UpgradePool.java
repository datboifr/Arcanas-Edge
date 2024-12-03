package upgrademenu;

import java.util.ArrayList;

import objects.Player;

public class UpgradePool {

        public static ArrayList<Upgrade> getUpgradePool(Player player) {
                ArrayList<Upgrade> upgradePool = new ArrayList<>();

                // health upgrade
                upgradePool.add(new Upgrade(
                                "Health",
                                "",
                                100,
                                () -> player.upgradeHealth()));

                // speed upgrade
                upgradePool.add(new Upgrade(
                                "Agility",
                                "",
                                100,
                                () -> player.upgradeAgility()));

                // contact damage upgrade
                upgradePool.add(new Upgrade(
                                "Contact Damage",
                                "",
                                100,
                                () -> player.upgradeContactDamage()));

                // shot damage upgrade
                upgradePool.add(new Upgrade(
                                "Shot Damage",
                                "",
                                100,
                                () -> player.upgradeProjectileDamage()));

                // projectile speed upgrade
                upgradePool.add(new Upgrade(
                                "Shot Speed",
                                "",
                                100,
                                () -> player.upgradeProjectileSpeed()));

                // projectile size upgrade
                upgradePool.add(new Upgrade(
                                "Shot Size",
                                "",
                                100,
                                () -> player.upgradeProjectileSize()));

                return upgradePool;

        }

}
