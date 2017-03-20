package lab3.gui;

import lab2.gui.Animation;
import lab2.main.Matrix3f;
import lab2.util.animation.Animable;
import lab2.util.TransformOperationExtended;

import java.util.Arrays;

public class RotateAnimation extends Animation {

    public RotateAnimation(Matrix3f transformationMatrix, Animable<? extends TransformOperationExtended> transformable,
                           Runnable... update) {
        super(transformationMatrix, transformable, update);
    }

    public RotateAnimation() {
    }

    @Override
    public void run() {
        transformationMatrix.copyTo(temp);
        n = fpsRate * totalTime / 1000f;

        while (transformable.hasNextFrame()) {

            transformable.nextFrame();
            ((TransformOperationExtended) transformable).applyChange();

            Arrays.stream(update).forEach(Runnable::run);

            try {
                Thread.sleep((long) (1000f / fpsRate));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
