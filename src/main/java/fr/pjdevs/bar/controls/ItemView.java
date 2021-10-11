package fr.pjdevs.bar.controls;

import java.io.IOException;
import java.util.Optional;

import fr.pjdevs.bar.models.Cart;
import fr.pjdevs.bar.models.Item;
import fr.pjdevs.bar.models.ItemList;
import fr.pjdevs.bar.util.MoneyStringBindings;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;

/**
 * Item view which displays an {@link Item} which can be adde to the {@link Cart} and edit/delete buttons.
 */
public class ItemView extends VBox {
    /**
     * The item represented by this view.
     */
    private Item item;

    /**
     * The label for the item's name.
     */
    @FXML
    private Label itemNameLbl;
    /**
     * The label for the item's price.
     */
    @FXML
    private Label itemPriceLbl;
    /**
     * The label for the item's description.
     */
    @FXML
    private Label itemDescriptionLbl;

    /**
     * Creates a new ItemView instance.
     * @param item The item to display.
     */
    public ItemView(Item item) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/ItemView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.item = item;
        this.itemNameLbl.textProperty().bind(this.item.nameProperty());
        this.itemPriceLbl.textProperty().bind(MoneyStringBindings.createIntegerMoneyStringBinding(this.item.priceProperty()));
        this.itemDescriptionLbl.textProperty().bind(this.item.descriptionProperty());
        this.backgroundProperty().bind(Bindings.createObjectBinding((() -> {
            return new Background(new BackgroundFill(this.item.getColor(), null, null));
        }), this.item.colorProperty()));;
    }

    /**
     * FXML accessible method to add this item to the {@link Cart} on click.
     */
    @FXML
    public void addToCart() {
        Cart.getInstance().add(this.item, 1);
    }

    /**
     * FXML accessible method to edit this item on edit button clicked
     * with an {@link ItemDialog}.
     */
    @FXML
    public void edit() {
        Optional<Item> newItem = new ItemDialog(this.item).showAndWait();

        if (newItem.isPresent()) {
            try {
                ItemList.getInstance().update(this.item, newItem.get());
            } catch (IOException e) {
                new Alert(AlertType.ERROR, "Impossible d'ouvrir la liste des items :\n" + e.getMessage()).show();
            }
        } else {
            new Alert(AlertType.ERROR, "L'item n'a pas ete mis a jour à cause de valeurs incorectes.").show();
        }
    }

    /**
     * FXML accessible method to remove this item on remove button clicked.
     */
    @FXML
    public void remove() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cet item ?");
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isEmpty() || res.get() == ButtonType.CANCEL) {
            return;
        }
        
        try {
            ItemList.getInstance().remove(this.item);
        } catch (IOException e) {
            new Alert(AlertType.ERROR, "Impossible d'ouvrir la liste des items :\n" + e.getMessage()).show();
        }
    }
}