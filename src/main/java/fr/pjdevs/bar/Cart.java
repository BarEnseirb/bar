package fr.pjdevs.bar;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public final class Cart {
    private static Cart instance;

    private ArrayList<CartChangedListenner> listenners;
    private Map<Item, Integer> items;

    private void cartChanged() {
        for (CartChangedListenner listenner : this.listenners) {
            listenner.onCartChanged();
        }
    }

    private Cart() {
        listenners = new ArrayList<CartChangedListenner>();
        items = new HashMap<Item, Integer>();
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addListenner(CartChangedListenner listenner) {
        this.listenners.add(listenner);
    }

    public final Map<Item, Integer> getItems() {
        return this.items;
    }

    public void add(Item item, int count) {
        this.items.put(item, count);
    }

    public void remove(Item item) {
        this.items.remove(item);

        this.cartChanged();
    }

    public void update(Item item, int count) {
        if (count <= 0) {
            this.items.remove(item);
        } else {
            this.items.replace(item, count);
        }

        this.cartChanged();
    }

    public int getCount(Item item) {
        Integer count = this.items.get(item);
        return count == null ? 0 : count;
    }

    public void clear() {
        this.items.clear();
    }
}
