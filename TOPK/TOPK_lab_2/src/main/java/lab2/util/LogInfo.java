package lab2.util;

import lab2.util.variantNative.CodesAnalyser;

public class LogInfo {
    public int row;
    public int column;
    protected int automationPosition;

    public LogInfo(int row, int column, int automationPosition) {
        this.row = row;
        this.column = column;
        this.automationPosition = automationPosition;
    }

    public LogInfo() {
        this(1, 1, CodesAnalyser.EOF);
    }

    public int getAutomationPosition() {
        return automationPosition;
    }

    public void setAutomationPosition(int automationPosition) {
        this.automationPosition = automationPosition;
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(row) + ", " + String.valueOf(column) + ")";
    }

    public LogInfo copy() {
        return new LogInfo(row, column, automationPosition);
    }

    public void updateTextPosition(char character) {
        if (character != '\n') {
            column++;
        } else {
            column = 1;
            row++;
        }
    }

    public void reset() {
        row = 1;
        column = 1;
        automationPosition = CodesAnalyser.EOF;
    }
}
