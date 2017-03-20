
public interface TransformMatrix {
    float[][] transform(float[][] translateMatrix);

    default float[][] transform(float a, float b, float p,
                                float d, float e, float q,
                                float l, float m, float s) {

        float[][] translateMatrix = {{a, b, p}, {d, e, q}, {l, m, s}};
        return transform(translateMatrix);
    }

    public interface Translate extends TransformMatrix {
        default float[][] translate(float x, float y) {
            return transform(
                    1, 0, 0,
                    0, 1, 0,
                    x, y, 1
            );
        }

        default float[][] translateX(float x) {
            return translate(x, 0);
        }

        default float[][] translateY(float y) {
            return translate(0, y);
        }
    }

    public interface Shift extends TransformMatrix {
        default float[][] shift(float x, float y) {
            return transform(
                    1, y, 0,
                    x, 1, 0,
                    0, 0, 1
            );
        }

        default float[][] shiftX(float x) {
            return shift(x, 0);
        }

        default float[][] shiftY(float y) {
            return shift(0, y);
        }
    }

    public interface Scale extends TransformMatrix {
        default float[][] scale(float x, float y, float xy) {
            return transform(
                    x, 0, 0,
                    0, y, 0,
                    0, 0, xy
            );
        }

        default float[][] scaleX(float x) {
            return scale(x, 1, 1);
        }

        default float[][] scaleY(float y) {
            return scale(1, y, 1);
        }

        default float[][] scale(float xy) {
            return scale(1, 1, xy);
        }
    }

    public static interface Mirror extends TransformMatrix {
        default float[][] mirror(boolean x, boolean y, boolean origin) {
            return transform(
                    x ? -1 : +1, 0, 0,
                    0, y ? -1 : +1, 0,
                    0, 0, origin ? -1 : +1
            );
        }

        default float[][] mirrorX(boolean x) {
            return mirror(x, false, false);
        }

        default float[][] mirrorY(boolean y) {
            return mirror(false, y, false);
        }

        default float[][] mirrorOrigin(boolean origin) {
            return mirror(false, false, origin);
        }
    }

    default float[][] perspectiveProjection(float p, float q) {
        return transform(
                1, 0, p,
                0, 1, q,
                0, 0, 1
        );
    }
}
