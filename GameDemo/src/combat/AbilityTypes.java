package combat;

import java.util.ArrayList;
import objects.GameObject;
import objects.projectiles.Projectile;
import objects.projectiles.ProjectileType;

public class AbilityTypes {

    public static Ability electric = new Ability("Electric", ProjectileType.LIGHTNING, 12, 5) {
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

    public static Ability earth = new Ability("Earth", ProjectileType.EARTH, 1, 7) {
        @Override
        public void doAbility(GameObject creator, ArrayList<Projectile> projectiles) {
            for (int i = 0; i < (this.numberOfProjectiles + creator.getProjectileBonus()); i++) {
                projectiles.add(new Projectile(creator, 90, this.projectileType));
            }
        }
    };

}
