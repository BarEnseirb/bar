package fr.pjdevs.bar.controls;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import fr.pjdevs.bar.models.Account;
import fr.pjdevs.bar.models.DatabaseConnection;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * Account component where the account list is displayed
 * and where you can update acocunts properties or add accounts.
 */
public class AccountPane extends VBox implements Updatable {
    /**
     * The table to display account.
     */
    @FXML
    private AccountTableView accountTable;
    /**
     * Field for login when creating an account.
     */
    @FXML
    private TextField loginField;
    /**
     * Field for name when creating an account.
     */
    @FXML
    private TextField nameField;
    /**
     * Field for year when creating an account.
     */
    @FXML
    private TextField yearField;
    /**
     * Field for sector when creating an account.
     */
    @FXML
    private TextField sectorField;

    /**
     * The source list of all account for the table view. 
     */
    private ObservableList<Account> accountList;

    /**
     * Creates a new AccountPane instance.
     */
    public AccountPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/AccountPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
    
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.accountTable.setLoginOnEditCommit(t -> {
            Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
            String oldLogin = account.getLogin();
            account.setLogin(t.getNewValue());
            updateAccount(oldLogin, account);
        });      

        this.accountTable.setNameOnEditCommit(t -> {
            Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
            account.setName(t.getNewValue());
            updateAccount(account.getLogin(), account);
        });

        this.accountTable.setYearOnEditCommit(t -> {
            Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
            account.setYear(t.getNewValue());
            updateAccount(account.getLogin(), account);
        });  

        this.accountTable.setSectorOnEditCommit(t -> {
            Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
            account.setSector(t.getNewValue());
            updateAccount(account.getLogin(), account);
        });  

        this.accountTable.setCreditOnEditCommit(t -> {
            int amount = t.getNewValue().setScale(2).unscaledValue().intValue();
            Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
            int total = account.getMoney() + amount;

            if (total >= 0) {
                account.setMoney(total);
                updateAccount(account.getLogin(), account);
                createHistoryEntry(account.getLogin(), amount);
            } else {
                new Alert(AlertType.ERROR, "Le compte ne peut pas etre en negatif.").show();
            }
        });

        this.accountList = FXCollections.observableArrayList();
        this.accountTable.setAccountList(accountList);;
        this.update();
    }

    /**
     * Creates a new 'Mouvement' entry in the table {@code history} of the database.
     * @param login The login of the concerned account.
     * @param amount The amount of the 'Mouvement'.
     */
    private void createHistoryEntry(String login, int amount) {
        try (DatabaseConnection c = new DatabaseConnection()) {
            c.createHistoryEntry(login, amount > 0 ? "Credit" : "Debit", amount, Date.from(Instant.now()), "Mouvement");
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();;
        }        
    }

    /**
     * Update an account properties.
     * @param login The login of the account to update.
     * @param account The new account values.
     */
    private void updateAccount(String login, Account account) {
        try (DatabaseConnection c = new DatabaseConnection()) {
            c.updateAccount(login, account);
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();;
        }
    }

    /**
     * FXML accessible method to add an account when the create account's button is clicked.
     * It uses all the previously defined text field for the values and set money to 0. 
     */
    @FXML
    public void addAccount() {
        String login = this.loginField.getText().strip();
        String name =  this.nameField.getText().strip();
        String yearStr = this.yearField.getText().strip();
        String sector = this.sectorField.getText().strip();

        if (login.isBlank() || name.isBlank() || yearStr.isBlank() || sector.isBlank()) {
            return;
        }

        try (DatabaseConnection c = new DatabaseConnection()) {
            int year = Integer.valueOf(yearStr);
            if (year < 0) {
                throw new NumberFormatException("L'annee doit être positive");
            } else if (sector.length() > 1) {
                throw new Exception("La filiere doit etre N,E,I,M,R,S,T");
            }

            c.createAccount(login, name, year, sector);
            this.update();

            this.loginField.clear();
            this.nameField.clear();
            this.yearField.clear();
            this.sectorField.clear();

            new Alert(AlertType.INFORMATION, "Compte " + login + " ajoute avec succes.").show();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

    /**
     * FXML accessible method to remove the currently select account the the table view
     * when a button is cliked.
     */
    @FXML
    public void removeAccount() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce compte ?");
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isEmpty() || res.get() == ButtonType.CANCEL) {
            return;
        }

        try (DatabaseConnection c = new DatabaseConnection()) {
            String login = accountTable.getSelectionModel().getSelectedItem().getLogin();
            c.removeAccount(login);
            this.update();
            new Alert(AlertType.INFORMATION, "Compte " + login + " supprime avec succes.").show();
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

    /**
     * Updates the account list by reading it in the database.
     */
    public void updateAccountList() {
        try (DatabaseConnection c = new DatabaseConnection()) {
            this.accountList.clear();

            for (Account account : c.getAccountList()) {
                this.accountList.add(account);
            }
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

    @Override
    @FXML
    public void update() {
        this.updateAccountList();
    }

    /**
     * FXML accessible method to add one year to everybody when a button is clicked.
     */
    @FXML
    public void nextYear() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Voulez-vous passer une annee ?");
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isEmpty() || res.get() == ButtonType.CANCEL) {
            return;
        }

        try (DatabaseConnection c = new DatabaseConnection()) {
            c.nextYear();
            this.update();
            new Alert(AlertType.INFORMATION, "Toutes les annees ont ete incrementees.").show();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }
}
