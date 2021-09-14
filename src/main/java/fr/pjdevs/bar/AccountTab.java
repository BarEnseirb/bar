package fr.pjdevs.bar;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.converter.IntegerStringConverter;

public class AccountTab extends Tab {
    @FXML
    private TableView<Account> accountTable;

    private ObservableList<Account> accountList;

    public AccountTab() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/AccountTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
    
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        TableColumn<Account, String> loginColumn = new TableColumn<Account, String>("Login");
        loginColumn.setCellValueFactory(cellData -> cellData.getValue().login);
        loginColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        loginColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Account, String>>(){
                @Override
                public void handle(CellEditEvent<Account, String> t) {
                    Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
                    String oldLogin = account.login.get();
                    account.login.set(t.getNewValue());
                    updateAccount(oldLogin, account);
                }
            }
        );      

        TableColumn<Account, String> nameColumn = new TableColumn<Account, String>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().name);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Account, String>>(){
                @Override
                public void handle(CellEditEvent<Account, String> t) {
                    Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
                    account.name.set(t.getNewValue());
                    updateAccount(account.login.get(), account);
                }
            }
        );      

        TableColumn<Account, Integer> moneyColumn = new TableColumn<Account, Integer>("Money");
        moneyColumn.setCellValueFactory(cellData -> cellData.getValue().money.asObject());
        moneyColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        moneyColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Account, Integer>>(){
                @Override
                public void handle(CellEditEvent<Account, Integer> t) {
                    Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
                    account.money.set(t.getNewValue());
                    updateAccount(account.login.get(), account);
                }
            }
        );  

        TableColumn<Account, Integer> yearColumn = new TableColumn<Account, Integer>("Year");
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().year.asObject());
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        yearColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Account, Integer>>(){
                @Override
                public void handle(CellEditEvent<Account, Integer> t) {
                    Account account = (Account)t.getTableView().getItems().get(t.getTablePosition().getRow());
                    account.year.set(t.getNewValue());
                    updateAccount(account.login.get(), account);
                }
            }
        );  

        this.accountTable.getColumns().add(loginColumn);
        this.accountTable.getColumns().add(nameColumn);
        this.accountTable.getColumns().add(moneyColumn);
        this.accountTable.getColumns().add(yearColumn);

        this.accountList = FXCollections.observableArrayList();
        this.accountTable.setItems(accountList);
        this.accountTable.setEditable(true);

        this.updateAccountList();
    }

    private void updateAccount(String login, Account account) {
        try (DatabaseConnection c = new DatabaseConnection()) {
            c.updateAccount(login, account);
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();;
        }
    }

    @FXML
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
}
