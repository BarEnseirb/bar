package fr.pjdevs.bar;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AccountView extends HBox {
    @FXML
    Label loginLbl;
    @FXML
    Label nameLbl;
    @FXML
    Label moneyLbl;
    @FXML
    Label yearLbl;

    public AccountView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/AccountView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
    
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public AccountView(Account account) {
        this();

        loginLbl.setText(account.getLogin());
        nameLbl.setText(account.getName());
        moneyLbl.setText(account.getMoney().toString());
        yearLbl.setText(String.valueOf(account.getYear()));
    }
}
