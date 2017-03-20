package lab3.util;

public interface Error {
    int UNDEFINED_CODE = 1000;

    int FRAGMENT = 1100;
    int OPERATORS = 1200;
    int OPERATOR = 1300;
    int EXPRESSION = 1400;
    int OPERAND = 1500;
    int SIGN = 1600;

    interface Fragment {
        int BEGIN_WITH_PROC = 1101;
        int COMMA_AFTER_PROC = 1102;

        int OPERATORS = 1103;

        int END_AFTER_OPERATORS = 1104;
        int COMMA_AFTER_END = 1105;
        int FRAGMENT_END = 1106;
    }

    interface Operators {
        int BEGIN_WITH_OPERATOR = 1201;
        int OPERATOR_AFTER_OPERATOR = 1202;
    }

    interface Operator {
        int BEGIN_WITH_IDEN = 1301;
        int EQUALLY_AFTER_IDEN = 1302;

        int EXPRESSION = 1303;

        int COMMA_AFTER_EXPRESSION = 1304;
    }

    interface Expression {
        int BEGIN_WITH_OPERAND = 1401;
        int SIGN_AFTER_OPERAND = 1402;
        int OPERAND_AFTER_SIGN = 1403;
    }

    interface Operand {
        int IDEN = 1501;
        int DATA = 1501;
    }

    interface Sign {
        int DOUBLE_AMPERSAND = 1601;
        int DOUBLE_EXCLAMATION_POINT = 1601;
        int LEFT_SHIFT = 1601;
        int RIGHT_SHIFT = 1601;
    }
}
