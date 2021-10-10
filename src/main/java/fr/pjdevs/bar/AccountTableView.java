package fr.pjdevs.bar;

import java.math.BigDecimal;

import javafx.event.EventHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class AccountTableView extends TableView<Account> {

    private FilteredList<Account> filteredAccountList;

    private TableColumn<Account, String> loginColumn;
    private TableColumn<Account, String> nameColumn;
    private TableColumn<Account, String> moneyColumn;
    private TableColumn<Account, Integer> yearColumn;
    private TableColumn<Account, String> sectorColumn;
    private TableColumn<Account, BigDecimal> creditColumn;

    private ObjectProperty<TextField> filterFieldProperty;

    public AccountTableView() {
        this(null);
    }

    public AccountTableView(TextField filterField) {
        super();

        this.loginColumn = new TableColumn<Account, String>("Login");
        this.loginColumn.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        this.loginColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.nameColumn = new TableColumn<Account, String>("Nom");
        this.nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        this.nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());    

        this.moneyColumn = new TableColumn<Account, String>("Argent");
        this.moneyColumn.setCellValueFactory(new IntegerStringMoneyCallback<Account>(account -> account.moneyProperty()));

        this.yearColumn = new TableColumn<Account, Integer>("Annee");
        this.yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
        this.yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        this.sectorColumn = new TableColumn<Account, String>("Filiere");
        this.sectorColumn.setCellValueFactory(cellData -> cellData.getValue().sectorProperty());
        this.sectorColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.creditColumn = new TableColumn<Account, BigDecimal>("Credit");
        this.creditColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        this.getColumns().add(this.loginColumn);
        this.getColumns().add(this.nameColumn);
        this.getColumns().add(this.moneyColumn);
        this.getColumns().add(this.yearColumn);
        this.getColumns().add(this.sectorColumn);

        this.editableProperty().addListener((o, oldValue, newValue) -> {
            if (newValue && !oldValue) {
                this.getColumns().add(this.creditColumn);
            } else if (oldValue && !newValue) {
                this.getColumns().remove(this.creditColumn);
            }
        });
        this.filterFieldProperty = new SimpleObjectProperty<TextField>();

        this.setEditable(false);;
        this.filterFieldProperty.set(filterField);
    }

    public void setLoginOnEditCommit(EventHandler<CellEditEvent<Account, String>> value) {
        this.loginColumn.setOnEditCommit(value);
    }

    public void setNameOnEditCommit(EventHandler<CellEditEvent<Account, String>> value) {
        this.nameColumn.setOnEditCommit(value);
    }

    public void setYearOnEditCommit(EventHandler<CellEditEvent<Account, Integer>> value) {
        this.yearColumn.setOnEditCommit(value);
    }

    public void setSectorOnEditCommit(EventHandler<CellEditEvent<Account, String>> value) {
        this.sectorColumn.setOnEditCommit(value);
    }

    public void setCreditOnEditCommit(EventHandler<CellEditEvent<Account, BigDecimal>> value) {
        this.creditColumn.setOnEditCommit(value);
    }

    public void setAccountList(ObservableList<Account> accountList) {
        this.filteredAccountList = new FilteredList<Account>(accountList);

        if (this.filterFieldProperty.get() != null) {
            this.filterFieldProperty.get().textProperty().addListener((observable, oldValue, newValue) -> {
                filteredAccountList.setPredicate(account -> {
                    if (newValue == null || newValue.isBlank()) {
                        return true;
                    } else {
                        return account.getName().toLowerCase().contains(newValue.toLowerCase()) || account.getLogin().toLowerCase().contains(newValue.toLowerCase())
                            || String.valueOf(account.getYear()).equals(newValue) || account.getSector().equals(newValue);
                    }
                });
            });
        }

        this.setItems(this.filteredAccountList);
    }

    public TextField getFilterField() {
        return this.filterFieldProperty.get();
    }

    public void setFilterField(TextField value) {
        this.filterFieldProperty.set(value);
    }

    public ObjectProperty<TextField> filterFieldProperty() {
        return this.filterFieldProperty;
    }
}
