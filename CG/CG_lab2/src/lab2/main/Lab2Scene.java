package lab2.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import lab2.main.figures.AnimableQuad;
import lab2.main.figures.Axis;
import lab2.main.figures.Vertex3f;
import lab2.util.Scene;

public class Lab2Scene extends Scene {

    protected float coordinateSize = 11;
    protected Axis axis = new Axis();

    protected final AnimableQuad base = new AnimableQuad();
    protected final AnimableQuad result = new AnimableQuad();

    public Lab2Scene() {
        AnimableQuad quad = new AnimableQuad(new Vertex3f[]{
                new Vertex3f("A", 0, 0),
                new Vertex3f("D", 3, 0),
                new Vertex3f("C", 4, 2),
                new Vertex3f("B", 1, 2)
        });

        quad.copyTo(base);
        base.copyTo(result);

        quad.setResult(this.base);
        this.base.setResult(result);

        quad.transform(
                1, 0, 0,
                0, 1, 0,
                1, 1, 1);

        this.base.normalize();

        base.transform(
                1, 0, 0,
                0, 1, 0,
                0, 0, 1);
    }

    public AnimableQuad getBase() {
        return base;
    }

    public AnimableQuad getResult() {
        return result;
    }

    public Axis getAxis() {
        return axis;
    }

    public float getCoordinateSize() {
        return coordinateSize;
    }

    public void setCoordinateSize(float coordinateSize) {
        this.coordinateSize = coordinateSize;
    }

    public void transform(Matrix3f transformationMatrix) {
        base.transform(transformationMatrix);
        result.normalize();
    }

    protected float[] color = {0f, 1f, 0f};

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glColor3f(0.6f, 0.6f, 0.6f);
        gl.glLineWidth(0.2f);
        base.display(drawable, coordinateSize);

//        gl.glColor3f(0f, 1f, 0f);
        gl.glColor3fv(color, 0);
        gl.glLineWidth(2f);
        result.display(drawable, coordinateSize);
        gl.glLineWidth(1f);

        axis.setCoordinateSize(coordinateSize);
        axis.display(drawable);
    }

    public void nextFrame() {
        base.nextFrame();
        result.nextFrame();
        base.normalize();
        result.normalize();
    }


}
