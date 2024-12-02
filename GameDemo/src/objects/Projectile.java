package objects;

public class Projectile extends GameObject {

    // private ProjectileBehaviour behaviour;
    private float direction;

    private float speed;

    Projectile(Player player, float direction) {
        super(player.x, player.y, 10, 10);
        this.speed = player.getProjectileSpeed();
        this.direction = direction;
    }

    public void update() {
        double dx = Math.cos(Math.toRadians(direction)) * speed;
        double dy = Math.sin(Math.toRadians(direction)) * speed;
        x += dx;
        y += dy;
    }

}
