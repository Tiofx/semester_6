package lab2.main;

import lab1.main.FiniteStateAutomaton;
import lab2.util.LogInfo;
import lab2.util.variantNative.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static lab2.util.variantNative.Code.*;
import static lab2.util.variantNative.Code.Error;

public class Automation extends FiniteStateAutomaton.AbstractFiniteStateAutomaton {
    protected final List<LogInfo> log = new ArrayList<>();
    protected LogInfo logInfo = new LogInfo();
    protected int mantissa = 0;
    protected int exponent = 0;


    protected Automation() {
    }

    public Automation(Map<Character, Integer> alphabet, int[][] transitionTable,
                      int firstState, Set<Integer> finalState) {
        super(alphabet, transitionTable, firstState, finalState);
    }

    public List<LogInfo> getLog() {
        return log;
    }

    public void sendLine(String line) {
        line.chars()
                .mapToObj(i -> (char) i)
                .forEach(this::sendCharacter);

        if (hasNoEndOfLine(line)) {
            sendCharacter('\n');
        }
    }

    private boolean hasNoEndOfLine(String line) {
        return line.isEmpty() || lastChar(line) != '\n';
    }

    private static char lastChar(String string) {
        if (string.isEmpty()) throw new StringIndexOutOfBoundsException("Empty string has no characters");
        return string.charAt(string.length() - 1);
    }

    @Override
    public boolean sendCharacter(char character) {
        boolean result = super.sendCharacter(character);

        validateData();

        int prevState = currentState;
        if (currentState >= BEGIN_CODE) {
            if (isSignsOrErrors(currentState)) {
                updateTextPosition(character);
            }

            LogInfo copy = logInfo.copy();
            copy.setAutomationPosition(currentState);
            log.add(copy);

            int prevCurrentState = currentState;
            currentState = 0;
            if (prevCurrentState <= DATA) {
                return sendCharacter(character);
            }
        }

        if (!isSignsOrErrors(prevState)) {
            updateTextPosition(character);
        }

        return result;
    }

    public LogInfo getLogInfo() {
        return logInfo;
    }

    private boolean isSignsOrErrors(int state) {
        return (state >= SIGNS && state < ERRORS);
    }

    protected void validateData() {
        if (currentState >= 14 && currentState <= 19 || currentState == DATA) {

            if (currentState == 14 || currentState == 16) {
                mantissa++;
            }

            if (currentState == 19) {
                exponent++;
            }

            if (isNotValidConstant()) {
                currentState = Error.IN_CONSTANT;
            }
        } else {
            resetFloatNumberInfo();
        }
    }

    private boolean isNotValidConstant() {
        return currentState == DATA && (mantissa > 6 || exponent != 2);
    }


    protected void updateTextPosition(char character) {
        if (character != '\n') {
            getLogInfo().column++;
        } else {
            getLogInfo().column = 1;
            getLogInfo().row++;
            log.add(new LogInfo());
        }
    }

    @Override
    protected void tryTransition(char character)
            throws ArrayIndexOutOfBoundsException {
        try {
            super.tryTransition(Character.toUpperCase(character));

        } catch (ArrayIndexOutOfBoundsException e) {
            setCurrentState(getStateByChar(character));
        }
    }

    private int getStateByChar(char character) {
        if (Character.isLetter(character)) {
            return getStateByRow(Constants.allLettersRowNumber);

        } else if (Character.isDigit(character)) {
            return getStateByRow(Constants.allDigitRowNumber);

        } else {
            return getStateByRow(Constants.L3RowNumber);
        }
    }


    private void setCurrentState(int value) {
        currentState = value;
    }

    private int getStateByRow(int row) {
        return transitionTable[row][currentState];
    }


    @Override
    public void reset() {
        super.reset();
        log.clear();
        logInfo.reset();
        resetFloatNumberInfo();
    }

    private void resetFloatNumberInfo() {
        mantissa = 0;
        exponent = 0;
    }
}

