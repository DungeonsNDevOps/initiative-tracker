package tech.jimothy.design;

import javafx.scene.control.ListView;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.db.Table;

public class EffectItemType implements ItemType{
    @Override
    public void populateItems(ListView<Object> listView){
        Database database = new Database(DatabaseConfig.URL);

        Table itemsTable = database.query("SELECT * FROM effects");
        
        for (int i = 0; i < itemsTable.getSize(); i++){
            String effectDesc = itemsTable.get(i, "desc");
            int effectID = Integer.valueOf(itemsTable.get(i, "id"));
            String effectName = itemsTable.get(i, "name");
            int effectDuration = Integer.valueOf(itemsTable.get(i, "duration"));

            listView.getItems().add(new Effect(effectDesc, effectName, effectID, effectDuration));        
        }
    }
}
