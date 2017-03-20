package lab3.main;

import lab2.util.animation.AbstractAnimation;

public class RotateAnimation extends AbstractAnimation {
    protected float degrees;
    protected TransformMatrix currentResult;

    public RotateAnimation(float degrees) {
        this(new TransformMatrix(), degrees);
    }

    public RotateAnimation(TransformMatrix currentResult, float degrees) {
        super(currentResult, (int) Math.abs(Math.ceil(degrees)));
        this.currentResult = currentResult;
        this.degrees = degrees;
    }

    @Override
    protected void calculateNextFrame() {
        currentResult.rotateByDegrees(degrees < 0 ? -1f : 1f);
    }
}
