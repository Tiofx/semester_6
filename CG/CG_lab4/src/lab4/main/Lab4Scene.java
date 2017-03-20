package lab4.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import lab2.main.Matrix3f;
import lab2.util.Scene;
import lab4.util.Complex;
import lab4.util.Vector2D;
import lab4.util.algorithm.FloodFiller;
import lab4.util.decorators.AutoApplyTransform;
import lab4.util.decorators.FigureDecorator;
import lab4.util.decorators.GlobalSpeed;
import lab4.util.decorators.InBoundMovement;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES2.GL_UNPACK_SKIP_PIXELS;
import static com.jogamp.opengl.GL2ES2.GL_UNPACK_SKIP_ROWS;

public class Lab4Scene extends Scene {
    protected List<FigureDecorator> allFigure = new ArrayList<>();
    protected List<Labyrinth> labyrinths = new ArrayList<>();
    protected List<ClipBox> clipBosex = new ArrayList<>();

    protected Matrix3f transformMatrix = new Matrix3f();

    protected Quad tempResult = new Quad();
    protected Vertex3f tempVertexResult = new Vertex3f();
    protected GlobalParameters globalParameters = new GlobalParameters();
    private float coordinateSize;

    public void setCoordinateSize(float coordinateSize) {
        this.coordinateSize = coordinateSize;
    }

    public void clear() {
        allFigure.clear();
        labyrinths.clear();
        clipBosex.clear();
    }

    public void setAnimation(boolean animation) {
        globalParameters.animation = animation;
    }

    public static class GlobalParameters {

        public float value = 1 / 100f;
        public boolean fill = false;
        public boolean animation = true;
    }

    protected Predicate<Float> inBound = value -> (value > -1 && value < 1);

    public void addClipBox(ClipBox quad) {
        quad.updateBounds();
        quad.setColor(Color.BLUE);
        clipBosex.add(quad);
    }

    public void addLabyrinth(Labyrinth quad) {
        quad.updateBounds();
        quad.setColor(Color.RED);
        labyrinths.add(quad);
    }

    public void addQuad(Quad quad, Vector2D directionAndSpeed) {
        FigureDecorator instance =
//                new AccelerationGlobalForFun(
                new GlobalSpeed(
                        new InBoundMovement(
                                new AutoApplyTransform(quad),
                                directionAndSpeed,
                                inBound,
                                inBound
                        ),
                        globalParameters
                );

        quad.setResult(tempResult);
        allFigure.add(instance);
    }

    public Lab4Scene() {

//        TransformMatrix matrix = new TransformMatrix();
//        matrix.setIdentity();
//        matrix.scale(5f);
//
//
//        ClipBox clipBox = new ClipBox();
//        clipBox.setResult(tempResult);
//        clipBox.setColor(Color.black);
//        clipBox.transform(matrix);
//        clipBox.applyChange();
//        matrix.translate(1 / 2f, 1 / 2f);
//        clipBox.transform(matrix);
//        clipBox.applyChange();
//
//        clipBox.updateBounds();
//
//        clipBosex.add(clipBox);
////
//        labyrinth.updateBounds();
//
//
//        labyrinths.add(labyrinth);
//        matrix.scale(1 / 10f, 1 / 10f, 1f);

        transformMatrix.setIdentity();
    }


    public GlobalParameters getGlobalParameters() {
        return globalParameters;
    }

    float color[] = {0f, 0f, 0f};
    FloatBuffer buffer = FloatBuffer.allocate(4);

    private void update() {
        labyrinths.stream().forEach(labyrinth -> allFigure.removeIf(labyrinth::hit));

        allFigure.forEach(figure -> figure.transform(transformMatrix));
    }

    @Override
    public synchronized void display(GLAutoDrawable drawable) {
        update();
        final GL2 gl = drawable.getGL().getGL2();

//        buffer.put(color);
//        gl.glWindowPos2f(100, 100);
//        int width = 300, height = 100;
//        byte[] src = new byte[width * height];
//
//        for (int a = 0; a < height; a++) {
//            for (int b = 0; b < width; b++) {
//                src[a * width + b] = 127;
//            }
//        }
//
//        gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
//        gl.glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
//        gl.glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
//
//        gl.glDrawPixels(width, height,
//                GL_RED, GL.GL_UNSIGNED_BYTE,
//                ByteBuffer.wrap(src));

//        gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
//        gl.glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
//        gl.glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
//
//        for (int i = 550; i < 600; i ++) {
//            for (int j = 550; j < 600; j++) {
//
//                gl.glDrawPixels(i, j, 1, 1, 0l);
//            }
//        }

//        gl.glColor3f(0.3f, 0.3f, 0.3f);
        allFigure.stream()
                .forEach(quad -> quad.display(drawable, coordinateSize));

        if (globalParameters.fill) {
//            gl.glReadBuffer(GL_FRONT);
//            gl.glReadPixels(0, 0, screenSize, screenSize, GL_RGB, GL_FLOAT, FloatBuffer.wrap(screenBuffer));
//            floodFiller.setScreenBuffer(screenBuffer);

            allFigure.stream()
                    .forEach(figure -> fill(gl, figure));

//            gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
//            gl.glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
//            gl.glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
//
//            gl.glDrawPixels(screenSize, screenSize,
//                    GL_RGB, GL_FLOAT,
//                    FloatBuffer.wrap(screenBuffer));
        }

        clipBosex.stream()
                .peek(clipBox -> clipBox.display(drawable, coordinateSize))
                .forEach(clipBox -> clipBox.display(drawable, coordinateSize,
                        allFigure));

        labyrinths.stream()
                .forEach(labyrinth -> labyrinth.display(drawable, coordinateSize));
    }


    List<lab2.main.figures.Vertex3f> list = new ArrayList<>();
    protected static FloodFiller floodFiller = new FloodFiller();
    protected final static int screenSize = 600;
    protected static float[] screenBuffer = new float[screenSize * screenSize * 3];


    private void fill(GL2 gl, Complex figure) {
        gl.glReadBuffer(GL_FRONT);
        gl.glReadPixels(0, 0, screenSize, screenSize, GL_RGB, GL_FLOAT, FloatBuffer.wrap(screenBuffer));
        floodFiller.setScreenBuffer(screenBuffer);


        list.removeAll(list);
        figure.getVertexes(list);

        floodFiller.floodFillScanline(
                (int) ((list.get(0).x + 1) * screenSize / 2 + 10),
                (int) ((list.get(0).y + 1) * screenSize / 2),
                figure.getColor().getRGBColorComponents(null));


        gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        gl.glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
        gl.glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);

        gl.glDrawPixels(screenSize, screenSize,
                GL_RGB, GL_FLOAT,
                FloatBuffer.wrap(screenBuffer));
    }
}
