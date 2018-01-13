package lab1.main;

import com.sun.tools.javac.util.List;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FiniteStateAutomaton {
    public static final int ERROR_CODE = -1;
    private List<Integer> endStates = List.of(3, 6, 7, 8);

    public enum Result {
        RIGHT, WRONG, ONGOING
    }

    protected final Map<Character, Integer> alphabet = new HashMap<>();

    protected final int table[][] = {
            {1, ERROR_CODE, 1, ERROR_CODE, 5, ERROR_CODE, 7, 7, 5},
            {ERROR_CODE, 2, ERROR_CODE, ERROR_CODE, ERROR_CODE, 6, 6, ERROR_CODE, ERROR_CODE},
            {8, ERROR_CODE, 3, 3, ERROR_CODE, ERROR_CODE, 7, 7, 3},
    };

    public FiniteStateAutomaton() {
        loadAlphabet();
    }

    protected void loadAlphabet() {
        alphabet.put('a', 0);
        alphabet.put('b', 1);
        alphabet.put('c', 2);
    }

    public Result check(String string) {
        final int resultState = resultState(string, 0);

        Result result;
        if (resultState == ERROR_CODE) {
            result = Result.WRONG;
        } else if (endStates.contains(resultState)){
            result = Result.RIGHT;
        } else {
            result = Result.ONGOING;
        }

        return result;
    }

    private int resultState(String string, int startState) {
        int result = startState;
        try {
            for (int i = 0; i < string.length(); i++) {
                char currentCharacter = string.charAt(i);
                int numberOfCurrentCharacter = alphabet.getOrDefault(currentCharacter, -1);
                result = table[numberOfCurrentCharacter][result];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            result = ERROR_CODE;
        }

        return result;
    }


    public abstract static class AbstractFiniteStateAutomaton {
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
                tryTransition(character);
                return true;
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }

        protected void tryTransition(char character) throws ArrayIndexOutOfBoundsException {
            currentState = transitionTable[alphabet.getOrDefault(character, -1)][currentState];
        }

        public boolean isFinalState() {
            return finalState.contains(currentState);
        }

        public void reset() {
            currentState = firstState;
        }
    }
}
