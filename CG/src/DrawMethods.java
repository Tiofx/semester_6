
import java.awt.*;
import java.util.Vector;

public final class DrawMethods {
    protected static LineDraw get = Method.BY_DDA.getMethod();

    private DrawMethods() {
    }

    public static void setMethod(Method method) {
        get = method.getMethod();
    }

//    public static Vector<Point> asymmetricDDA(int x1, int y1, int x2, int y2) {
//        Vector<Point> vector;
//        int n, px, py;
//        float dx, dy;
//        float x1f = x1, y1f = y1;
//
//        px = x2 - x1;
//        py = y2 - y1;
//
//        if (Math.abs(px) > Math.abs(py)) {
//            n = Math.abs(px);
//
//            dx = (px > 0 ? 1f : -1f);
//            dy = (float) py / n;
//        } else {
//            n = Math.abs(py);
//
//            dx = (float) px / n;
//            dy = (py > 0 ? 1f : -1f);
//        }
//
//        vector = new Vector<>(2 * n);
//        vector.add(new Point(x1, y1));
//
//        while (x1 != x2 || y1 != y2) {
//            x1f += dx;
//            y1f += dy;
//
//            x1 = Math.round(x1f);
//            y1 = Math.round(y1f);
//            vector.add(new Point(x1, y1));
//        }
//
//        return vector;
//    }


    public static Vector<Point> rectangular(int x0, int y0, int width, int height) {
        Vector<Point> result = get.getPoints(x0, y0, x0 + width, y0);
        result.addAll(get.getPoints(x0, y0, x0, y0 + height));
        result.addAll(get.getPoints(x0, y0 + height, x0 + width, y0 + height));
        result.addAll(get.getPoints(x0 + width, y0, x0 + width, y0 + height));

        return result;
    }

    public static Vector<Point> rightArrow(int x1, int y1, int x2, int y2) {
        Vector<Point> result = get.getPoints(x1, y1, x2, y2);
        result.addAll(get.getPoints(x2, y2, x2 - (x2 - x1) / 4, y2 - (x2 - x1) / 4));
        result.addAll(get.getPoints(x2, y2, x2 - (x2 - x1) / 4, y2 + (x2 - x1) / 4));

        return result;
    }

    public static Vector<Point> leftArrow(int x1, int y1, int x2, int y2) {
        Vector<Point> result = get.getPoints(x1, y1, x2, y2);
        result.addAll(get.getPoints(x1, y1, x1 + (x2 - x1) / 4, y1 - (x2 - x1) / 4));
        result.addAll(get.getPoints(x1, y1, x1 + (x2 - x1) / 4, y1 + (x2 - x1) / 4));

        return result;
    }

    public enum Method {
        BY_DDA(new ByDDA()), BY_GET(new ByGet());
        private final LineDraw method;

        Method(LineDraw method) {
            this.method = method;
        }

        public LineDraw getMethod() {
            return method;
        }
    }

    @FunctionalInterface
    public interface LineDraw {
        Vector<Point> getPoints(int x1, int y1, int x2, int y2);
    }

    public static class ByGet implements LineDraw {
        @Override
        public Vector<Point> getPoints(int x1, int y1, int x2, int y2) {
            Vector<Point> vector = new Vector<>(2);

            vector.add(new Point(x1, y1));
            vector.add(new Point(x2, y2));

            return vector;
        }
    }

    public static class ByDDA implements LineDraw {
        @Override
        public Vector<Point> getPoints(int x1, int y1, int x2, int y2) {
            Vector<Point> vector;
            int n, px, py;
            float dx, dy;
            float x1f = x1, y1f = y1;

            px = x2 - x1;
            py = y2 - y1;

            if (Math.abs(px) > Math.abs(py)) {
                n = Math.abs(px);

                dx = (px > 0 ? 1f : -1f);
                dy = (float) py / n;
            } else {
                n = Math.abs(py);

                dx = (float) px / n;
                dy = (py > 0 ? 1f : -1f);
            }

            vector = new Vector<>(2 * n);
            vector.add(new Point(x1, y1));

            while (x1 != x2 || y1 != y2) {
                x1f += dx;
                y1f += dy;

                x1 = Math.round(x1f);
                y1 = Math.round(y1f);
                vector.add(new Point(x1, y1));
            }

            return vector;

        }
    }
}
