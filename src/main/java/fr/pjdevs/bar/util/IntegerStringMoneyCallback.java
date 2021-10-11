package fr.pjdevs.bar.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * Callback for the {@link CellValueFactory} to convert an {@link Integer} representing money to decimal {@link String}.
 * @param <T> Type of the model which contains the {@link IntegerProperty}.
 */
public class IntegerStringMoneyCallback<T> implements Callback<CellDataFeatures<T, String>, ObservableValue<String>> {
    /**
     * The callback to retrieve the {@link IntegerProperty}.
     */
    private Callback<T, IntegerProperty> getIntegerProperty;

    /**
     * Creates a new IntegerStringMoneyCallback instance.
     * @param getIntegerProperty The callback to call on {@link T} to get its {@link IntegerProperty}.
     */
    public IntegerStringMoneyCallback(Callback<T, IntegerProperty> getIntegerProperty) {
        this.getIntegerProperty = getIntegerProperty;
    }

    @Override
    public ObservableValue<String> call(CellDataFeatures<T, String> cellData) {
        return MoneyStringBindings.createIntegerMoneyStringBinding(getIntegerProperty.call(cellData.getValue()));
    }
    
}
