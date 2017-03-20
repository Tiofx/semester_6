import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class TEST extends JFrame {

    int x, y;

    private Image dbgImage;
    private Graphics dbg;

    public TEST() {
        addKeyListener(new AL());
        setTitle("CIRCLE THING");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        x = 150;
        y = 150;
    }

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            if(keyCode == e.VK_D) {
                x++;
            }
            if(keyCode == e.VK_A) {
                x--;
            }
            if(keyCode == e.VK_W) {
                y--;
            }
            if(keyCode == e.VK_S) {
                y++;
            }

            repaint();
        }

        public void keyReleased(KeyEvent e) {}
    }

    public void paint(Graphics g) {
        dbgImage = createImage (getWidth(), getHeight());
        dbg = dbgImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbgImage, 0, 0, this);
    }

    public void paintComponent(Graphics g) {
        g.fillOval(x, y, 90, 100);
        repaint();  
    }

    public static void main(String[] args) {
        new TEST(); 
    }
}
