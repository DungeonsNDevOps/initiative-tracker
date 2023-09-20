package tech.jimothy.design;

import javafx.scene.control.ListView;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import tech.jimothy.db.DatabaseConfig;

public class CharacterItemType implements ItemType {
    @Override
    public void populateItems(ListView<Object> listView){
    Database database = new Database(DatabaseConfig.URL);

    Table itemsTable = database.query("SELECT * FROM entities WHERE type = 'player' OR type = 'npc'");
    
    for (int i = 0; i < itemsTable.getSize(); i++){
        int charID = Integer.valueOf(itemsTable.get(i, "id"));
        String charName = itemsTable.get(i, "name");
        int charBonus = Integer.valueOf(itemsTable.get(i, "bonus"));
        String type = itemsTable.get(i, "type");

        listView.getItems().add(new Entity(charID, charName, charBonus, type));
    }
    }
}
