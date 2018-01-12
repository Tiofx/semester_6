package lab2.util;

public class LogContainer {
    public TextPosition textPosition;
    public int automationPosition;

    public LogContainer(TextPosition textPosition, int automationPosition) {
        this.textPosition = textPosition;
        this.automationPosition = automationPosition;
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(textPosition.row) + ", " + String.valueOf(textPosition.column) + ")";
    }
}
