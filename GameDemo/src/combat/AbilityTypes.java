package combat;

import java.util.ArrayList;
import java.util.Random;

import objects.GameObject;
import objects.enemies.Enemy;
import objects.projectiles.Projectile;
import objects.projectiles.ProjectileType;

public class AbilityTypes {

    static Random random = new Random();

    public static Ability electric = new Ability("Electric Blast", ProjectileType.LIGHTNING, 12, 5) {
        @Override
        public void doAbility(GameObject creator, ArrayList<Projectile> projectiles) {
            // Start angle adjusted to ensure full circle distribution
            float startDirection = -180 + (360f / this.numberOfProjectiles) / 2;
            for (int i = 0; i < (this.numberOfProjectiles + creator.getProjectileBonus()); i++) {
                // Each projectile is spaced equally within the full 360 degrees
                projectiles.add(new Projectile(creator,
                        creator.getDirection() + startDirection + (360f / this.numberOfProjectiles) * i,
                        this.projectileType));
            }
        }

    };

    public static Ability earth = new Ability("Earth ", ProjectileType.EARTH, 1, 7) {
        @Override
        public void doAbility(GameObject creator, ArrayList<Projectile> projectiles) {
            for (int i = 0; i < (this.numberOfProjectiles + creator.getProjectileBonus()); i++) {
                if (i < 6)
                    projectiles.add(new Projectile(creator, 90, this.projectileType));
            }
        }
    };

    public static Ability falcon = new Ability("Falcon Whisper", ProjectileType.FALCON, 1, 5.5f) {
        @Override
        public void doAbility(GameObject creator, ArrayList<Projectile> projectiles) {
            for (int i = 0; i < (this.numberOfProjectiles + creator.getProjectileBonus()); i++) {
                if (i < 3)
                    projectiles.add(new Projectile(creator, 90, this.projectileType));
            }
        }
    };

    public static Ability fire = new Ability("Fire Shot", ProjectileType.FIRE, 3, 2f) {
        @Override
        public void doAbility(GameObject creator, ArrayList<Projectile> projectiles) {
            int direction = random.nextInt(360);
            for (int i = 0; i < (this.numberOfProjectiles + creator.getProjectileBonus()); i++) {
                if (i < 6)
                    projectiles.add(new Projectile(creator, direction + (15 * i), this.projectileType));
            }
        }
    };

    public static Ability blast = new Ability("Blast", ProjectileType.BASIC, 1, 2f) {
        @Override
        public void doAbility(GameObject creator, ArrayList<Projectile> projectiles) {
            for (int i = 0; i < (this.numberOfProjectiles + creator.getProjectileBonus()); i++) {
                if (i < 6)
                    projectiles.add(new Projectile(creator, creator.getDirection(), this.projectileType));
            }
        }
    };

    /**
     * SCRAPPED
     * public static Ability shark = new Ability("Shark Attack",
     * ProjectileType.SHARK, 1, 3f) {
     * 
     * @Override
     *           public void doAbility(GameObject creator, ArrayList<Projectile>
     *           projectiles) {
     *           for (int i = 0; i < (this.numberOfProjectiles +
     *           creator.getProjectileBonus()); i++) {
     *           if (i < 6)
     *           projectiles.add(new Projectile(creator, 0, this.projectileType));
     *           }
     *           }
     *           };
     **/

}
