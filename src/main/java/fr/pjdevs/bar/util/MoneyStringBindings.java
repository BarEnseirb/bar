package fr.pjdevs.bar.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberExpression;
import javafx.beans.binding.StringBinding;

/**
 * Non-instantiable class to wrap some usefull predefined {@link StringBindings} related to money conversion.
 */
public final class MoneyStringBindings  {
    /**
     * The decimal format used to display money in this application.
     */
    private final static String DECIMAL_FORMAT = "%.2fE"; 

    /**
     * Private constructor to avoid instantiation of this class.
     */
    private MoneyStringBindings() {}

    /**
     * Binding that converts an integer to decimal string.
     * @param money The money as integer to use in the StringBinding.
     * @return The new StringBinding created.
     */
    public static final StringBinding createIntegerMoneyStringBinding(NumberExpression money) {
        return money.divide(100.0).asString(DECIMAL_FORMAT);
    }

    /**
     * Binding that converts an integer to decimal string and sets it to 0 if negative.
     * @param money The money as integer to use in the StringBinding.
     * @return The new StringBinding created.
     */
    public static final StringBinding createPositiveIntegerMoneyStringBinding(NumberExpression money) {
        return Bindings
            .when(money.lessThan(0))
            .then(String.format(DECIMAL_FORMAT, 0.0))
            .otherwise(createIntegerMoneyStringBinding(money));
    }
}
