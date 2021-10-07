package fr.pjdevs.bar;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class AccountPane extends VBox implements Updatable {
    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TextField loginField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField sectorField;
    @FXML
    private TextField nameFilterField;

    private ObservableList<Account> accountList;
    private FilteredList<Account> filteredAccountList;

    public AccountPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/AccountPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
    
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        TableColumn<Account, String> loginColumn = new TableColumn<Account, String>("Login");
        loginColumn.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        loginColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        loginColumn.setOnEditCommit(t -> {
            Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
            String oldLogin = account.getLogin();
            account.setLogin(t.getNewValue());
            updateAccount(oldLogin, account);
        });      

        TableColumn<Account, String> nameColumn = new TableColumn<Account, String>("Nom");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(t -> {
            Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
            account.setName(t.getNewValue());
            updateAccount(account.getLogin(), account);
        });      

        TableColumn<Account, String> moneyColumn = new TableColumn<Account, String>("Argent");
        moneyColumn.setCellValueFactory(new IntegerStringMoneyCallback<Account>(account -> account.moneyProperty()));

        TableColumn<Account, Integer> yearColumn = new TableColumn<Account, Integer>("Annee");
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        yearColumn.setOnEditCommit(t -> {
            Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
            account.setYear(t.getNewValue());
            updateAccount(account.getLogin(), account);
        });  

        
        TableColumn<Account, String> sectorColumn = new TableColumn<Account, String>("Filiere");
        sectorColumn.setCellValueFactory(cellData -> cellData.getValue().sectorProperty());
        sectorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sectorColumn.setOnEditCommit(t -> {
            Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
            account.setSector(t.getNewValue());
            updateAccount(account.getLogin(), account);
        });  

        TableColumn<Account, BigDecimal> creditColumn = new TableColumn<Account, BigDecimal>("Credit");
        creditColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        creditColumn.setOnEditCommit(t -> {
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

        this.accountTable.getColumns().add(loginColumn);
        this.accountTable.getColumns().add(nameColumn);
        this.accountTable.getColumns().add(moneyColumn);
        this.accountTable.getColumns().add(yearColumn);
        this.accountTable.getColumns().add(sectorColumn);
        this.accountTable.getColumns().add(creditColumn);

        this.accountList = FXCollections.observableArrayList();
        this.filteredAccountList = new FilteredList<Account>(this.accountList);
        this.nameFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAccountList.setPredicate(account -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                } else {
                    return account.getName().toLowerCase().contains(newValue.toLowerCase()) || account.getLogin().toLowerCase().contains(newValue.toLowerCase())
                        || String.valueOf(account.getYear()).equals(newValue) || account.getSector().equals(newValue);
                }
            });
        });

        this.accountTable.setItems(this.filteredAccountList);
        this.accountTable.setEditable(true);

        this.update();
    }

    private void createHistoryEntry(String login, int amount) {
        try (DatabaseConnection c = new DatabaseConnection()) {
            c.createHistoryEntry(login, amount > 0 ? "Credit" : "Debit", amount, Date.from(Instant.now()), "Mouvement");
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();;
        }        
    }

    private void updateAccount(String login, Account account) {
        try (DatabaseConnection c = new DatabaseConnection()) {
            c.updateAccount(login, account);
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();;
        }
    }

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
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

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

    @Override
    @FXML
    public void update() {
        try (DatabaseConnection c = new DatabaseConnection()) {
            this.accountList.clear();

            for (Account account : c.getAccountList()) {
                this.accountList.add(account);
            }
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

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
