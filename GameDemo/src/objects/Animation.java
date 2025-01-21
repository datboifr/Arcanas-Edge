package objects;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// Allows for each gameObject to have multiple preloaded animations
public class Animation {
    private String name, path;
    private int length;
    private BufferedImage[] frames;

    public Animation(String name, String path, int length) {
        this.name = name;
        this.path = path;
        this.length = length;
        frames = new BufferedImage[length];
    }

    public void load() {
        BufferedImage loadedFrame;
        for (int i = 0; i < length; i++) {
            String framePath = "/res/" + path + (i + 1) + ".png"; // Construct the path for each frame
            System.out.println("Attempting to load: " + framePath); // Log the frame path
            try {
                loadedFrame = ImageIO
                        .read(getClass()
                                .getResourceAsStream(framePath));
                frames[i] = loadedFrame;
            } catch (IOException e) {
                System.out.println("Error loading frame: " + path + (i + 1) + ".png");
            }
        }

    }

    // getters

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return path;
    }

    public int getLength() {
        return length;
    }

    public BufferedImage getFrame(int frame) {
        return frames[frame];
    }
}
