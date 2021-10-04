package fr.pjdevs.bar;

import javafx.scene.Node;
import javafx.scene.control.Tab;

public class UpdateTab extends Tab {
    public UpdateTab() {
        super();
        this.updateOnChanged();
    }

    public UpdateTab(String text) {
        super(text);
        this.updateOnChanged();
    }

    public UpdateTab(String text, Node content) {
        super(text, content);
        this.updateOnChanged();
    }

    private void updateOnChanged() {
        this.tabPaneProperty().addListener((oTab, oldPane, newPane) -> {
            newPane.getSelectionModel().selectedItemProperty().addListener((oPane, oldTab, newTab) -> {
                if (newTab == this && this.getContent() instanceof Updatable) {
                    ((Updatable)this.getContent()).update();
                }
            });
        });
    }
}
