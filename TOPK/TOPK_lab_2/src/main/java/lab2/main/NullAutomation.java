package lab2.main;

import lab2.util.variantNative.Code;

import java.util.Map;
import java.util.Set;

public class NullAutomation extends Automation {
    public NullAutomation() {
        log.add(logInfo);
    }

    public NullAutomation(Map<Character, Integer> alphabet, int[][] transitionTable, int firstState, Set<Integer> finalState) {
        super(alphabet, transitionTable, firstState, finalState);
        log.add(logInfo);
    }

    @Override
    public void sendLine(String line) {
    }

    @Override
    public boolean sendCharacter(char character) {
        return false;
    }

    @Override
    protected void validateData() {
        currentState = Code.Error.IN_CONSTANT;
    }

    @Override
    protected void updateTextPosition(char character) {
    }

    @Override
    protected void tryChangeStateBy(char character) throws ArrayIndexOutOfBoundsException {
        currentState = 0;
    }
}
