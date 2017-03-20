package lab5.main;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import lab2.main.figures.Vertex3f;
import lab4.util.Drawable;

import java.util.ArrayList;
import java.util.List;

public class Function implements Drawable {
    protected List<Vertex3f> points1 = new ArrayList<>();
    protected List<Vertex3f> points2 = new ArrayList<>();
    protected boolean isFirstList = true;
    protected float a, n;

    protected float dx;
    protected float xBegin, xEnd, xCurrent;

    public Function(float a, float n) {
        this.a = a;
        this.n = n;
    }

    public void setA(float a) {
        this.a = a;
    }

    public void setN(float n) {
        this.n = n;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public void setxBegin(float xBegin) {
        xCurrent = xBegin;
        this.xBegin = xBegin;
    }

    public void setxEnd(float xEnd) {
        this.xEnd = xEnd;
    }

    public float function(float x) {
        return (float) (a * Math.pow(x, -n));
    }

    public void reset() {
        xCurrent = xBegin;
        isFirstList = true;
        points1.clear();
        points2.clear();
    }

    public void nextFrame() {
        if (!hasNext()) {
            return;
        }

        float y = function(xCurrent);
        if (Math.abs(y) < 20) {
            if (isFirstList) {
                points1.add(new Vertex3f(xCurrent, y));
            } else {
                points2.add(new Vertex3f(xCurrent, y));
            }
        } else {
            isFirstList = false;
        }

        xCurrent += dx;
    }

    public boolean hasNext() {
        return xCurrent <= xEnd;
    }

    @Override
    public void display(GLAutoDrawable drawable, float coordinateSize) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glEnable(GL.GL_LINE_WIDTH);
        gl.glLineWidth(10f);
        gl.glColor3f(1f, 0f, 0f);

        gl.glBegin(GL2.GL_LINE_STRIP);
        points1.stream()
                .forEach(vertex3f -> gl.glVertex2f(vertex3f.x, vertex3f.y));
        gl.glEnd();

        gl.glBegin(GL2.GL_LINE_STRIP);
        points2.stream()
                .forEach(vertex3f -> gl.glVertex2f(vertex3f.x, vertex3f.y));
        gl.glEnd();

        gl.glLineWidth(1f);
        gl.glDisable(GL.GL_LINE_WIDTH);
    }
}
