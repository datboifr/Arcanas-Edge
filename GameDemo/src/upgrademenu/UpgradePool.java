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
                                () -> player.upgradeMaxHealth(), true));

                // recovery upgrade
                upgradePool.add(new Upgrade(
                                "Recovery",
                                "Heal faster",
                                "Recovery", // spritePath
                                25, // cost
                                1.5f, // costIncrease
                                () -> player.upgradeRecovery(), true));

                // speed upgradeuku
                upgradePool.add(new Upgrade(
                                "Agility",
                                "Move faster to evade enemies",
                                "Agility", // spritePath
                                20, // cost
                                1.5f, // costIncrease
                                () -> player.upgradeAgility(), true));

                // contact damage upgrade
                upgradePool.add(new Upgrade(
                                "Contact Damage",
                                "Boost the damage dealt on contact",
                                "ContactDamage", // spritePath
                                35, // cost
                                1f, // costIncrease
                                () -> player.upgradeContactDamage(), true));

                // shot damage upgrade
                upgradePool.add(new Upgrade(
                                "Magic Damage",
                                "Increase the damage of your magic",
                                "ProjectileDamage", // spritePath
                                35, // cost
                                1.2f, // costIncrease
                                () -> player.upgradeProjectileDamage(), true));

                // projectile speed upgrade
                upgradePool.add(new Upgrade(
                                "Magic Speed",
                                "Increase the speed of your magic",
                                "ProjectileSpeed", // spritePath
                                40, // cost
                                1.2f, // costIncrease
                                () -> player.upgradeProjectileSpeed(), true));

                // projectile size upgrade
                upgradePool.add(new Upgrade(
                                "Magic Size",
                                "Increase the size of your magic",
                                "MagicSize", // spritePath
                                30, // cost
                                1.3f, // costIncrease
                                () -> player.upgradeProjectileSize(), true));

                // ability cooldown upgrade
                upgradePool.add(new Upgrade(
                                "Magic Cooldown",
                                "Reduce the cooldown of abilities",
                                "Cooldown", // spritePath
                                45, // cost
                                1.7f, // costIncrease
                                () -> player.upgradeAbilityCooldown(), true));

                // more projectiles upgrade
                upgradePool.add(new Upgrade(
                                "More Projectiles",
                                "Fire additional projectiles",
                                "ProjectileBonus", // spritePath
                                45, // cost
                                2, // costIncrease
                                () -> player.upgradeProjectileBonus(), true));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Lenses",
                                "Increase pickup range",
                                "Lenses", // spritePath
                                25, // cost
                                1.4f, // costIncrease
                                () -> player.upgradePickupRange(), true));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Jar Of Lightning",
                                "We bring the boom!",
                                "Abilities/JarOfLightning", // spritePath
                                250, // cost
                                1f, // costIncrease
                                () -> player.newAbility(AbilityTypes.jarOfLightning), false));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Mask Of Flames",
                                "Remember to social distance",
                                "Abilities/MaskOfFlames", // spritePath
                                250, // cost
                                1f, // costIncrease
                                () -> player.newAbility(AbilityTypes.maskOfFlames), false));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Rock",
                                "It calls for it's brothers",
                                "Abilities/Rock", // spritePath
                                250, // cost
                                1f, // costIncrease
                                () -> player.newAbility(AbilityTypes.rock), false));

                // pickup range upgrade
                upgradePool.add(new Upgrade(
                                "Soul Egg",
                                "Who created this thing?",
                                "Abilities/SoulEgg", // spritePath
                                250, // cost
                                1f, // costIncrease
                                () -> player.newAbility(AbilityTypes.falcon), false));

                return upgradePool;
        }

}
