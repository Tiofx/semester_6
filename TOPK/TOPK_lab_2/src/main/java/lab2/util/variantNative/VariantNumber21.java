package lab2.util.variantNative;

import lab2.util.AbstractVariant;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static lab2.util.variantNative.Code.*;

public class VariantNumber21 extends AbstractVariant {

    public VariantNumber21() {
    }

    @Override
    public HashMap<Character, Integer> getAlphabet() {
        return new HashMap<Character, Integer>() {{
            put('P', 0);
            put('R', 1);
            put('O', 2);
            put('C', 3);
            put('E', 4);
            put('N', 5);
            put('D', 6);
            put('0', 9);
            put('1', 10);
            put('=', 11);
            put(',', 12);
            put('&', 13);
            put('!', 14);
            put('<', 15);
            put('>', 16);
            put('+', 17);
            put('-', 18);
            put('.', 19);

            put(' ', 20);
            put('\n', 21);
            put('\t', 22);
        }};
    }

    @Override
    public Set<Integer> getFinalStates() {
        return Stream.of(PROC, END, IDEN, DATA,
                Sign.EQUALLY, Sign.COMMA, Sign.DOUBLE_AMPERSAND,
                Sign.DOUBLE_EXCLAMATION_POINT, Sign.LEFT_SHIFT, Sign.RIGHT_SHIFT,
//                Sign.SPACE, Sign.NEW_LINE,

                Code.Error.L3, Code.Error.IN_CHAIN, Code.Error.IN_KEY_WORD, Code.Error.IN_VARIABLE,
                Code.Error.IN_CONSTANT, Code.Error.IN_DOUBLE_AMPERSAND, Code.Error.IN_DOUBLE_EXCLAMATION_POINT,
                Code.Error.IN_LEFT_SHIFT, Code.Error.IN_RIGHT_SHIFT
        )
                .collect(Collectors.toSet());
    }
}
