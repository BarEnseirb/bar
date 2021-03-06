package fr.pjdevs.bar.controls;

import fr.pjdevs.bar.models.Item;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 * Custom {@link Dialog} to choose/edit {@link Item} properties.
 */
public class ItemDialog extends Dialog<Item> {
    /**
     * Creates a new ItemDialog instance.
     * @param item The item used to fill properties field.
     * If the item is {@code null} the fields are empty.
     */
    public ItemDialog(Item item) {
        // Dialog super
        super();
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Editer");
        this.setHeaderText("Editez les proprietes de l'item");
        this.setWidth(800);

        ButtonType chooseButtonType = new ButtonType("Choisir", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(chooseButtonType, ButtonType.CANCEL);

        // UI
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(10.0);
        mainLayout.setVgap(10.0);
        mainLayout.setAlignment(Pos.CENTER);
        
        TextField nameField = new TextField(item != null ? item.getName() : "");
        TextField priceField = new TextField(item != null ? String.valueOf(item.getPrice()) : "");
        TextArea descriptionArea = new TextArea(item != null ? item.getDesciption() : "");
        GridPane.setVgrow(descriptionArea, Priority.ALWAYS);

        ColorPicker colorPicker = new ColorPicker(item != null ? item.getColor() : Color.BLUE);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == chooseButtonType) {
                try {
                    String name = nameField.getText();
                    String priceStr = priceField.getText();
                    String description = descriptionArea.getText();
                    int price = Integer.valueOf(priceField.getText());
                    
                    if (name.isBlank() || priceStr.isBlank()) {
                        throw new Exception();
                    }

                    return new Item(name, price, description, colorPicker.getValue());
                } catch (Exception e) {
                    return null;
                }
            }

            return null;
        });

        mainLayout.add(new Label("Nom:"), 0, 0);
        mainLayout.add(nameField, 1, 0);
        mainLayout.add(new Label("Prix (centimes):"), 0, 1);
        mainLayout.add(priceField, 1, 1);
        mainLayout.add(new Label("Description:"), 0, 2);
        mainLayout.add(descriptionArea, 1, 2);
        mainLayout.add(colorPicker, 0, 3);

        this.getDialogPane().setContent(mainLayout);
    }
}
