package lab3.main;

import lab2.main.Matrix3f;
import lab2.util.TransformOperation;
import lab3.util.Rotation;
import lab3.util.RotationAround;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.IllegalFormatException;

public class TransformMatrix extends Matrix3f implements
        Rotation<TransformMatrix>,
        RotationAround<TransformMatrix>,
        TransformOperation.Translate<TransformMatrix> {

    @Override
    public TransformMatrix transform(float a, float b, float p,
                                     float d, float e, float q,
                                     float l, float m, float s) {
        m00 = a;
        m01 = b;
        m02 = p;

        m10 = d;
        m11 = e;
        m12 = q;

        m20 = l;
        m21 = m;
        m22 = s;

        return this;
    }

    @Override
    public TransformMatrix getResultMatrix() {
        return this;
    }

    @Override
    public void transform(Matrix3f translateMatrix, TransformMatrix result) {

    }

    @Override
    public void normalize() {
        throw new NotImplementedException();
    }
}
