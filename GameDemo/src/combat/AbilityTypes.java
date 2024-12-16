package combat;

import java.util.ArrayList;

import objects.GameObject;
import objects.Projectile;
import objects.ProjectileType;

public class AbilityTypes {

    public static Ability electric = new Ability(ProjectileType.LIGHTNING, 0, 1) {
        public void doAbility(GameObject creator, float direction, ArrayList<Projectile> projectiles) {
            for (int i = 0; i <= this.numberOfProjectiles; i++) {
                projectiles.add(new Projectile(creator, direction, this.projectileType));
            }
        }
    };

    public static Ability earth = new Ability(ProjectileType.EARTH, 0, 1) {
        public void doAbility(GameObject creator, float direction, ArrayList<Projectile> projectiles) {
            for (int i = 0; i <= this.numberOfProjectiles; i++) {
                System.out.println("you did it");
                projectiles.add(new Projectile(creator, direction, this.projectileType));
            }
        }
    };

}
