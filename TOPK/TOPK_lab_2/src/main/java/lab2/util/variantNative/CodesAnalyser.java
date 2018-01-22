package lab2.util.variantNative;

import lab2.util.LogInfo;

import java.util.HashMap;
import java.util.Map;

import static lab2.util.variantNative.Code.*;

public class CodesAnalyser {
    public final static int EOF = -1;
    protected final Map<Integer, String> map;

    public CodesAnalyser() {
        map = new HashMap<Integer, String>() {{
            put(PROC, "Служебное слово PROC");
            put(END, "Служебное слово END");
            put(IDEN, "Переменная");
            put(DATA, "Константа");

            put(Sign.EQUALLY, "Знак [ = ]");
            put(Sign.COMMA, "Знак [ , ]");
            put(Sign.DOUBLE_AMPERSAND, "Знак [ && ]");
            put(Sign.DOUBLE_EXCLAMATION_POINT, "Знак [ !! ]");
            put(Sign.LEFT_SHIFT, "Знак [ << ]");
            put(Sign.RIGHT_SHIFT, "Знак [ >> ]");

//            put(Sign.SPACE, "Оператор [   ]");
//            put(Sign.NEW_LINE, "Оператор [ \\n ]");

            put(Code.Error.L3, "Ошибка: данного символа нет в алфавите");
            put(Code.Error.IN_CHAIN, "Ошибка: некоректный фрагмент цепочки");
            put(Code.Error.IN_KEY_WORD, "Ошибка: в написании служебного слова");
            put(Code.Error.IN_VARIABLE, "Ошибка: в написании имени переменной");
            put(Code.Error.IN_CONSTANT, "Ошибка: в написании константы");
            put(Code.Error.IN_DOUBLE_AMPERSAND, "Ошибка: в написании оператора [ && ]");
            put(Code.Error.IN_DOUBLE_EXCLAMATION_POINT, "Ошибка: в написании оператора [ !! ]");
            put(Code.Error.IN_LEFT_SHIFT, "Ошибка: в написании оператора [ << ]");
            put(Code.Error.IN_RIGHT_SHIFT, "Ошибка: в написании оператора [ >> ]");

            put(EOF, "\n");
        }};
    }

    public String interpret(int code) {
        return map.getOrDefault(code, "Серьезная ошибка: данный код отсутсвтует");
    }

    public String interpret(LogInfo log) {
        if (isEndOfLine(log)) return "\n";

        return log.toString() + " " + interpret(log.getAutomationPosition());
    }

    private boolean isEndOfLine(LogInfo log) {
        return log.getAutomationPosition() == EOF;
    }
}
