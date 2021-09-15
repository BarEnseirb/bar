package fr.pjdevs.bar;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton class that represent the current cart as a list of items.
 */
public final class Cart {
    /**
     * Singleton instance.
     */
    private static Cart instance;

    /**
     * List of listenners for event {@link CartChangedListenner}
     */
    private ArrayList<CartChangedListenner> listenners;
    /**
     * List of {@link Item} in the cart associated with their count.
     */
    private Map<Item, Integer> items;

    /**
     * Calls {@link CartChangedListenner#onCartChanged} on all subscribed listenners in {@link #listenners}.
     */
    private void cartChanged() {
        for (CartChangedListenner listenner : this.listenners) {
            listenner.onCartChanged();
        }
    }

    /**
     * The private constructor intialize all attributes.
     */
    private Cart() {
        listenners = new ArrayList<CartChangedListenner>();
        items = new HashMap<Item, Integer>();
    }

    /**
     * Main method to access the singleton.
     * @return Returns the unique instance of the Singleton class.
     */
    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    /**
     * Subscribes a new {@link CartChangedListenner}.
     * @param listenner The listenner to subscribe.
     */
    public void addListenner(CartChangedListenner listenner) {
        this.listenners.add(listenner);
    }

    /**
     * Obtain the list of items.
     * @return Returns the list of associations {@link Item}/cout.
     */
    public final Map<Item, Integer> getItems() {
        return this.items;
    }

    /**
     * Adds an item to the cart.
     * @param item The {@link Item} to add to the cart.
     * @param count The number of {@code item} to add
     */
    public void add(Item item, int count) {
        Integer oldCount = this.items.get(item);

        if (oldCount == null) {
            this.items.put(item, count);
        } else {
            this.items.put(item, count + oldCount);
        }
    }

    /**
     * Removes an item from the cart.
     * @param item The {@link Item} to remove.
     */
    public void remove(Item item) {
        this.items.remove(item);

        this.cartChanged();
    }

    /**
     * Updates the count of an item in the cart.
     * @param item The {@link Item} to update.
     * @param count The new count of the {@code item}.
     */
    public void update(Item item, int count) {
        if (count <= 0) {
            this.items.remove(item);
        } else {
            this.items.replace(item, count);
        }

        this.cartChanged();
    }

    /**
     * 
     */
    public int getCount(Item item) {
        Integer count = this.items.get(item);
        return count == null ? 0 : count;
    }

    public void clear() {
        this.items.clear();
    }
}
