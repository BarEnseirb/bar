package fr.pjdevs.bar.models;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.pjdevs.bar.util.ChangedListenner;
import javafx.scene.paint.Color;

/**
 * Singleton class that represents the list of available {@link Item} for sale. 
 */
public final class ItemList {
    /**
     * Gson model to serialize and deserialize {@link Item}.
     */
    private class ItemModel {
        private String name;
        private int price;
        private String description;
        private String color;

        private ItemModel(String name, int price, String description, String color) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.color = color;
        }
    }

    /**
     * Path to JSON file where items are stored.
     */
    private final static String JSON_PATH = "data/items/items.json";
    /**
     * Singleton instance.
     */
    private static ItemList instance;

    /**
     * The represented item list.
     */
    private List<Item> itemList;
    /**
     * Listenner to changed events in this list.
     */
    private List<ChangedListenner> listenners;

    /**
     * Converts a list of models to real items.
     * @param models List of {@link ItemModel} to convert.
     * @return A list of {@link Item}.
     */
    private List<Item> modelsToItems(List<ItemModel> models) {
        List<Item> items = new ArrayList<Item>(models.size());

        for (ItemModel model : models) {
            items.add(new Item(model.name, model.price, model.description, Color.valueOf(model.color)));
        }

        return items;
    }

    /**
     * Converts a list of real items to a list of models.
     * @param items List of {@link Item} to convert.
     * @return A list of {@link ItemModel}.
     */
    private List<ItemModel> itemsToModels(List<Item> items) {
        List<ItemModel> models = new ArrayList<ItemModel>(items.size());
        
        for (Item item : items) {
            models.add(new ItemModel(item.getName(), item.getPrice(), item.getDesciption(), item.getColor().toString()));
        }

        return models;
    }

    /**
     * Loads the list of {@link Item} from the JSON file.
     * @return The list of {@link Item} loaded.
     * @throws IOException If the file is not accessible or cannot be created.
     */
    private List<Item> loadJsonItems() throws IOException{
        String json = Files.readString(Paths.get(JSON_PATH));
        List<ItemModel> models = new Gson().fromJson(json, (new TypeToken<List<ItemModel>>() {}).getType());

        return modelsToItems(models);
    }

    /**
     * Save the given list of item to the JSON file.
     * @param items The list of {@link Item} to save.
     * @throws IOException If the file is not accessible or cannot be created.
     */
    private void saveJsonItems(List<Item> items) throws IOException {
        List<ItemModel> models = this.itemsToModels(items);
        Files.writeString(Paths.get(JSON_PATH), new Gson().toJson(models));
    }

    /**
     * Private constructor for the Singleton class.
     * Loads the item list from the JSON file or creates it if it does not exist.
     * @throws IOException
     */
    private ItemList() throws IOException {
        try {
            this.itemList = this.loadJsonItems();
        } catch (Exception e) {
            this.itemList = new ArrayList<Item>();
            this.saveJsonItems(this.itemList);
        }

        this.listenners = new ArrayList<ChangedListenner>();
    }

    /**
     * Notifies every listenner that the list has changed.
     */
    private void listChanged() {
        for (ChangedListenner listenner : this.listenners) {
            listenner.onChanged();
        }
    }

    /**
    * Subscribes a new {@link ChangedListenner}.
    * @param listenner The listenner to subscribe.er
    */
    public void addListenner(ChangedListenner listenner) {
        this.listenners.add(listenner);
    }

    /**
     * Returns the Singleton instance.
     * @return The {@link ItemList} instance.
     * @throws IOException If the list cannot be created.
     */
    public static ItemList getInstance() throws IOException {
        if (instance == null) {
            instance = new ItemList();
        }
        
        return instance;
    }

    /**
     * Gets the list of {@link Items}.
     */
    public final List<Item> getList() {
        return this.itemList;
    }

    /**
     * Update the given item properties.
     * @param oldItem The item to update.
     * @param newItem The new item properties.
     * @throws IOException If the list cannot be saved.
     */
    public void update(Item oldItem, Item newItem) throws IOException {
        int index = this.itemList.indexOf(oldItem);
        this.itemList.set(index, newItem);
        this.saveJsonItems(this.itemList);
        this.listChanged();
    }

    /**
     * Adds a ne item to the list.
     * @param newItem The item to add.
     * @throws IOException If the list cannot be saved.
     */
    public void add(Item newItem) throws IOException {
        this.itemList.add(newItem);
        this.saveJsonItems(this.itemList);
        this.listChanged();
    }

    /**
     * Remove the given item from the list.
     * @param item The item to remove.
     * @throws IOException If the list cannot be saved.
     */
    public void remove(Item item) throws IOException {
        this.itemList.remove(item);
        this.saveJsonItems(this.itemList);
        this.listChanged();
    }
}
