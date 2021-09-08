package fr.pjdevs.bar;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class ItemList {
    private static ItemList instance;

    private ArrayList<Item> itemList;

    private ArrayList<Item> listFromJson(String json) throws IOException {
        return new Gson().fromJson(json, (new TypeToken<List<Item>>() {}).getType());
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
