package lab2.util.variantNative;

public interface Code {
    int PROC = 100;
    int END = 101;
    int IDEN = 200;
    int DATA = 300;
    int SIGNS = 400;
    int ERRORS = 500;

    int BEGIN_CODE = 100;
    int END_CODE = 500;


    interface Sign {
        int EQUALLY = 400;
        int COMMA = 401;
        int DOUBLE_AMPERSAND = 402;
        int DOUBLE_EXCLAMATION_POINT = 403;
        int LEFT_SHIFT = 404;
        int RIGHT_SHIFT = 405;

//        int SPACE = 406;
//        int NEW_LINE = 407;
    }

    interface Error {
        int L3 = 500;
        int IN_CHAIN = 501;
        int IN_KEY_WORD = 502;
        int IN_VARIABLE = 503;
        int IN_CONSTANT = 504;
        int IN_DOUBLE_AMPERSAND = 505;
        int IN_DOUBLE_EXCLAMATION_POINT = 506;
        int IN_LEFT_SHIFT = 507;
        int IN_RIGHT_SHIFT = 508;
    }
}
