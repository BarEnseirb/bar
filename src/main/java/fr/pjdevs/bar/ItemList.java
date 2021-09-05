package fr.pjdevs.bar;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class ItemList {
    private ArrayList<Item> itemList;

    public ItemList(List<Item> itemList) {
        this.itemList = new ArrayList<Item>(itemList);
    }

    public ArrayList<Item> getList() {
        return this.itemList;
    }

    public static ItemList fromJson(String json) throws Exception {
        return new Gson().fromJson(json, ItemList.class);
    }
}
