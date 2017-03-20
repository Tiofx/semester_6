package lab4.util.decorators;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import lab2.main.Matrix3f;
import lab4.main.Lab4Scene;
import lab4.util.Complex;
import lab4.util.Vector2D;
import lab4.util.algorithm.FloodFiller;

import java.util.ArrayList;
import java.util.List;

public class GlobalSpeed<T extends Complex<T>>
        extends TransformByVector<T> {

    public final Lab4Scene.GlobalParameters globalParameters;

    protected static FloodFiller floodFiller = new FloodFiller();
    protected final static int screenSize = 600;
    protected static float[] screenBuffer = new float[screenSize * screenSize * 3];

    public GlobalSpeed(TransformByVector<T> figure, Lab4Scene.GlobalParameters globalParameters) {
        this((T) figure, figure.getVelocity(), globalParameters);
    }

    public GlobalSpeed(T figure, Vector2D vector2D, Lab4Scene.GlobalParameters globalParameters) {
        super(figure, vector2D);
        this.globalParameters = globalParameters;
    }

    @Override
    public void transform(Matrix3f translateMatrix, T result) {
        if (globalParameters.value == 0 || !globalParameters.animation) {
            return;
        }

        velocity.multiply(globalParameters.value);

        super.transform(translateMatrix, result);

        velocity.multiply(1f / globalParameters.value);
    }

    @Override
    public void display(GLAutoDrawable drawable, float coordinateSize) {
        final GL2 gl = drawable.getGL().getGL2();
//        gl.glReadBuffer(GL_FRONT);
//        FloatBuffer buffer = FloatBuffer.allocate(4);
//
//        gl.glReadBuffer(GL_FRONT);
//        gl.glReadPixels(0, 0, 1, 1, GL_RGB, GL_FLOAT, FloatBuffer.wrap(screenBuffer));
//        gl.glDrawPixels();
//
//        float[] pixels;
//        pixels = buffer.array();
//        float red = pixels[0];
//        float green = pixels[1];
//        float blue = pixels[2];

        super.display(drawable, coordinateSize);


//        gl.glReadBuffer(GL_FRONT);
//        gl.glReadPixels(0, 0, screenSize, screenSize, GL_RGB, GL_FLOAT, FloatBuffer.wrap(screenBuffer));
//        floodFiller.setScreenBuffer(screenBuffer);
////        floodFiller.gl = gl;
//
//
//        list.removeAll(list);
//        getVertexes(list);
//
//        floodFiller.floodFillScanline(
//                (int) ((list.get(0).x + 1) * screenSize / 2 + 10),
//                (int) ((list.get(0).y + 1) * screenSize / 2),
//                getColor().getRGBColorComponents(null));
//
//
//
//        gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
//        gl.glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
//        gl.glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
//
//        gl.glDrawPixels(screenSize, screenSize,
//                GL_RGB, GL.GL_FLOAT,
//                FloatBuffer.wrap(screenBuffer));
    }

    List<lab2.main.figures.Vertex3f> list = new ArrayList<>();
}
