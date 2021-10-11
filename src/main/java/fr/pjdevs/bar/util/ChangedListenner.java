package fr.pjdevs.bar.util;

import fr.pjdevs.bar.models.Cart;

/**
 * Listenner interface for event related to changes/updates in an object.
 */
public interface ChangedListenner {

    /**
     * This method is called whenever a change occurs in the object
     * e.g. when an item is supressed/added/updated in the {@link Cart}. 
     */
    void onChanged();
}