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

    private List<Item> listFromJson(String json) throws IOException {
        List<ItemModel> models = new Gson().fromJson(json, (new TypeToken<List<ItemModel>>() {}).getType());
        
        List<Item> list = new ArrayList<Item>(models.size());
        for (ItemModel model : models) {
            list.add(new Item(model.name, model.price, model.description));
        }

        return list;
    }

    private ItemList() throws IOException {
        String json = Files.readString(Paths.get("data/items/items.json"));
        this.itemList = this.listFromJson(json);
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
}
