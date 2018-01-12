package lab2.util;

public class LogContainer {
    protected LogInfo logInfo;
    protected int automationPosition;

    public LogContainer(LogInfo logInfo, int automationPosition) {
        this.logInfo = logInfo;
        this.automationPosition = automationPosition;
    }

    @Override
    public String toString() {
        return logInfo.toString();
    }

    public int getAutomationPosition() {
        return automationPosition;
    }
}
