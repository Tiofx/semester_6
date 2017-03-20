package lab2.util;

public abstract class BaseFigure<T extends BaseFigure<T>>
        implements TransformOperationExtended<T> {
    protected T result;
    protected boolean visible = true;

    @Override
    public T getResultMatrix() {
        return result;
    }

    @Override
    public void setResult(T resultMatrix) {
        this.result = resultMatrix;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
