package combat;

import java.util.ArrayList;
import objects.GameObject;
import objects.projectiles.Projectile;
import objects.projectiles.ProjectileType;

public class Ability {

    protected final String name;
    protected final int numberOfProjectiles;
    protected final ProjectileType projectileType;
    protected final float cooldown; // in seconds
    protected int cooldownTimer;

    Ability(String name, ProjectileType projectileType, int numberofProjectiles, float cooldown) {
        this.name = name;
        this.projectileType = projectileType;
        this.numberOfProjectiles = numberofProjectiles;
        this.cooldown = (int) (cooldown * 60);
        this.cooldownTimer = (int) cooldown;
    }

    public void doAbility(GameObject creator, ArrayList<Projectile> projectiles) {

    }

    public void update(GameObject creator, ArrayList<Projectile> projectiles) {
        this.cooldownTimer--;
        if (this.cooldownTimer <= 0) {
            this.cooldownTimer = (int) cooldown;
            this.doAbility(creator, projectiles);
        }
    }

    public int getCooldownTimer() {
        return this.cooldownTimer;
    }

}
