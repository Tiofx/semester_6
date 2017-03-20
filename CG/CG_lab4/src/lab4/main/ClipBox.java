package lab4.main;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import lab2.main.figures.Vertex3f;
import lab4.util.Complex;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClipBox extends Labyrinth {
    public static final int INSIDE = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 4;
    public static final int TOP = 8;
    
    private CohenSutherland clipper = new CohenSutherland();

    public ClipBox() {
        super();
        setColor(Color.blue);
    }

    @Override
    protected void displayQuad(GL2 gl) {
        super.displayQuad(gl);
    }

    @Override
    protected void floodFill(GL2 gl) {
        gl.glColor3fv(Color.white.getRGBColorComponents(null), 0);
        gl.glBegin(GL2.GL_QUADS);
        Arrays.stream(vertices).forEach(vertex3f -> gl.glVertex2f(vertex3f.x, vertex3f.y));
        gl.glEnd();
    }


    public void display(GLAutoDrawable drawable, float coordinateSize, List<? extends Complex> figures) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL.GL_LINE_WIDTH);
        gl.glLineWidth(3f);
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3fv(getColor().getRGBColorComponents(null), 0);

        figures.stream()
                .map(clipper::clip)
                .forEach(vertex3fs -> vertex3fs.forEach(vertex3f -> gl.glVertex2f(vertex3f.x, vertex3f.y)));

        gl.glEnd();

        gl.glEnable(GL.GL_POINT_SIZE);
        gl.glPointSize(5f);
        gl.glBegin(GL2.GL_POINTS);

        figures.stream()
                .map(clipper::clip)
                .forEach(vertex3fs -> vertex3fs.forEach(vertex3f -> gl.glVertex2f(vertex3f.x, vertex3f.y)));

        gl.glEnd();
    }

    private class LineSegment {
        public float x0;
        public float y0;
        public float x1;
        public float y1;

        public LineSegment(float x0, float y0, float x1, float y1) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
        }
    }

    public interface LineClipper {
        public LineSegment clip(LineSegment clip);
    }

    public class CohenSutherland implements LineClipper {

        private int computeOutCode(double x, double y) {
            int code = INSIDE;

            if (x < xMin) {
                code |= LEFT;
            } else if (x > xMax) {
                code |= RIGHT;
            }
            if (y < yMin) {
                code |= BOTTOM;
            } else if (y > yMax) {
                code |= TOP;
            }

            return code;
        }

        public List<Vertex3f> clip(Complex figure) {
            List<LineSegment> segments = new ArrayList<>();
            List<Vertex3f> list = new ArrayList<>();
            figure.getVertexes(list);


            for (int i = 1; i < list.size() + 1; i++) {
                segments.add(clip(list.get(i - 1), list.get(i % list.size())));
            }


            return segments.stream()
                    .filter(segment -> segment != null)
                    .flatMap(segment -> Stream.of(
                            new Vertex3f(segment.x0, segment.y0),
                            new Vertex3f(segment.x1, segment.y1)))
                    .collect(Collectors.toList());
        }

        public LineSegment clip(lab2.main.figures.Vertex3f begin, lab2.main.figures.Vertex3f end) {
            return clip(new LineSegment(begin.x, begin.y, end.x, end.y));
        }

        public LineSegment clip(LineSegment line) {
            float x0 = line.x0, x1 = line.x1, y0 = line.y0, y1 = line.y1;
            int outCode0 = computeOutCode(x0, y0);
            int outCode1 = computeOutCode(x1, y1);
            boolean accept = false;

            while (true) {
                if ((outCode0 | outCode1) == 0) {
                    accept = true;
                    break;
                } else if ((outCode0 & outCode1) != 0) {
                    break;
                } else {
                    float x, y;

                    int outCodeOut = (outCode0 != 0) ? outCode0 : outCode1;

                    if ((outCodeOut & TOP) != 0) {
                        x = x0 + (x1 - x0) * (yMax - y0) / (y1 - y0);
                        y = yMax;
                    } else if ((outCodeOut & BOTTOM) != 0) {
                        x = x0 + (x1 - x0) * (yMin - y0) / (y1 - y0);
                        y = yMin;
                    } else if ((outCodeOut & RIGHT) != 0) {
                        y = y0 + (y1 - y0) * (xMax - x0) / (x1 - x0);
                        x = xMax;
                    } else {
                        y = y0 + (y1 - y0) * (xMin - x0) / (x1 - x0);
                        x = xMin;
                    }

                    if (outCodeOut == outCode0) {
                        x0 = x;
                        y0 = y;
                        outCode0 = computeOutCode(x0, y0);
                    } else {
                        x1 = x;
                        y1 = y;
                        outCode1 = computeOutCode(x1, y1);
                    }
                }
            }

            if (accept) {
                return new LineSegment(x0, y0, x1, y1);
            }
            return null;
        }
    }
}
