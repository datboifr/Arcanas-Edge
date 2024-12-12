package combat;

import objects.GameObject;
import objects.ProjectileType;

public class Attack {

    private final int numberOfProjectiles;
    private final ProjectileType projectileType;

    Attack(ProjectileType projectileType, int numberofProjectiles) {
        this.projectileType = projectileType;
        this.numberOfProjectiles = numberofProjectiles;
    }

    public void doAttack(GameObject creator, int direction) {

    }
    
}
