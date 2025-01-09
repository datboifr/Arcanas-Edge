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
                                1,
                                () -> player.upgradeMaxHealth()));

                // speed upgrade
                upgradePool.add(new Upgrade(
                                "Agility",
                                "",
                                1,
                                () -> player.upgradeAgility()));

                // contact damage upgrade
                upgradePool.add(new Upgrade(
                                "Contact Damage",
                                "ContactDamage",
                                1,
                                () -> player.upgradeContactDamage()));

                // shot damage upgrade
                upgradePool.add(new Upgrade(
                                "Magic Damage",
                                "",
                                1,
                                () -> player.upgradeProjectileDamage()));

                // projectile speed upgrade
                upgradePool.add(new Upgrade(
                                "Magic Speed",
                                "ProjectileSpeed",
                                1,
                                () -> player.upgradeProjectileSpeed()));

                // projectile size upgrade
                upgradePool.add(new Upgrade(
                                "Magic Size",
                                "",
                                1,
                                () -> player.upgradeProjectileSize()));

                upgradePool.add(new Upgrade(
                                "Magic Cooldown",
                                "",
                                2,
                                () -> player.upgradeAbilityCooldown()));

                upgradePool.add(new Upgrade(
                                "More Projectiles",
                                "ProjectileBonus",
                                2,
                                () -> player.upgradeProjectileBonus()));

                upgradePool.add(new Upgrade(
                                "Electric Blast",
                                "",
                                5,
                                () -> player.newAbility(AbilityTypes.electric)));

                return upgradePool;

        }
}
