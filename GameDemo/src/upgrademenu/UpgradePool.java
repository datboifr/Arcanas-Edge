package upgrademenu;

import java.util.ArrayList;

import combat.AbilityTypes;
import main.GamePanel;
import objects.Player;

public class UpgradePool {

        interface UpgradeAction {
                void execute();
        }

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
                                "MaxHealth", // spritePath
                                25, // cost
                                1.1f, // costIncrease
                                () -> player.upgradeMaxHealth(), 100));

                // recovery upgrade
                upgradePool.add(new Upgrade(
                                "Recovery",
                                "Heal faster",
                                "Recovery", // spritePath
                                25, // cost
                                1.2f, // costIncrease
                                () -> player.upgradeRecovery(), 100));

                // speed upgradeuku
                upgradePool.add(new Upgrade(
                                "Agility",
                                "Move faster to evade enemies",
                                "Agility", // spritePath
                                20, // cost
                                1.3f, // costIncrease
                                () -> player.upgradeAgility(), 100));

                // contact damage upgrade
                upgradePool.add(new Upgrade(
                                "Contact Damage",
                                "Boost the damage dealt on contact",
                                "ContactDamage", // spritePath
                                35, // cost
                                1f, // costIncrease
                                () -> player.upgradeContactDamage(), 100));

                // shot damage upgrade
                upgradePool.add(new Upgrade(
                                "Magic Damage",
                                "Increase the damage of your magic",
                                "ProjectileDamage", // spritePath
                                35, // cost
                                1.1f, // costIncrease
                                () -> player.upgradeProjectileDamage(), 100));

                // projectile speed upgrade
                upgradePool.add(new Upgrade(
                                "Magic Speed",
                                "Increase the speed of your magic",
                                "ProjectileSpeed", // spritePath
                                40, // cost
                                1.1f, // costIncrease
                                () -> player.upgradeProjectileSpeed(), 100));

                // projectile size upgrade
                upgradePool.add(new Upgrade(
                                "Magic Size",
                                "Increase the size of your magic",
                                "MagicSize", // spritePath
                                30, // cost
                                1.1f, // costIncrease
                                () -> player.upgradeProjectileSize(), 100));

                // ability cooldown upgrade
                upgradePool.add(new Upgrade(
                                "Magic Cooldown",
                                "Reduce the cooldown of abilities",
                                "Cooldown", // spritePath
                                45, // cost
                                1.4f, // costIncrease
                                () -> player.upgradeAbilityCooldown(), 10));

                // more projectiles upgrade
                upgradePool.add(new Upgrade(
                                "More Projectiles",
                                "Fire additional projectiles",
                                "ProjectileBonus", // spritePath
                                45, // cost
                                1.5f, // costIncrease
                                () -> player.upgradeProjectileBonus(), 6));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Lenses",
                                "Increase pickup range",
                                "Lenses", // spritePath
                                25, // cost
                                1.4f, // costIncrease
                                () -> player.upgradePickupRange(), 10));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Jar Of Lightning",
                                "We bring the boom!",
                                "Abilities/JarOfLightning", // spritePath
                                250, // cost
                                1f, // costIncrease
                                () -> player.newAbility(AbilityTypes.jarOfLightning), 1));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Mask Of Flames",
                                "Remember to social distance",
                                "Abilities/MaskOfFlames", // spritePath
                                250, // cost
                                1f, // costIncrease
                                () -> player.newAbility(AbilityTypes.maskOfFlames), 1));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Rock",
                                "It calls for it's brothers",
                                "Abilities/Rock", // spritePath
                                250, // cost
                                1f, // costIncrease
                                () -> player.newAbility(AbilityTypes.rock), 1));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Soul Egg",
                                "Who created this thing?",
                                "Abilities/SoulEgg", // spritePath
                                250, // cost
                                1f, // costIncrease
                                () -> player.newAbility(AbilityTypes.falcon), 1));

                return upgradePool;
        }

}
