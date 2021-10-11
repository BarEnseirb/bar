package fr.pjdevs.bar;

import java.io.IOException;
import java.util.Optional;

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

public class ItemView extends VBox {
    private Item item;

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

    @FXML
    private Label itemNameLbl;
    @FXML
    private Label itemPriceLbl;
    @FXML
    private Label itemDescriptionLbl;

    @FXML
    public void addToCart() {
        Cart.getInstance().add(this.item, 1);
    }

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