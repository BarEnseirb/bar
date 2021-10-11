package fr.pjdevs.bar;

import javafx.scene.Node;
import javafx.scene.control.Tab;

/**
 * Custom {@link Tab} which updates its content on selection changed if it implements the {@link Updatable} interface.
 */
public class UpdateTab extends Tab {

    /**
     * Creates a new UpdateTab instance.
     */
    public UpdateTab() {
        super();
        this.updateOnChanged();
    }

    /**
     * Creates a new UpdateTab instance with given title.
     * @param text The title of the Tab.
     */
    public UpdateTab(String text) {
        super(text);
        this.updateOnChanged();
    }

    /**
     * Creates a new UpdateTab instance with given title and content.
     * @param text The title of the Tab.
     * @param content The content of the Tab.
     */
    public UpdateTab(String text, Node content) {
        super(text, content);
        this.updateOnChanged();
    }

    /**
     * Adds a listenner on the father {@link TabPane} to update this tab content when the slected tab is this one.
     */
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
