package objects;

public class AnimationData {
    private String path;
    private int length;

    public AnimationData(String path, int length) {
        this.path = path;
        this.length = length;
    }

    public String getPath() {
        return path;
    }

    public int getLength() {
        return length;
    }
}
