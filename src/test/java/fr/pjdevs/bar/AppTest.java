package fr.pjdevs.bar;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void itemListJsonTest() throws Exception {
        String json = Files.readString(Paths.get("file:data/items/items.json"));
        ItemList list = ItemList.fromJson(json);

        assertEquals(list.getList().get(0), new Item("Snickers", 1.2, "file:data/items/images/snickers.jpg"));
    }
}
