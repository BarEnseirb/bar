package fr.pjdevs.bar;

/**
 * Listenner interface for event related to changes/updates in the cart.
 */
public interface CartChangedListenner {

    /**
     * This method is called whenever a change occurs in the {@link Cart}
     * i.e. when an item is supressed. 
     */
    void onCartChanged();
}