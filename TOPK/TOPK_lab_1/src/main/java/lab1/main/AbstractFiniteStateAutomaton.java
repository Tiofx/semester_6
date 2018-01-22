package lab1.main;

import java.util.Map;
import java.util.Set;

public abstract class AbstractFiniteStateAutomaton {
    protected final Map<Character, Integer> alphabet;
    protected final int transitionTable[][];
    protected final int firstState;
    protected final Set<Integer> finalState;

    protected int currentState;

    public AbstractFiniteStateAutomaton() {
        this(null, null, 0, null);
    }

    protected AbstractFiniteStateAutomaton(Map<Character, Integer> alphabet, int[][] transitionTable,
                                           int firstState, Set<Integer> finalState) {
        this.alphabet = alphabet;
        this.transitionTable = transitionTable;
        this.firstState = firstState;
        this.finalState = finalState;
    }

    public boolean sendCharacter(char character) {
        try {
            tryChangeStateBy(character);
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    protected void tryChangeStateBy(char character) throws ArrayIndexOutOfBoundsException {
        currentState = transitionTable[alphabet.getOrDefault(character, -1)][currentState];
    }

    public boolean isFinalState() {
        return finalState.contains(currentState);
    }

    public void reset() {
        currentState = firstState;
    }
}
