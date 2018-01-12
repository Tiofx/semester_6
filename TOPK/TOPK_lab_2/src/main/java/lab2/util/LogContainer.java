package lab2.util;

public class LogContainer {
    public int rowTextPosition;
    public int columnTextPosition;

    public int automationPosition;

    public LogContainer(int rowTextPosition, int columnTextPosition, int automationPosition) {
        this.rowTextPosition = rowTextPosition;
        this.columnTextPosition = columnTextPosition;
        this.automationPosition = automationPosition;
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(rowTextPosition) + ", " + String.valueOf(columnTextPosition) + ")";
    }
}
