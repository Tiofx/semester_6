package lab2.util;

import lab2.main.Automation;
import lab2.main.NullAutomation;
import lab2.util.variantNative.VariantNumber21;

import java.util.HashMap;

public final class AutomationFactory {
    protected static HashMap<Integer, AbstractVariant> variantBuilder =
            new HashMap<Integer, AbstractVariant>() {{
                put(21, new VariantNumber21());
            }};

    public static Automation createAutomation(int variantNumber) {
        switch (variantNumber) {
            case 21:
                AbstractVariant builder = variantBuilder.get(variantNumber);
                return new Automation(
                        builder.getAlphabet(),
                        builder.getTransitionTable(),
                        builder.getFirstState(),
                        builder.getFinalStates());
            default:
                return new NullAutomation();
        }
    }
}
