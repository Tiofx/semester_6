package lab6.main;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;

public class Main implements GLEventListener {
    protected static int screenSize = 600;

    protected LevyCurve levyCurve = new LevyCurve(-0.5f, -0.25f, 0.5f, -0.25f);

    public LevyCurve getLevyCurve() {
        return levyCurve;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(1f, 1f, 1f, 1f);
//        gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);

        gl.glColor3f(0f, 0f, 0f);
        levyCurve.draw(gl);
    }

//    public void draw(float x1, float y1, float x2, float y2, int n, GL2 gl) {
//        if (n == 0) {
//            drawLine(x1, y1, x2, y2, gl);
//        } else {
//            float xx = (x1 + x2) / 2f - (y2 - y1) / 2f;
//            float yy = (y1 + y2) / 2f + (x2 - x1) / 2f;
//
//            draw(x1, y1, xx, yy, n - 1, gl);
//            draw(xx, yy, x2, y2, n - 1, gl);
//        }
//    }
//
//    private void drawLine(float x1, float y1, float x2, float y2, GL2 gl) {
//        gl.glBegin(GL2.GL_LINES);
//
//        gl.glVertex2f(x1, y1);
//        gl.glVertex2f(x2, y2);
//
//        gl.glEnd();
//    }


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
        glcanvas.repaint();

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

}