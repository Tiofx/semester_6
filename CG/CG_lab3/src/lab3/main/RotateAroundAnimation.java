package lab3.main;

import lab2.main.figures.Vertex3f;

public class RotateAroundAnimation extends RotateAnimation {
    protected Vertex3f point;

    public RotateAroundAnimation(float degrees, Vertex3f point) {
        super(degrees);
        this.point = point;
    }

    public RotateAroundAnimation(TransformMatrix currentResult, float degrees, Vertex3f point) {
        super(currentResult, degrees);
        this.point = point;
    }

    @Override
    protected void calculateNextFrame() {
        currentResult.rotateAroundByDegrees(degrees < 0 ? -1 : 1, point.x, point.y);
    }
}
