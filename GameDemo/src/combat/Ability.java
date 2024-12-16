package combat;

import java.util.ArrayList;

import objects.GameObject;
import objects.Projectile;
import objects.ProjectileType;

public class Ability {

    protected final int numberOfProjectiles;
    protected final ProjectileType projectileType;
    protected final float cooldown; //in seconds

    Ability(ProjectileType projectileType, int numberofProjectiles, int cooldown) {
        this.projectileType = projectileType;
        this.numberOfProjectiles = numberofProjectiles;
        this.cooldown = cooldown * 60;
    }

    public void doAbility(GameObject creator, float directionLiteral, ArrayList<Projectile> projectiles) {

    }
    
}
