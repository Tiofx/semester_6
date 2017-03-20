package lab3.main;

import lab2.util.variantNative.Code;
import lab3.util.Error;

import java.util.List;
import java.util.Stack;


public class Resolver {
    protected List<Integer> codes;
    protected int codeNumber = 0;
    protected Stack<Integer> errors = new Stack<>();

    public Resolver() {
    }

    public void setCodes(List<Integer> codes) {
        this.codes = codes;
        codes.add(-1);
    }

    public boolean check() {
        reset();

        try {
            return isFragment();
        } catch (Throwable e) {
            return false;
        }
    }

    public Stack<Integer> getErrors() {
        return errors;
    }


    protected boolean isFragment() {
        if (!isProc()) {
            errors.push(Error.Fragment.BEGIN_WITH_PROC);
            throw new IllegalArgumentException();
        }

        if (!isComma()) {
            errors.push(Error.Fragment.COMMA_AFTER_PROC);
            throw new IllegalArgumentException();
        }

        if (!isOperators()) {
            errors.push(Error.Fragment.OPERATORS);
            throw new IllegalArgumentException();
        }

        if (!isEnd()) {
            errors.push(Error.Fragment.END_AFTER_OPERATORS);
            throw new IllegalArgumentException();
        }

        errors.clear();

        if (!isComma()) {
            errors.push(Error.Fragment.COMMA_AFTER_END);
            throw new IllegalArgumentException();
        }

        if (codes.get(codeNumber) != -1) {
            errors.push(Error.Fragment.FRAGMENT_END);
            throw new IllegalArgumentException();
        }

        return true;
    }


    protected boolean isOperators() {
        if (!isOperator()) {
            errors.push(Error.Operators.BEGIN_WITH_OPERATOR);
//            throw new IllegalArgumentException();
            return false;
        }

        while (true) {
            if (!isOperator()) {
                codeNumber--;
                errors.push(Error.Operators.OPERATOR_AFTER_OPERATOR);
                return true;
            }
            errors.clear();
        }
    }

    protected boolean isOperator() {
        if (!isIden()) {
            errors.push(Error.Operator.BEGIN_WITH_IDEN);
//            throw new IllegalArgumentException();
            return false;
        }

        if (!isEqually()) {
            errors.push(Error.Operator.EQUALLY_AFTER_IDEN);
            throw new IllegalArgumentException();
//            return Error.Operator.EQUALLY_AFTER_IDEN;
        }

        if (!isExpression()) {
            errors.push(Error.Operator.EXPRESSION);
//            throw new IllegalArgumentException();
            return false;
        }

        if (!isComma()) {
            errors.push(Error.Operator.COMMA_AFTER_EXPRESSION);
            throw new IllegalArgumentException();
        }

        errors.clear();
        return true;
    }

    protected boolean isExpression() {
        if (!isOperand()) {
            errors.push(Error.Expression.BEGIN_WITH_OPERAND);
//            throw new IllegalArgumentException();
            return false;
        }

        while (true) {
            if (!isSign()) {
                codeNumber--;
                errors.push(Error.Expression.SIGN_AFTER_OPERAND);
                return true;
            } else {
                if (!isOperand()) {
                    errors.push(Error.Expression.OPERAND_AFTER_SIGN);
                    throw new IllegalArgumentException();
                } else {
                    errors.clear();
                }
            }
        }
    }

    protected boolean isOperand() {
        switch (codes.get(codeNumber++)) {
            case Code.IDEN:
            case Code.DATA:
                return true;
            default:
                errors.push(Error.OPERAND);
                return false;
        }
    }

    protected boolean isSign() {
        switch (codes.get(codeNumber++)) {
            case Code.Sign.DOUBLE_AMPERSAND:
            case Code.Sign.DOUBLE_EXCLAMATION_POINT:
            case Code.Sign.LEFT_SHIFT:
            case Code.Sign.RIGHT_SHIFT:
                return true;
            default:
                errors.push(Error.SIGN);
                return false;
        }
    }

    protected boolean isProc() {
        return codes.get(codeNumber++) == Code.PROC;
    }

    protected boolean isEnd() {
        return codes.get(codeNumber++) == Code.END;
    }

    protected boolean isIden() {
        return codes.get(codeNumber++) == Code.IDEN;
    }

    protected boolean isComma() {
        return codes.get(codeNumber++) == Code.Sign.COMMA;
    }

    protected boolean isEqually() {
        return codes.get(codeNumber++) == Code.Sign.EQUALLY;
    }


    public void reset() {
        codeNumber = 0;
        errors.clear();
    }
}