package combat;

import java.util.ArrayList;

import objects.GameObject;
import objects.projectiles.Projectile;
import objects.projectiles.ProjectileType;

public class Ability {

    protected final String name;
    protected final int numberOfProjectiles;
    protected final ProjectileType projectileType;
    protected final float cooldown; //in seconds

    Ability(String name, ProjectileType projectileType, int numberofProjectiles, int cooldown) {
        this.name = name;
        this.projectileType = projectileType;
        this.numberOfProjectiles = numberofProjectiles;
        this.cooldown = cooldown * 60;
    }

    public void doAbility(GameObject creator, float directionLiteral, ArrayList<Projectile> projectiles) {

    }
    
}
