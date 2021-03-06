package lab4.util.decorators;

import lab2.main.Matrix3f;
import lab4.util.Complex;
import lab4.util.Vector2D;

public class TransformByVector<T extends Complex<T>>
        extends FigureDecorator<T> {

    protected Vector2D velocity;

    public TransformByVector(T figure, Vector2D velocity) {
        super(figure);
        this.velocity = velocity;
    }

    @Override
    public void transform(Matrix3f translateMatrix, T result) {
        translateMatrix.setIdentity();

        translateMatrix.m20 = velocity.x;
        translateMatrix.m21 = velocity.y;

        super.transform(translateMatrix, result);
    }

    public Vector2D getVelocity() {
        return velocity;
    }
}
