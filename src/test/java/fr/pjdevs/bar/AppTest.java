package fr.pjdevs.bar;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void itemListJsonTest() throws Exception {
        String json = Files.readString(Paths.get("data/items/items.json"));
        ItemList list = ItemList.fromJson(json);

        Item item = list.getList().get(0);
        assertEquals(item.getName(), "Snickers");
        assertEquals(item.getPrice(), BigDecimal.valueOf(1.2));
        assertEquals(item.getImagePath(), "file:data/items/images/snickers.jpg");
    }
}
