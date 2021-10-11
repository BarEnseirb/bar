package fr.pjdevs.bar.controls;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.pjdevs.bar.models.DatabaseConnection;
import fr.pjdevs.bar.models.HistoryEntry;
import fr.pjdevs.bar.util.IntegerStringMoneyCallback;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;

/**
 * History component which contains the table of the history.
 */
public class HistoryPane extends VBox implements Updatable {
    /**
     * The table to display the history.
     */
    @FXML
    private TableView<HistoryEntry> historyTable;
    /**
     * The filter text field to filter entries.
     */
    @FXML
    private TextField filterField;

    /**
     * The source list of the entries for the table.
     */
    private ObservableList<HistoryEntry> historyList;
    /**
     * The filtered list of the entries.
     */
    private FilteredList<HistoryEntry> filteredHistoryList;

    /**
     * Creates a new HistoryPane instance.
     */
    public HistoryPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/HistoryPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
    
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        TableColumn<HistoryEntry, String> studentLoginColumn = new TableColumn<HistoryEntry, String>("Login");
        studentLoginColumn.setCellValueFactory(cellData -> cellData.getValue().studentLoginProperty());
        studentLoginColumn.setCellFactory(TextFieldTableCell.forTableColumn());    

        TableColumn<HistoryEntry, String> productColumn = new TableColumn<HistoryEntry, String>("Produit");
        productColumn.setCellValueFactory(cellData -> cellData.getValue().productProperty());
        productColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<HistoryEntry, String> priceColumn = new TableColumn<HistoryEntry, String>("Prix");
        priceColumn.setCellValueFactory(new IntegerStringMoneyCallback<HistoryEntry>(entry -> entry.priceProperty()));

        TableColumn<HistoryEntry, Date> dateColumn = new TableColumn<HistoryEntry, Date>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter("yyyy-MM-dd HH:mm:ss")));
        
        TableColumn<HistoryEntry, String> transactionColumn = new TableColumn<HistoryEntry, String>("Transaction");
        transactionColumn.setCellValueFactory(cellData -> cellData.getValue().transactionProperty());
        transactionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.historyTable.getColumns().add(studentLoginColumn);
        this.historyTable.getColumns().add(productColumn);
        this.historyTable.getColumns().add(priceColumn);
        this.historyTable.getColumns().add(dateColumn);
        this.historyTable.getColumns().add(transactionColumn);

        this.historyList = FXCollections.observableArrayList();
        this.filteredHistoryList = new FilteredList<HistoryEntry>(this.historyList);
        this.filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            this.filteredHistoryList.setPredicate(entry -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                } else {
                    return entry.getStudentLogin().toLowerCase().contains(newValue.toLowerCase()) || entry.getProduct().toLowerCase().contains(newValue.toLowerCase())
                        || new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entry.getDate()).contains(newValue) || entry.getTransaction().toLowerCase().equals(newValue.toLowerCase());
                }
            });
        });

        this.historyTable.setItems(this.filteredHistoryList.sorted((o1, o2) -> -o1.getDate().compareTo(o2.getDate())));
        this.historyTable.setEditable(false);

        this.update();
    }

    @Override
    @FXML
    public void update() {
        try (DatabaseConnection c = new DatabaseConnection()) {
            this.historyList.clear();

            for (HistoryEntry HistoryEntry : c.getHistoryEntryList()) {
                this.historyList.add(HistoryEntry);
            }
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }
}
