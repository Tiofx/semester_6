package lab4.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static javax.swing.SwingConstants.CENTER;

class BackgroundPanel extends JPanel {
    // The Image to store the background image in.
    Image img;

    public BackgroundPanel() {
        // Loads the background image and stores in img object.
//        img = Toolkit.getDefaultToolkit().createImage("/Users/andrej/IdeaProjects/semester_6/CG/resource/jogl_j3d_logo.png");
//        img = Toolkit.getDefaultToolkit().createImage("jogl_j3d_logo.png");

        try {
            img = ImageIO.read(new File("/Users/andrej/IdeaProjects/semester_6/CG/resource/jogl_j3d_logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setLayout(new GridLayout(4, 1));
//        new ImageIcon("/Users/andrej/IdeaProjects/semester_6/CG/resource/jogl_j3d_logo.png");
        JLabel label = new JLabel("hello");
        label.setVisible(true);
        label.setText("hello");
        add(label);
        add(new JLabel("hello"));
        add(new JLabel("hello"));
        add(new JLabel("hell,", new ImageIcon("/Users/andrej/IdeaProjects/semester_6/CG/resource/jogl_j3d_logo.png"), CENTER));
    }

    @Override
    public void paintComponent(Graphics g) {
        // Draws the img to the BackgroundPanel.
        g.drawImage(img, 0, 0, null);
    }
}