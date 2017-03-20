package lab5.main;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import lab2.main.figures.Vertex3f;
import lab4.util.Drawable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AngleFunction implements Drawable {
    protected float a, b, lambda;

    protected float dAngle;
    protected float beginAngle = 0, endAngle, currentAngle;

    protected List<Vertex3f> points = new ArrayList<>();

    private static final float eps = 1e-4f;
    private float xStart, yStart;

    public AngleFunction(float a, float b, float lambda) {
        this.a = a;
        this.b = b;
        this.lambda = lambda;

        xStart = functionX(beginAngle);
        yStart = functionY(beginAngle);
    }

    public void setA(float a) {
        this.a = a;

        endAngle = (float) (getRotateNumber(a, b) * Math.toRadians(360)) + beginAngle;
    }

    public void setB(float b) {
        this.b = b;

        endAngle = (float) (getRotateNumber(a, b) * Math.toRadians(360)) + beginAngle;
    }

    public void setLambda(float lambda) {
        this.lambda = lambda;
    }

    public void setBeginAngle(float beginAngle) {
        this.beginAngle = beginAngle;
        currentAngle = beginAngle;

        endAngle = (float) ((float) (getRotateNumber(a, b) * Math.toRadians(360)) + beginAngle + 0.1);
    }

    private double getRotateNumber(float a, float b) {
        return a / gcdThing((int) a, (int) b);
    }

    private static int gcdThing(int a, int b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    public void setdAngle(float dAngle) {
        this.dAngle = dAngle;
    }

    public float functionX(float angle) {
        return (float) ((b - a) * Math.cos(angle) + lambda * a * Math.cos((b - a) * angle / a));
    }

    public float functionY(float angle) {
        return (float) ((b - a) * Math.sin(angle) + lambda * a * Math.sin((b - a) * angle / a));
    }

    public void nextFrame() {
        if (!hasNext()) {
            return;
        }

        points.add(new Vertex3f(
                functionX(currentAngle),
                functionY(currentAngle)
        ));


        currentAngle += dAngle;
    }

    public boolean hasNext() {
        return currentAngle < endAngle;
    }

    public void reset() {
        currentAngle = beginAngle;
        points.clear();
    }

    @Override
    public void display(GLAutoDrawable drawable, float coordinateSize) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glEnable(GL.GL_LINE_WIDTH);
        gl.glLineWidth(5f);
        gl.glColor3f(1f, 0f, 0f);

        gl.glBegin(GL2.GL_LINE_STRIP);
        points.stream()
                .forEach(vertex3f -> gl.glVertex2f(vertex3f.x, vertex3f.y));
        gl.glEnd();

        gl.glLineWidth(1f);
        gl.glDisable(GL.GL_LINE_WIDTH);
    }
}
