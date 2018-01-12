package lab2.main;

import lab1.main.FiniteStateAutomaton;
import lab2.util.LogContainer;
import lab2.util.TextPosition;
import lab2.util.variantNative.CodesAnalyser;
import lab2.util.variantNative.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static lab2.util.variantNative.Code.*;
import static lab2.util.variantNative.Code.Error;

public class Automation extends FiniteStateAutomaton.AbstractFiniteStateAutomaton {
    protected final List<LogContainer> log = new ArrayList<>();
    protected TextPosition textPosition = new TextPosition();
    protected int mantissa = 0;
    protected int exponent = 0;


    protected Automation() {
    }

    public Automation(Map<Character, Integer> alphabet, int[][] transitionTable,
                      int firstState, Set<Integer> finalState) {
        super(alphabet, transitionTable, firstState, finalState);
    }

    public List<LogContainer> getLog() {
        return log;
    }

    public void sendLine(String line) {
        line.chars()
                .mapToObj(i -> (char) i)
                .forEach(this::sendCharacter);

        if (line.length() == 0 || line.charAt(line.length() - 1) != '\n') {
            sendCharacter('\n');
        }
    }


    @Override
    public boolean sendCharacter(char character) {
        boolean result = super.sendCharacter(character);

        validateData(character);

        int prevState = currentState;
        if (currentState >= BEGIN_CODE) {
            if (currentState >= SIGNS && currentState < ERRORS) {
                updateTextPosition(character);
            }

            log.add(new LogContainer(textPosition.copy(), currentState));

            if (currentState <= DATA) {
                currentState = 0;
                result = sendCharacter(character);
                return result;
            } else {
                currentState = 0;
            }
        }

        if (!(prevState >= SIGNS && prevState < ERRORS)) {
            updateTextPosition(character);
        }

        return result;
    }

    protected void validateData(char character) {
        if (currentState >= 14 && currentState <= 19 || currentState == DATA) {

            if (currentState == 14 || currentState == 16) {
                mantissa++;
            }

            if (currentState == 19) {
                exponent++;
            }

            if (currentState == DATA && (mantissa > 6 || exponent != 2)) {
                currentState = Error.IN_CONSTANT;
            }
        } else {
            resetFloatNumberInfo();
        }
    }


    protected void updateTextPosition(char character) {
        if (character != '\n') {
            textPosition.column++;
        } else {
            textPosition.column = 1;
            textPosition.row++;
            log.add(new LogContainer(null, CodesAnalyser.EOF));
        }
    }

    @Override
    protected void tryTransition(char character)
            throws ArrayIndexOutOfBoundsException {
        try {
            super.tryTransition(Character.toUpperCase(character));

        } catch (ArrayIndexOutOfBoundsException e) {

            if (Character.isLetter(character)) {
                currentState = transitionTable[Constants.allLettersRowNumber][currentState];
            } else if (Character.isDigit(character)) {
                currentState = transitionTable[Constants.allDigitRowNumber][currentState];
            } else {
                currentState = transitionTable[Constants.L3RowNumber][currentState];
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        log.clear();
        textPosition.reset();
        resetFloatNumberInfo();
    }

    protected void resetFloatNumberInfo() {
        mantissa = 0;
        exponent = 0;
    }
}

