import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;

/**
 * This program demonstrates geometric primitives and their attributes.
 *
 * @author Kiet Le (Java port) Ported to JOGL 2.x by Claudio Eduardo Goes
 */
public class LinesDemo//
//        extends GLSkeleton<GLJPanel>
        implements GLEventListener, KeyListener {
    private GLU glu;

//    public static void main(String[] args) {
//        final GLCanvas glcanvas = createCanvas();
//
//        final JFrame frame = new JFrame("Basic Frame");
//
//        frame.getContentPane().add(glcanvas);
//        frame.setSize(frame.getContentPane().getPreferredSize());
//        frame.setVisible(true);
//
//        frame.repaint();
//    }

//    public static GLCanvas createCanvas() {
//        final GLProfile profile = GLProfile.get(GLProfile.GL2);
//        GLCapabilities capabilities = new GLCapabilities(profile);
//
//        final GLCanvas glcanvas = new GLCanvas(capabilities);
//        LinesDemo b = new LinesDemo();
//        glcanvas.addGLEventListener(b);
////        glcanvas.setSize(screenSize, screenSize);
//
//        return glcanvas;
//    }

    //    @Override
    protected GLJPanel createDrawable() {
        GLCapabilities caps = new GLCapabilities(null);
        //
        GLJPanel panel = new GLJPanel(caps);
        panel.addGLEventListener(this);
        panel.addKeyListener(this);
        return panel;
    }

    public void run() {
        GLJPanel demo = createDrawable();

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("LinesDemo");
        frame.setSize(400, 150);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(demo);
        frame.setVisible(true);
        demo.requestFocusInWindow();
    }

    public static void main(String[] args) {
        new LinesDemo().run();
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        //
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL2.GL_FLAT);
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        //
        int i;

        float coordinateSize = 300;

//        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//        gl.glClearColor(1f, 1f, 1f, 1f);


//        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
//        gl.glLoadIdentity();
//        gl.glOrtho(-coordinateSize, coordinateSize, -coordinateSize, coordinateSize, -1, 1);
        /* select white for all LinesDemo */
        gl.glColor3f(1.0f, 1.0f, 1.0f);


        /* in 1st row, 3 LinesDemo, each with a different stipple */
        gl.glEnable(GL2.GL_LINE_STIPPLE);

        gl.glEnable(GL2.GL_POINT_SMOOTH);
        gl.glBegin(GL2.GL_POINTS);
        gl.glPointSize(10e5f);
        gl.glVertex2i(200, 200);
        gl.glEnd();
        gl.glDisable(GL2.GL_POINT_SMOOTH);

        gl.glLineStipple(1, (short) 0x0101); /* dotted */
        drawOneLine(gl, 50.0f, 125.0f, 150.0f, 125.0f);
        gl.glLineStipple(1, (short) 0x00FF); /* dashed */
        drawOneLine(gl, 150.0f, 125.0f, 250.0f, 125.0f);
        drawOneLine(gl, 150.0f, 125.0f, 250.0f, 200f);
        gl.glLineStipple(1, (short) 0x1C47); /* dash/dot/dash */
        drawOneLine(gl, 250.0f, 125.0f, 350.0f, 125.0f);

        /* in 2nd row, 3 wide LinesDemo, each with different stipple */
        gl.glLineWidth(5.0f);
        gl.glLineStipple(1, (short) 0x0101); /* dotted */
        drawOneLine(gl, 50.0f, 100.0f, 150.0f, 100.f);
        gl.glLineStipple(1, (short) 0x00FF); /* dashed */
        drawOneLine(gl, 150.0f, 100.0f, 250.0f, 100.0f);
        gl.glLineStipple(1, (short) 0x1C47); /* dash/dot/dash */
        drawOneLine(gl, 250.0f, 100.0f, 350.0f, 100.0f);
        gl.glLineWidth(1.0f);

        /* in 3rd row, 6 LinesDemo, with dash/dot/dash stipple */
        /* as part of a single connected line strip */
        gl.glLineStipple(1, (short) 0x1C47); /* dash/dot/dash */
        gl.glBegin(GL.GL_LINE_STRIP);
        for (i = 0; i < 7; i++)
            gl.glVertex2f(50.0f + ((float) i * 50.0f), 75.0f);
        gl.glEnd();

        /* in 4th row, 6 independent LinesDemo with same stipple */
        for (i = 0; i < 6; i++) {
            drawOneLine(gl, 50.0f + ((float) i * 50.0f), 50.0f,
                    50.0f + ((float) (i + 1) * 50.0f), 50.0f);
        }

        /* in 5th row, 1 line, with dash/dot/dash stipple */
        /* and a stipple repeat factor of 5 */
        gl.glLineStipple(5, (short) 0x1C47); /* dash/dot/dash */
        drawOneLine(gl, 50.0f, 25.0f, 350.0f, 25.0f);

        gl.glDisable(GL2.GL_LINE_STIPPLE);
        gl.glFlush();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL2 gl = drawable.getGL().getGL2();
        //
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0.0, (double) w, 0.0, (double) h);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
                               boolean deviceChanged) {
    }

    private void drawOneLine(GL2 gl, float x1, float y1, float x2, float y2) {
//        gl.glBegin(GL.GL_LINES);

        gl.glVertex2f((x1), (y1));
        gl.glVertex2f((x2), (y2));

//        gl.glEnd();
    }

    public void keyTyped(KeyEvent key) {
    }

    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;

            default:
                break;
        }
    }

    public void keyReleased(KeyEvent key) {
    }

    public void dispose(GLAutoDrawable arg0) {

    }

}