package lab1.main;

import com.sun.tools.javac.util.List;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FiniteStateAutomaton implements AutomatonInterface {
    public static final int ERROR_CODE = -1;
    private List<Integer> endStates = List.of(3, 6, 7, 8);

    public enum Result {
        RIGHT, WRONG, ONGOING
    }

    protected final Map<Character, Integer> alphabet;

    protected final int table[][] = {
            {1, ERROR_CODE, 1, ERROR_CODE, 5, ERROR_CODE, 7, 7, 5},
            {ERROR_CODE, 2, ERROR_CODE, ERROR_CODE, ERROR_CODE, 6, 6, ERROR_CODE, ERROR_CODE},
            {8, ERROR_CODE, 3, 3, ERROR_CODE, ERROR_CODE, 7, 7, 3},
    };

    public FiniteStateAutomaton() {
        alphabet = Collections.unmodifiableMap(new HashMap<Character, Integer>() {{
            put('a', 0);
            put('b', 1);
            put('c', 2);
        }});
    }

    @Override
    public Result check(String string) {
        final int resultState = resultState(string);

        if (resultState == ERROR_CODE) return Result.WRONG;
        if (endStates.contains(resultState)) return Result.RIGHT;

        return Result.ONGOING;
    }

    private int resultState(String string) {
        int result = 0;

        for (int i = 0; i < string.length(); i++) {
            char currentCharacter = string.charAt(i);
            int numberOfCurrentCharacter = alphabet.getOrDefault(currentCharacter, -1);
            if (!isIndexesInRange(result, numberOfCurrentCharacter)) return ERROR_CODE;

            result = table[numberOfCurrentCharacter][result];
        }

        return result;
    }

    private boolean isIndexesInRange(int col, int row) {
        return row >= 0 && row < table.length && col >= 0 && col < table[row].length;
    }


}
