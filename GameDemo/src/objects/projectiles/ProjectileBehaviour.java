package objects.projectiles;

import objects.GameObject;

public interface ProjectileBehaviour {
    void created(Projectile projectile);
    void update(Projectile projectile);
    void hit(Projectile projectile, GameObject other);
    void cooldownFinished(Projectile projectile);
}
    
