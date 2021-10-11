package fr.pjdevs.bar.util;

/**
 * Listenner interface for event related to changes/updates in an object.
 */
public interface ChangedListenner {

    /**
     * This method is called whenever a change occurs in the object
     * e.g. when an item is supressed/added/updated.
     */
    void onChanged();
}