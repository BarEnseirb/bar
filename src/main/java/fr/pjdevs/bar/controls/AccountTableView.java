package fr.pjdevs.bar.controls;

import java.math.BigDecimal;

import fr.pjdevs.bar.models.Account;
import fr.pjdevs.bar.util.IntegerStringMoneyCallback;
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

/**
 * Custom {@link TableView} to display {@link Account}.
 */
public class AccountTableView extends TableView<Account> {

    /**
     * Wrapped source list to filter the account with a text field.
     */
    private FilteredList<Account> filteredAccountList;

    /**
     * The login column.
     */
    private TableColumn<Account, String> loginColumn;
    /**
     * The name column.
     */
    private TableColumn<Account, String> nameColumn;
    /**
     * The money column.
     */
    private TableColumn<Account, String> moneyColumn;
    /**
     * The year column.
     */
    private TableColumn<Account, Integer> yearColumn;
    /**
     * The sector column.
     */
    private TableColumn<Account, String> sectorColumn;
    /**
     * The credit column to add money to the account when the table is editable.
     */
    private TableColumn<Account, BigDecimal> creditColumn;

    /**
     * TextField property to fillter the accounts.
     */
    private ObjectProperty<TextField> filterFieldProperty;

    /**
     * Creates a new AccountTableView instance with no filter text field.
     */
    public AccountTableView() {
        this(null);
    }

    /**
     * Creates a new AccountTableView instance with a filter text field.
     * @param filterField The TextField to filter accounts.
     */
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

        this.setEditable(false);
        this.filterFieldProperty.set(filterField);
    }

    /**
     * Sets the event handler for commit when editing login column.
     * @param value The event handler.
     */
    public void setLoginOnEditCommit(EventHandler<CellEditEvent<Account, String>> value) {
        this.loginColumn.setOnEditCommit(value);
    }

    /**
     * Sets the event handler for commit when editing name column.
     * @param value The event handler.
     */
    public void setNameOnEditCommit(EventHandler<CellEditEvent<Account, String>> value) {
        this.nameColumn.setOnEditCommit(value);
    }

    /**
     * Sets the event handler for commit when editing year column.
     * @param value The event handler.
     */
    public void setYearOnEditCommit(EventHandler<CellEditEvent<Account, Integer>> value) {
        this.yearColumn.setOnEditCommit(value);
    }

    /**
     * Sets the event handler for commit when editing sector column.
     * @param value The event handler.
     */
    public void setSectorOnEditCommit(EventHandler<CellEditEvent<Account, String>> value) {
        this.sectorColumn.setOnEditCommit(value);
    }

    /**
    * Sets the event handler for commit when editing credit column.
     * @param value The event handler.
     */
    public void setCreditOnEditCommit(EventHandler<CellEditEvent<Account, BigDecimal>> value) {
        this.creditColumn.setOnEditCommit(value);
    }

    /**
     * Sets the account list of this table.
     * This method replaces the method {@link TableView#setItems} because it also wraps the source list in a FilteredList.
     * @param accountList The source account list.
     */
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

    /**
     * Gets the filter TextField.
     * @return The filter {@link TextField}.
     */
    public TextField getFilterField() {
        return this.filterFieldProperty.get();
    }

    /**
     * Sets the filter TextField.
     * @param value The new filter TextField.
     */
    public void setFilterField(TextField value) {
        this.filterFieldProperty.set(value);
    }

    /**
     * The filter TextField for the accounts.
     * @return The filter object property.
     */
    public ObjectProperty<TextField> filterFieldProperty() {
        return this.filterFieldProperty;
    }
}
