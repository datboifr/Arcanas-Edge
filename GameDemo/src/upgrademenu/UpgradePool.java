package upgrademenu;

import java.util.ArrayList;

import combat.AbilityTypes;
import main.GamePanel;
import objects.Player;

public class UpgradePool {

        public static ArrayList<Upgrade> getAbilityPool(Player player) {
                ArrayList<Upgrade> upgradePool = new ArrayList<>();
                return upgradePool;
        }

        public static ArrayList<Upgrade> getUpgrades(GamePanel panel, Player player) {
                ArrayList<Upgrade> upgradePool = new ArrayList<>();

                // health upgrade
                upgradePool.add(new Upgrade(
                                "Max Health",
                                "",
                                25,
                                () -> player.upgradeMaxHealth()));

                // speed upgrade
                upgradePool.add(new Upgrade(
                                "Agility",
                                "",
                                20,
                                () -> player.upgradeAgility()));

                // contact damage upgrade
                upgradePool.add(new Upgrade(
                                "Contact Damage",
                                "ContactDamage",
                                35,
                                () -> player.upgradeContactDamage()));

                // shot damage upgrade
                upgradePool.add(new Upgrade(
                                "Magic Damage",
                                "",
                                35,
                                () -> player.upgradeProjectileDamage()));

                // projectile speed upgrade
                upgradePool.add(new Upgrade(
                                "Magic Speed",
                                "ProjectileSpeed",
                                40,
                                () -> player.upgradeProjectileSpeed()));

                // projectile size upgrade
                upgradePool.add(new Upgrade(
                                "Magic Size",
                                "",
                                30,
                                () -> player.upgradeProjectileSize()));

                upgradePool.add(new Upgrade(
                                "Magic Cooldown",
                                "",
                                45,
                                () -> player.upgradeAbilityCooldown()));

                upgradePool.add(new Upgrade(
                                "More Projectiles",
                                "ProjectileBonus",
                                45,
                                () -> player.upgradeProjectileBonus()));

                upgradePool.add(new Upgrade(
                                "Electric Blast",
                                "",
                                5,
                                () -> player.newAbility(AbilityTypes.electric)));

                upgradePool.add(new Upgrade(
                              "Lenses",
                               "",
                                25,
                               () -> player.upgradePickupRange()));

                return upgradePool;

        }
}
