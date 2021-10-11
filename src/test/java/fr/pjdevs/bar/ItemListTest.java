package fr.pjdevs.bar;

import org.junit.jupiter.api.Test;

import fr.pjdevs.bar.models.ItemList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ItemList}.
 */
class ItemListTest {

    /**
     * Tests if the singleton instance itself is not {@code null}.
     * @throws Exception
     */
    @Test
    void itemListInstanceNotNull() throws Exception {
        assertNotEquals(ItemList.getInstance(), null);
    }

    /**
     * Tests if the list of the singleton instance is not {@code null}.
     * @throws Exception
     */
    @Test
    void itemListNotNull() throws Exception {
        assertNotEquals(ItemList.getInstance().getList(), null);
    }

    /**
     * Tests if the list of the singleton instance is not empty.
     * @throws Exception
     */
    @Test
    void itemListItemsNotNull() throws Exception {
        assertNotEquals(ItemList.getInstance().getList().get(0), null);
    }
}
