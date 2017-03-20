package lab1.main;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.awt.TextRenderer;
import lab1.DrawMethods;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;


public class Main implements GLEventListener {
    protected static int screenSize = 600;
    protected boolean clear = true;
    protected boolean joglDraw = false;
    protected boolean ddaDraw = false;

    protected TextRenderer renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));

    @Override
    public void display(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.9f, 0.5f, 0.9f, 0.0f);

        if (clear) {
            return;
        }

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, screenSize, 0, screenSize, -1, 1);

        if (ddaDraw) {
            drawImageByDDA(gl);
        } else if (joglDraw) {
            drawImageByJogl(gl);
        }
    }

    final int n = 4, scale = 15;
    final int xUp = 20, yUp = 200;

    public void drawImageByJogl(GL2 gl) {
        Vector<Point> quads = new Vector<>();
        Vector<Point> lines = new Vector<>();

        for (int i = 0; i < n; i++) {
            quads.addAll(DrawMethods.StandardMethod.rectangular(
                    10 * i * scale, 0 * scale, 6 * scale, 4 * scale));

            if (i != n - 1) {
                lines.addAll(DrawMethods.StandardMethod.twoArrow(
                        10 * i * scale + 6 * scale, 0 * scale, 4 * scale, 4 * scale));
            }
        }

        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f(1f, 1f, 1f);
        quads.forEach(point -> gl.glVertex2i(point.x + xUp, point.y + yUp));
        gl.glColor3f(0f, 0f, 0f);
        lines.forEach(point -> gl.glVertex2i(point.x + xUp, point.y + yUp));
        gl.glEnd();
    }


    public void drawImageByDDA(GL2 gl) {
        Vector<Point> result = new Vector<>();

        for (int i = 0; i < n; i++) {
            result.addAll(DrawMethods.rectangular(
                    10 * i * scale, 0 * scale, 6 * scale, 4 * scale));

            if (i != n - 1) {
                result.addAll(DrawMethods.twoArrow(
                        10 * i * scale + 6 * scale, 0 * scale, 4 * scale, 4 * scale));
            }
        }

        gl.glBegin(GL2.GL_POINTS);
        gl.glColor3f(0.5f, 0.3f, 0.1f);
        result.forEach(point -> gl.glVertex2i(point.x + xUp, point.y + yUp));
        gl.glEnd();
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        //method body
    }

    @Override
    public void init(GLAutoDrawable arg0) {
        // method body
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        glAutoDrawable.getGL().getGL2().glViewport(i, i1, i2, i3);
//        glAutoDrawable.getGL().getGL2().glViewport(0, 0, 600, 600);
    }

    public static GLCanvas createCanvas() {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Main b = new Main();
        glcanvas.addGLEventListener(b);
        glcanvas.setSize(screenSize, screenSize);

        return glcanvas;
    }

    public static void main(String[] args) {
        final GLCanvas glcanvas = createCanvas();

        final JFrame frame = new JFrame(" Basic Frame");

        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);

        frame.repaint();
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public void setJoglDraw(boolean joglDraw) {
        this.joglDraw = joglDraw;
        clear = false;
        ddaDraw = false;
    }


    public void setDdaDraw(boolean ddaDraw) {
        this.ddaDraw = ddaDraw;
        clear = false;
        joglDraw = false;
    }
}