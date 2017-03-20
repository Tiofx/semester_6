package lab4.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import lab4.util.Complex;

import java.awt.*;
import java.util.List;

public class Vertex3f extends lab2.main.figures.Vertex3f
        implements Complex<lab2.main.figures.Vertex3f> {

    public Vertex3f() {
    }

    public Vertex3f(float x, float y) {
        super(x, y);
    }

    public Vertex3f(String name, float x, float y) {
        super(name, x, y);
    }

    public Vertex3f(float x, float y, float h) {
        super(x, y, h);
    }

    public Vertex3f(String name, float x, float y, float h) {
        super(name, x, y, h);
    }

    @Override
    public void display(GLAutoDrawable drawable, float coordinateSize) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glBegin(GL2.GL_POINTS);
        gl.glVertex2f(x, y);
        gl.glEnd();
    }


    @Override
    public void getVertexes(List<lab2.main.figures.Vertex3f> result) {
        result.add(this);
    }

    @Override
    public Color getColor() {
        return null;
    }
}
