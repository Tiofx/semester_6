package lab3.util;

import java.util.HashMap;
import java.util.Map;

import static lab3.util.Error.*;

public class CodesAnalyser {
    protected final Map<Integer, String> map;

    public CodesAnalyser() {
        map = new HashMap<Integer, String>() {{

            put(UNDEFINED_CODE, "Неопреденная ошибка");
            put(FRAGMENT, "Ошибка в < фрагменте > программы");
            put(OPERATORS, "Ошибка в < операторах >");
            put(OPERATOR, "Ошибка в < операторе >");
            put(EXPRESSION, "Ошибка в < выражение >");
            put(OPERAND, "Ошибка в < операнде >");
            put(SIGN, "Ошибка в < знаке >");

            put(Fragment.BEGIN_WITH_PROC, "Ошибка < фрагмента > : фрагмент начинается со служебного слова < PROC >");
            put(Fragment.COMMA_AFTER_PROC, "Ошибка < фрагмента > : после служебного слова < PROC > должна быть < , >");
            put(Fragment.OPERATORS, "Ошибка < фрагмента > : после < PROC > < , > должны следовать < операторы >");
//            put(Fragment.OPERATORS, "Ошибка < фрагмента > : проблема в < операторах >");
            put(Fragment.END_AFTER_OPERATORS, "Ошибка < фрагмента > : после < операторов > должнен стоять < END >");
            put(Fragment.COMMA_AFTER_END, "Ошибка < фрагмента > : после < END > должна быть запятая");
            put(Fragment.FRAGMENT_END, "Ошибка < фрагмента > : < фрагмента > уже закончен");

            put(Operators.BEGIN_WITH_OPERATOR, "Ошибка < операторов > : < операторы > состоят хотя бы из одного < оператора >");
            put(Operators.OPERATOR_AFTER_OPERATOR, "Предупреждение < операторов > : после < оператора > может следовать < оператор >");

            put(Operator.BEGIN_WITH_IDEN, "Ошибка < оператора > : < оператор > начинается с < IDEN >");
            put(Operator.EQUALLY_AFTER_IDEN, "Ошибка < оператора > : после < IDEN > должен быть знак [ = ]");
            put(Operator.EXPRESSION, "Ошибка < оператора > : после < IDEN > < = > должно следовать выражение");
            put(Operator.COMMA_AFTER_EXPRESSION, "Ошибка < оператора > : < оператор > заканчивается < , >");

            put(Expression.BEGIN_WITH_OPERAND, "Ошибка < выражения > : < выражение > начинается с < операнда >");
            put(Expression.SIGN_AFTER_OPERAND, "Предупреждение < выражения > : после < операнда > может следовать < знак >");
            put(Expression.OPERAND_AFTER_SIGN, "Ошибка < выражения > : после < знак > должен следовать < операнд >");
        }};
    }

    public String interpret(int code) {
        return map.getOrDefault(code, "Серьезная ошибка: код данной ошибки отсутсвует!!!");
    }
}