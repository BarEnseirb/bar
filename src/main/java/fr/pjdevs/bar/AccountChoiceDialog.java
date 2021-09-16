package fr.pjdevs.bar;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class AccountChoiceDialog extends Dialog<Account> {

    public AccountChoiceDialog(List<Account> accountList) {
        // Dialog super
        super();
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Payment");
        this.setHeaderText("Choose an account to pay");
        this.setWidth(800);

        ButtonType chooseButtonType = new ButtonType("Choose", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(chooseButtonType, ButtonType.CANCEL);

        // UI
        VBox mainLayout = new VBox();

        TableView<Account> tableView = new TableView<Account>();
        
        TableColumn<Account, String> loginColumn = new TableColumn<Account, String>("Login");
        loginColumn.setCellValueFactory(cellData -> cellData.getValue().login);
        TableColumn<Account, String> nameColumn = new TableColumn<Account, String>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().name);
        TableColumn<Account, Integer> moneyColumn = new TableColumn<Account, Integer>("Money");
        moneyColumn.setCellValueFactory(cellData -> cellData.getValue().money.asObject());
        TableColumn<Account, Integer> yearColumn = new TableColumn<Account, Integer>("Year");
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().year.asObject());
        TableColumn<Account, Integer> sectorColumn = new TableColumn<Account, Integer>("Sector");
        sectorColumn.setCellValueFactory(cellData -> cellData.getValue().sector.asObject());

        tableView.getColumns().add(loginColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(moneyColumn);
        tableView.getColumns().add(yearColumn);
        tableView.getColumns().add(sectorColumn);

        HBox bottomLayout = new HBox();
        bottomLayout.setSpacing(10.0);
        bottomLayout.setAlignment(Pos.CENTER);
        Label filterLabel = new Label("Filter :");
        TextField nameFilterField = new TextField();
        nameFilterField.setPromptText("Login/Name");

        bottomLayout.getChildren().add(filterLabel);
        bottomLayout.getChildren().add(nameFilterField);

        mainLayout.getChildren().add(tableView);
        mainLayout.getChildren().add(bottomLayout);

        this.getDialogPane().setContent(mainLayout);

        // Data
        FilteredList<Account> filteredAccountList = new FilteredList<Account>(FXCollections.observableArrayList(accountList));
        tableView.setItems(filteredAccountList);

        nameFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAccountList.setPredicate(account -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                } else {
                    return account.name.get().toLowerCase().contains(newValue.toLowerCase()) || account.login.get().toLowerCase().contains(newValue.toLowerCase())
                        || String.valueOf(account.year.get()).equals(newValue) || String.valueOf(account.sector.get()).equals(newValue);
                }
            });
        });

        this.setResultConverter(dialogButton -> {
            if (dialogButton == chooseButtonType) {
                return tableView.getSelectionModel().getSelectedItem();
            }
            return null;
        });
    }
}
