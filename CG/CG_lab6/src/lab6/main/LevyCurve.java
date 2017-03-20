package lab6.main;

import com.jogamp.opengl.GL2;
import lab2.main.figures.Vertex3f;

import java.util.ArrayList;
import java.util.List;

public class LevyCurve {
    protected float x1, y1, x2, y2;
    protected int iterationNumber;

    protected List<Vertex3f> vertex3fs = new ArrayList<>();

    public LevyCurve(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setIterationNumber(int iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public void draw(GL2 gl) {
        vertex3fs.clear();
        formLines(x1, y1, x2, y2, iterationNumber, gl);

        gl.glColor3f(0, 0, 0);
        gl.glBegin(GL2.GL_LINES);

        vertex3fs.forEach(vertex3f -> gl.glVertex2f(vertex3f.x, vertex3f.y));

        gl.glEnd();
    }

    protected void formLines(float x1, float y1, float x2, float y2, int n, GL2 gl) {
        if (n == 0) {
            vertex3fs.add(new Vertex3f(x1, y1));
            vertex3fs.add(new Vertex3f(x2, y2));
        } else {
            float xx = (x1 + x2) / 2 - (y2 - y1) / 2;
            float yy = (y1 + y2) / 2 + (x2 - x1) / 2;

            formLines(x1, y1, xx, yy, n - 1, gl);
            formLines(xx, yy, x2, y2, n - 1, gl);
        }
    }
}
