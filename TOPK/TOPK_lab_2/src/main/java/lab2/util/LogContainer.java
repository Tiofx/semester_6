package lab2.util;

public class LogContainer {
    protected LogInfo logInfo;

    public LogContainer(LogInfo logInfo) {
        this.logInfo = logInfo;
    }

    @Override
    public String toString() {
        return logInfo.toString();
    }

    public int getAutomationPosition() {
        return logInfo.getAutomationPosition();
    }
}
