package fr.pjdevs.bar;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

public class AccountTab extends Tab {
    @FXML
    private GridPane accountBox;

    private int accountNb;

    private void addAccount(Account account) {
        accountBox.add(new Label(account.getLogin()), 0, accountNb, 1, 1);
        accountBox.add(new Label(account.getName()), 1, accountNb, 1, 1);
        accountBox.add(new Label(account.getMoney().toPlainString() + "E"), 2, accountNb, 1, 1);
        accountBox.add(new Label(String.valueOf(account.getYear())), 3, accountNb, 1, 1);

        accountNb++;
    }

    public AccountTab() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/AccountTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
    
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.accountNb = 0;
        this.update();
    }

    @FXML
    public void update() {
        try {
            for (Account account : AccountList.getInstance().getList()) {
                this.addAccount(account);
            }
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }
}
