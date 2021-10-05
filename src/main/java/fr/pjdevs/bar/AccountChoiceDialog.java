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
        this.setTitle("Paiement");
        this.setHeaderText("Choisissez un compte à créditer");
        this.setWidth(800);

        ButtonType chooseButtonType = new ButtonType("Choisir", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(chooseButtonType, ButtonType.CANCEL);

        // UI
        VBox mainLayout = new VBox();

        TableView<Account> tableView = new TableView<Account>();
        
        TableColumn<Account, String> loginColumn = new TableColumn<Account, String>("Login");
        loginColumn.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        TableColumn<Account, String> nameColumn = new TableColumn<Account, String>("Nom");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        TableColumn<Account, Integer> moneyColumn = new TableColumn<Account, Integer>("Argent");
        moneyColumn.setCellValueFactory(cellData -> cellData.getValue().moneyProperty().asObject());
        TableColumn<Account, Integer> yearColumn = new TableColumn<Account, Integer>("Annee");
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
        TableColumn<Account, String> sectorColumn = new TableColumn<Account, String>("Filiere");
        sectorColumn.setCellValueFactory(cellData -> cellData.getValue().sectorProperty());

        tableView.getColumns().add(loginColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(moneyColumn);
        tableView.getColumns().add(yearColumn);
        tableView.getColumns().add(sectorColumn);

        HBox bottomLayout = new HBox();
        bottomLayout.setSpacing(10.0);
        bottomLayout.setAlignment(Pos.CENTER);
        Label filterLabel = new Label("FIltre :");
        TextField nameFilterField = new TextField();
        nameFilterField.setPromptText("Login ou nom");

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
                    return account.getName().toLowerCase().contains(newValue.toLowerCase()) || account.getLogin().toLowerCase().contains(newValue.toLowerCase())
                        || String.valueOf(account.getYear()).equals(newValue) || account.getSector().equals(newValue);
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
