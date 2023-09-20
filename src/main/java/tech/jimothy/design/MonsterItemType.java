package tech.jimothy.design;

import javafx.scene.control.ListView;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.db.Table;

public class MonsterItemType implements ItemType {

    @Override
    public void populateItems(ListView<Object> listView) {
        Database database = new Database(DatabaseConfig.URL);

        Table monsterTable = database.query("SELECT * FROM entities WHERE type = 'monster'");

        for (int i = 0; i < monsterTable.getSize(); i++){
            int id = Integer.valueOf(monsterTable.get(i, "id"));
            String name = monsterTable.get(i, "name");
            int bonus = Integer.valueOf(monsterTable.get(i, "bonus"));

            listView.getItems().add(new Entity(id, name, bonus, "monster"));
            
        }
    }
}
