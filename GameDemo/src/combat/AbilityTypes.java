package combat;

import java.util.ArrayList;
import java.util.Random;

import objects.GameObject;
import objects.Projectile;
import objects.ProjectileType;

@SuppressWarnings("unused")
public class AbilityTypes {

    static Random random = new Random();

    public static Ability electric = new Ability(ProjectileType.LIGHTNING, 1, 1) {
        public void doAbility(GameObject creator, int direction, ArrayList<Projectile> projectiles) {
            for (int i = 0; i <= this.numberOfProjectiles; i++) {
                projectiles.add(new Projectile(creator, direction, this.projectileType));
            }
        }
    };

}
