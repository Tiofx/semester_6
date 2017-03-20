package lab2.util;

public final class TextPosition {
    public int row;
    public int column;

    public TextPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public TextPosition() {
        this(1, 1);
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(row) + ", " + String.valueOf(column) + ")";
    }

    public TextPosition copy() {
        return new TextPosition(row, column);
    }

    public TextPosition copyTo(TextPosition textPosition) {
        textPosition.row = row;
        textPosition.column = column;
        return textPosition;
    }

    public void reset() {
        row = 1;
        column = 1;
    }
}
