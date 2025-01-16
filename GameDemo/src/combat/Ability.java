package combat;

import java.util.ArrayList;
import objects.GameObject;
import objects.projectiles.Projectile;
import objects.projectiles.ProjectileType;

public class Ability {

    protected final String name;
    protected final int numberOfProjectiles;
    protected final ProjectileType projectileType;
    protected final int shotCooldown; // frames between each projectile

    protected float direction;

    // ability cooldown
    protected final float cooldown; // in seconds
    protected int cooldownTimer;

    // projectile logistics
    protected int numberOfProjectilesShot;
    protected boolean active = false;
    protected int shootCooldownTimer;

    Ability(String name, ProjectileType projectileType, int numberofProjectiles, float cooldown,
            int shotCooldown) {
        this.name = name;
        this.projectileType = projectileType;
        this.numberOfProjectiles = numberofProjectiles;

        this.cooldown = (int) (cooldown * 60);
        this.cooldownTimer = (int) cooldown;

        this.shotCooldown = shotCooldown;
        shootCooldownTimer = shotCooldown;
    }

    public void updateCooldown(GameObject creator) {
        if (active) {
            if (!(numberOfProjectilesShot == (numberOfProjectiles + creator.getProjectileBonus()))) {
                shootCooldownTimer--;
                if (shootCooldownTimer == 0) {
                    this.updateAbility(creator, creator.getGamePanel().getProjectiles());
                    numberOfProjectilesShot++;
                    shootCooldownTimer = shotCooldown;
                }
            } else {
                numberOfProjectilesShot = 0;
                active = false;
            }
        } else {
            this.cooldownTimer--;
            if (this.cooldownTimer <= 0) {
                this.cooldownTimer = (int) (cooldown * creator.getAbilityCooldown());
                active = true;
                setupAbility(creator);
            }
        }
    }

    protected void updateAbility(GameObject creator, ArrayList<Projectile> projectiles) {
    }

    protected void setupAbility(GameObject creator) {
    }
}
