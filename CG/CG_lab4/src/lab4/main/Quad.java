package lab4.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import lab2.main.figures.Vertex3f;
import lab4.util.Complex;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Quad extends lab2.main.figures.Quad implements Complex<lab2.main.figures.Quad> {
    protected Color color;

    public Quad() {
        fill = true;
    }

    public Quad(lab2.main.figures.Vertex3f[] vertices) {
        super(vertices);
        fill = true;
    }

    @Override
    public void getVertexes(List<Vertex3f> result) {
        for (int i = 0; i < 4; i++) {
            result.add(vertices[i]);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable, float coordinateSize) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glLineWidth(2f);
//        gl.glGetColor()
        if (color != null) {
            gl.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        }

        super.display(drawable, coordinateSize);

        gl.glLineWidth(1f);
    }

    @Override
    protected void displayQuad(GL2 gl) {
        gl.glBegin(GL2.GL_LINE_LOOP);
        Arrays.stream(vertices).forEach(vector3f -> gl.glVertex2f(vector3f.x, vector3f.y));
        gl.glEnd();

        if (fill) {
            floodFill(gl);
        }
    }

    protected void floodFill(GL2 gl) {

    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
