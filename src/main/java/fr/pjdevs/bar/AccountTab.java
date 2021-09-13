package fr.pjdevs.bar;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;

public class AccountTab extends Tab {
    @FXML
    VBox accountBox;

    public AccountTab() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/AccountTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
    
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.update();
    }

    @FXML
    public void update() {
        try {
            for (Account account : AccountList.getInstance().queryList()) {
                accountBox.getChildren().add(new AccountView(account));
            }
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }
}
