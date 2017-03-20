package lab4;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import lab4.main.Lab4Scene;

import javax.swing.*;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;

public class Main implements GLEventListener {

    private Lab4Scene scene = new Lab4Scene();
    private lab3.main.Main jogl;
    private GLCanvas canvas;


    protected static int screenSize = 600;
    protected float coordinateSize = 11;


    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
//        gl.glClearColor(1f, 1f, 1f, 1f);

//        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
//        gl.glLoadIdentity();
//        gl.glOrtho(-coordinateSize, coordinateSize, -coordinateSize, coordinateSize, -1, 1);

        scene.setCoordinateSize(coordinateSize);
        scene.display(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        //method body
    }

    @Override
    public void init(GLAutoDrawable arg0) {
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

        final JFrame frame = new JFrame("Basic Frame");

        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);

        frame.repaint();

        new Thread(() -> {
            while (true) {
                glcanvas.repaint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public Lab4Scene getScene() {
        return scene;
    }
}
