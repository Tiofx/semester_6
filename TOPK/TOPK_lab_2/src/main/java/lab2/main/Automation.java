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

        if (isEndOfLine(line)) {
            sendCharacter('\n');
        }
    }

    private boolean isEndOfLine(String line) {
        return line.length() == 0 || line.charAt(line.length() - 1) != '\n';
    }

    private static char lastChar(String string) {
        return string.charAt(string.length() - 1);
    }


    @Override
    public boolean sendCharacter(char character) {
        boolean result = super.sendCharacter(character);

        validateData(character);

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

    private boolean isSignsOrErrors(int state) {
        return (state >= SIGNS && state < ERRORS);
    }

    protected void validateData(char character) {
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
            logInfo.column++;
        } else {
            logInfo.column = 1;
            logInfo.row++;
            log.add(new LogInfo());
        }
    }

    @Override
    protected void tryTransition(char character)
            throws ArrayIndexOutOfBoundsException {
        try {
            super.tryTransition(Character.toUpperCase(character));

        } catch (ArrayIndexOutOfBoundsException e) {

            if (Character.isLetter(character)) {
                setCurrentStateAsLetters();
            } else if (Character.isDigit(character)) {
                setCurrentStateAsDigit();
            } else {
                setCurrentStateAsL3();
            }
        }
    }

    private void setCurrentStateAsL3() {
        currentState = transitionTable[Constants.L3RowNumber][currentState];
    }

    private void setCurrentStateAsDigit() {
        currentState = transitionTable[Constants.allDigitRowNumber][currentState];
    }

    private void setCurrentStateAsLetters() {
        currentState = transitionTable[Constants.allLettersRowNumber][currentState];
    }

    @Override
    public void reset() {
        super.reset();
        log.clear();
        logInfo.reset();
        resetFloatNumberInfo();
    }

    protected void resetFloatNumberInfo() {
        mantissa = 0;
        exponent = 0;
    }
}

