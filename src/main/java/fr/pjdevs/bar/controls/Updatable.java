package fr.pjdevs.bar.controls;

import javafx.fxml.FXML;

/**
 * Interface which allows a component to be updatable.
 */
public interface Updatable {
    /**
     * FXML accessible method which updates this component.
     */
    @FXML
    public void update(); 
}
