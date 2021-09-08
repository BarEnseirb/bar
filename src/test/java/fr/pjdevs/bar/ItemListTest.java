package fr.pjdevs.bar;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemListTest {

    @Test
    void itemListInstanceNotNull() throws Exception {
        assertNotEquals(ItemList.getInstance(), null);
    }

    @Test
    void itemListNotNull() throws Exception {
        assertNotEquals(ItemList.getInstance().getList(), null);
    }

    @Test
    void itemListNotEmpty() throws Exception {
        assertTrue(ItemList.getInstance().getList().size() > 0);
    }

    @Test
    void itemListItemsNotNull() throws Exception {
        assertNotEquals(ItemList.getInstance().getList().get(0), null);
    }

    @Test
    void itemListJsonTest() throws Exception {
        Item item = ItemList.getInstance().getList().get(0);

        assertEquals(item.getName(), "Snickers");
        assertEquals(item.getPrice(), new BigDecimal(BigInteger.valueOf(120), 2));
        assertEquals(item.getImagePath(), "file:data/items/images/snickers.jpg");
    }
}
