package tech.jimothy.design;

import javafx.scene.control.ListView;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;

public class CharacterItem implements ItemType {
    @Override
    public void populateItems(ListView<Object> listView){
    Database database = new Database("./sqlite/inibase");

    Table itemsTable = database.query("SELECT * FROM characters");
    
    for (int i = 0; i < itemsTable.getSize(); i++){
        int charID = Integer.valueOf(itemsTable.get(i, "id"));
        String charName = itemsTable.get(i, "name");
        int charBonus = Integer.valueOf(itemsTable.get(i, "bonus"));

        listView.getItems().add(new Entity(charID, charName, charBonus));
    }
    }
}
