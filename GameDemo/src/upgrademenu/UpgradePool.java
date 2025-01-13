package upgrademenu;

import java.util.ArrayList;

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
                                "Increase your maximum health",
                                "", // spritePath
                                25, // cost
                                1.1f, // costIncrease
                                () -> player.upgradeMaxHealth()));

                // recovery upgrade
                upgradePool.add(new Upgrade(
                                "Recovery",
                                "Heal faster",
                                "Recovery", // spritePath
                                25, // cost
                                1.5f, // costIncrease
                                () -> player.upgradeRecovery()));

                // speed upgradeuku
                upgradePool.add(new Upgrade(
                                "Agility",
                                "Move faster to evade enemies",
                                "Agility", // spritePath
                                20, // cost
                                1.5f, // costIncrease
                                () -> player.upgradeAgility()));

                // contact damage upgrade
                upgradePool.add(new Upgrade(
                                "Contact Damage",
                                "Boost the damage dealt on contact",
                                "ContactDamage", // spritePath
                                35, // cost
                                1f, // costIncrease
                                () -> player.upgradeContactDamage()));

                // shot damage upgrade
                upgradePool.add(new Upgrade(
                                "Magic Damage",
                                "Increase the damage of your magic",
                                "ProjectileDamage", // spritePath
                                35, // cost
                                1.2f, // costIncrease
                                () -> player.upgradeProjectileDamage()));

                // projectile speed upgrade
                upgradePool.add(new Upgrade(
                                "Magic Speed",
                                "Increase the speed of your magic",
                                "ProjectileSpeed", // spritePath
                                40, // cost
                                1.2f, // costIncrease
                                () -> player.upgradeProjectileSpeed()));

                // projectile size upgrade
                upgradePool.add(new Upgrade(
                                "Magic Size",
                                "Increase the size of your magic",
                                "", // spritePath
                                30, // cost
                                1.3f, // costIncrease
                                () -> player.upgradeProjectileSize()));

                // ability cooldown upgrade
                upgradePool.add(new Upgrade(
                                "Magic Cooldown",
                                "Reduce the cooldown of abilities",
                                "Cooldown", // spritePath
                                45, // cost
                                1.7f, // costIncrease
                                () -> player.upgradeAbilityCooldown()));

                // more projectiles upgrade
                upgradePool.add(new Upgrade(
                                "More Projectiles",
                                "Fire additional projectiles",
                                "ProjectileBonus", // spritePath
                                45, // cost
                                2, // costIncrease
                                () -> player.upgradeProjectileBonus()));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Lenses",
                                "Increase pickup range",
                                "", // spritePath
                                25, // cost
                                1.4f, // costIncrease
                                () -> player.upgradePickupRange()));

                return upgradePool;
        }

}
