package lab2.util;

public final class LogInfo {
    public int row;
    public int column;

    public LogInfo(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public LogInfo() {
        this(1, 1);
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(row) + ", " + String.valueOf(column) + ")";
    }

    public LogInfo copy() {
        return new LogInfo(row, column);
    }

    public void reset() {
        row = 1;
        column = 1;
    }
}
