import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Shop1 extends JFrame implements ActionListener {
    JButton closeButton;
    JButton a = new JButton("buy");
    JButton g = new JButton("buy");
    JButton b = new JButton("buy");
    JButton c = new JButton("buy");
    JButton d = new JButton("buy");
    JButton h = new JButton("buy");
    static JFrame f = new JFrame();// creating JFrame  
    JLabel imageLabel; // JLabel to display image

    public static void main(String[] args) {
        new Shop1();
    }

    Shop1() {
        JTextArea area = new JTextArea("Arcana's Edge");
        area.setBounds(500, 50, 175, 35);
        area.setFont(new Font("Comic Sans", Font.BOLD, 25));
        area.setBackground(Color.cyan);
        area.setEditable(false);

        JTextArea stats = new JTextArea("Stats");
        stats.setBounds(0, 200, 200, 300);
        stats.setFont(new Font("Comic Sans", Font.BOLD, 25));
        stats.setBackground(Color.black);
        stats.setEditable(false);

        b.setBackground(new Color(160, 32, 240)); //creating JButton  
        b.setBounds(285, 900, 75, 20); // x axis, y axis, width, height  

        c.setBackground(new Color(160, 32, 240));
        c.setBounds(570, 900, 75, 20);

        d.setBackground(new Color(160, 32, 240));
        d.setBounds(855, 900, 75, 20);

        h.setBackground(new Color(160, 32, 240));
        h.setBounds(285, 450, 75, 20);

        a.setBackground(new Color(160, 32, 240));
        a.setBounds(570, 450, 75, 20);

        g.setBackground(new Color(160, 32, 240));
        g.setBounds(855, 450, 75, 20);

        closeButton = new JButton("Close");
        closeButton.setBounds(1000, 100, 70, 20);

        closeButton.addActionListener(this);
        a.addActionListener(this);
        b.addActionListener(this);
        c.addActionListener(this);
        d.addActionListener(this);
        h.addActionListener(this);
        g.addActionListener(this);

        f.add(stats);
        f.add(area);
        f.add(closeButton);
        f.add(h); // adding buttons in JFrame  
        f.add(a);
        f.add(g);
        f.add(b); 
        f.add(c);
        f.add(d);

        // Load and display the image
        imageLabel = new JLabel(loadImage("fire.jfif"));
        imageLabel.setBounds(100, 100, 450, 525); // Adjust the position and size of the image
        f.add(imageLabel);

        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setUndecorated(true);
        f.setBackground(new Color(0, 0, 0, 63));
        f.setSize(1200, 1000); // 1100 width and 1000 height  
        f.setLayout(null);
        f.setLocationRelativeTo(null); // using no layout managers  
        f.setVisible(true); // making the frame visible  
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) { // Handle skip button click
            this.dispose();
            System.exit(0); // Close the shop window
        }
        if (e.getSource() == a) {
            a.setText("Bought");
        } else if (e.getSource() == b) {
            b.setText("Bought");
        } else if (e.getSource() == c) {
            c.setText("Bought");
        } else if (e.getSource() == d) {
            d.setText("Bought");
        } else if (e.getSource() == h) {
            h.setText("Bought");
        } else if (e.getSource() == g) {
            g.setText("Bought");
        }
    }

    // Modified loadImage to return ImageIcon
    static ImageIcon loadImage(String filename) {
        BufferedImage img = null;
        try {
            // Use the passed filename to load the image
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            // Print the error for debugging
            System.out.println(e.toString());
            // Show an error dialog to the user
            JOptionPane.showMessageDialog(null, "An image failed to load: " + filename, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return img == null ? null : new ImageIcon(img); // Return an Image
}
}