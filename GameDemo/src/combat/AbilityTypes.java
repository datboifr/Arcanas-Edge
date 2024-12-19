package combat;

import java.util.ArrayList;
import objects.GameObject;
import objects.projectiles.Projectile;
import objects.projectiles.ProjectileType;

public class AbilityTypes {

    public static Ability electric = new Ability("Electric", ProjectileType.LIGHTNING, 6, 1) {
        @Override
        public void doAbility(GameObject creator, float direction, ArrayList<Projectile> projectiles) {
            float startAngle =- 15 * (this.numberOfProjectiles / 2);
            for (int i = 0; i < this.numberOfProjectiles; i++) {
                projectiles.add(new Projectile(creator, direction + (startAngle + (15 * i)), this.projectileType));
            }
        }
    };

    public static Ability earth = new Ability("Earth", ProjectileType.EARTH, 1, 1) {
        @Override
        public void doAbility(GameObject creator, float direction, ArrayList<Projectile> projectiles) {
            for (int i = 0; i < this.numberOfProjectiles; i++) {
                System.out.println("you did it");
                projectiles.add(new Projectile(creator, direction, this.projectileType));
            }
        }
    };

}
