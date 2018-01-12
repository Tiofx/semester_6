package lab2.main;

import lab1.main.FiniteStateAutomaton;
import lab2.util.LogContainer;
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

    protected int rowTextPosition = 1;
    protected int columnTextPosition = 1;

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

            log.add(new LogContainer(rowTextPosition, columnTextPosition, currentState));

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
            exponent = 0;
            mantissa = 0;
        }
    }


    protected void updateTextPosition(char character) {
        if (character != '\n') {
            columnTextPosition++;
        } else {
            columnTextPosition = 1;
            rowTextPosition++;
            log.add(new LogContainer(-1, -1, CodesAnalyser.EOF));
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

        rowTextPosition = 1;
        columnTextPosition = 1;

        mantissa = 0;
        exponent = 0;
    }
}

