package fr.pjdevs.bar;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class ItemList {
    private class ItemModel {
        String name;
        int price;
        String description;
    }

    private static ItemList instance;

    private List<Item> itemList;
    private List<ChangedListenner> listenners;

    private List<Item> listFromJson(String json) throws IOException {
        List<ItemModel> models = new Gson().fromJson(json, (new TypeToken<List<ItemModel>>() {}).getType());
        
        List<Item> list = new ArrayList<Item>(models.size());
        for (ItemModel model : models) {
            list.add(new Item(model.name, model.price, model.description));
        }

        return list;
    }

    private ItemList() {
        try {
            String json = Files.readString(Paths.get("data/items/items.json"));
            this.itemList = this.listFromJson(json);
        } catch (Exception e) {
            this.itemList = new ArrayList<Item>();
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

    public static ItemList getInstance() {
        if (instance == null) {
            instance = new ItemList();
        }
        
        return instance;
    }

    public final List<Item> getList() {
        return this.itemList;
    }

    public void update(Item oldItem, Item newItem) {
        int index = this.itemList.indexOf(oldItem);
        this.itemList.set(index, newItem);
        this.listChanged();
    }

    public void add(Item newItem) {
        this.itemList.add(newItem);
        this.listChanged();
    }
}
