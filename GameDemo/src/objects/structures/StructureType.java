package objects.structures;

import java.sql.Struct;

public enum StructureType implements StructureBehaviour {

    TRAP(50, 50, 10, 1, "trap", 5, 5) {
        @Override
        public void created(Structure structure) {

        }

        @Override
        public void update(Structure structure) {
        }

        @Override
        public void hit(Structure structure) {
        }
    };

    // physical information
    protected final int width;
    protected final int height;
    protected final float contactDamage;
    protected final float cooldown; // in seconds

    // visual information
    protected final String spritePath;
    protected final int idleAnimationLength;
    protected final int activeAnimationLength;

    StructureType(int width, int height, int contactDamage, int cooldown, String spritePath, int idleAnimationLength,
            int activeAnimationLength) {
        this.width = width;
        this.height = height;
        this.contactDamage = contactDamage;
        this.spritePath = spritePath;
        this.idleAnimationLength = idleAnimationLength;
        this.activeAnimationLength = activeAnimationLength;

        this.cooldown = cooldown * 60; // seconds * fps = frames
    }

    // Getters

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getContactDamage() {
        return this.contactDamage;
    }

    public String getSpritePath() {
        return this.spritePath;
    }

    public int getIdleAnimationLength() {
        return this.idleAnimationLength;
    }

    public int getActiveAnimationLength() {
        return this.activeAnimationLength;
    }

}
