package fr.pjdevs.bar;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberExpression;
import javafx.beans.binding.StringBinding;

public final class CustomStringBindings  {
    private final static String DECIMAL_FORMAT = "%.2fE"; 

    private CustomStringBindings() {}

    public static final StringBinding createIntegerMoneyStringBinding(NumberExpression money) {
        return money.divide(100.0).asString(DECIMAL_FORMAT);
    }

    public static final StringBinding createPositiveIntegerMoneyStringBinding(NumberExpression money) {
        return Bindings
            .when(money.lessThan(0))
            .then(String.format(DECIMAL_FORMAT, 0.0))
            .otherwise(createIntegerMoneyStringBinding(money));
    }
}
