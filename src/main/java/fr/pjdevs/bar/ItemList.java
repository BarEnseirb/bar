package fr.pjdevs.bar;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.scene.paint.Color;

public final class ItemList {
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

    private final static String JSON_PATH = "data/items/items.json"; 
    private static ItemList instance;

    private List<Item> itemList;
    private List<ChangedListenner> listenners;

    private List<Item> modelsToItems(List<ItemModel> models) {
        List<Item> items = new ArrayList<Item>(models.size());

        for (ItemModel model : models) {
            items.add(new Item(model.name, model.price, model.description, Color.valueOf(model.color)));
        }

        return items;
    }

    private List<ItemModel> itemsToModels(List<Item> items) {
        List<ItemModel> models = new ArrayList<ItemModel>(items.size());
        
        for (Item item : items) {
            models.add(new ItemModel(item.getName(), item.getPrice(), item.getDesciption(), item.getColor().toString()));
        }

        return models;
    }

    private List<Item> loadJsonItems() throws IOException{
        String json = Files.readString(Paths.get(JSON_PATH));
        List<ItemModel> models = new Gson().fromJson(json, (new TypeToken<List<ItemModel>>() {}).getType());

        return modelsToItems(models);
    }

    private void saveJsonItems(List<Item> items) throws IOException {
        List<ItemModel> models = this.itemsToModels(items);
        Files.writeString(Paths.get(JSON_PATH), new Gson().toJson(models));
    }

    private ItemList() throws IOException {
        try {
            this.itemList = this.loadJsonItems();
        } catch (Exception e) {
            this.itemList = new ArrayList<Item>();
            this.saveJsonItems(this.itemList);
        }

        this.listenners = new ArrayList<ChangedListenner>();
    }

    private void listChanged() {
        for (ChangedListenner listenner : this.listenners) {
            listenner.onChanged();
        }
    }

    public void addListenner(ChangedListenner listenner) {
        this.listenners.add(listenner);
    }

    public static ItemList getInstance() throws IOException {
        if (instance == null) {
            instance = new ItemList();
        }
        
        return instance;
    }

    public final List<Item> getList() {
        return this.itemList;
    }

    public void update(Item oldItem, Item newItem) throws IOException {
        int index = this.itemList.indexOf(oldItem);
        this.itemList.set(index, newItem);
        this.saveJsonItems(this.itemList);
        this.listChanged();
    }

    public void add(Item newItem) throws IOException {
        this.itemList.add(newItem);
        this.saveJsonItems(this.itemList);
        this.listChanged();
    }

    public void remove(Item item) throws IOException {
        this.itemList.remove(item);
        this.saveJsonItems(this.itemList);
        this.listChanged();
    }
}
