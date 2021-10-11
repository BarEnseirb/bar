package fr.pjdevs.bar.controls;

import java.util.List;

import fr.pjdevs.bar.models.Account;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 * Custom {@link Dialog} to pick an {@link Account} among a given list.
 */
public class AccountChoiceDialog extends Dialog<Account> {

    /**
     * Create an instance of this dialog.
     * @param accountList The list of accounts available which will be displayed.
     */
    public AccountChoiceDialog(List<Account> accountList) {
        // Dialog super
        super();

        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Paiement");
        this.setHeaderText("Choisissez un compte a crediter");
        this.setWidth(800);

        ButtonType chooseButtonType = new ButtonType("Choisir", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(chooseButtonType, ButtonType.CANCEL);

        // UI
        VBox mainLayout = new VBox();

        HBox topLayout = new HBox();
        topLayout.setSpacing(10.0);
        topLayout.setAlignment(Pos.CENTER);
        
        TextField filterField = new TextField();
        filterField.setPromptText("Filtre");

        topLayout.getChildren().add(filterField);
        HBox.setHgrow(filterField, Priority.ALWAYS);

        AccountTableView tableView = new AccountTableView();
        tableView.setEditable(false);
        tableView.setFilterField(filterField);

        mainLayout.getChildren().add(topLayout);
        mainLayout.getChildren().add(tableView);

        this.getDialogPane().setContent(mainLayout);

        // Data
        tableView.setAccountList(FXCollections.observableArrayList(accountList));;

        // Result
        this.setResultConverter(dialogButton -> {
            if (dialogButton == chooseButtonType) {
                return tableView.getSelectionModel().getSelectedItem();
            }
            return null;
        });
    }
}
