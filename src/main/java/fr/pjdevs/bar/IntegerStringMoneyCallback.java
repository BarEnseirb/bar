package fr.pjdevs.bar;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class IntegerStringMoneyCallback<T> implements Callback<CellDataFeatures<T, String>, ObservableValue<String>> {

    private Callback<T, IntegerProperty> getIntegerProperty;

    public IntegerStringMoneyCallback(Callback<T, IntegerProperty> getIntegerProperty) {
        this.getIntegerProperty = getIntegerProperty;
    }

    @Override
    public ObservableValue<String> call(CellDataFeatures<T, String> cellData) {
        return Bindings.createStringBinding(() -> 
            String.format("%.2fE",
                getIntegerProperty.call(cellData.getValue()).get()/100.0),
                getIntegerProperty.call(cellData.getValue())
        );
    }
    
}
