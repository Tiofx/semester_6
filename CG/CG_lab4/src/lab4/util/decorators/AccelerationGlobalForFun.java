package lab4.util.decorators;

import lab2.main.Matrix3f;
import lab4.main.Lab4Scene;
import lab4.util.Complex;
import lab4.util.Vector2D;

@Deprecated
public class AccelerationGlobalForFun<T extends Complex<T>> extends GlobalSpeed<T> {
    public AccelerationGlobalForFun(TransformByVector<T> figure, Lab4Scene.GlobalParameters globalParameters) {
        super(figure, globalParameters);
    }

    public AccelerationGlobalForFun(T figure, Vector2D vector2D, Lab4Scene.GlobalParameters globalParameters) {
        super(figure, vector2D, globalParameters);
    }

    float c = 0.01f;

    @Override
    public void transform(Matrix3f translateMatrix, T result) {
        velocity.y -= c;
        c += 0.001f;
        super.transform(translateMatrix, result);
    }
}
