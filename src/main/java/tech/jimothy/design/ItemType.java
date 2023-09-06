package tech.jimothy.design;

import javafx.scene.control.ListView;

/**
 * For various types of items(items meant specifically for populating a javafx ListView)
 */
@FunctionalInterface
public interface ItemType {
    public void populateItems(ListView<Object> listView);

}
