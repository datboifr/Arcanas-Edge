package combat;

import objects.GameObject;
import objects.ProjectileType;

public class AttackTypes {
    
    public static Attack electric = new Attack(ProjectileType.LIGHTNING, 5) {
        public void doAttack(GameObject creator, int direction) {
            
        }
    };

}
