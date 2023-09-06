package tech.jimothy.design;

import java.util.ArrayList;

import tech.jimothy.db.Table;

public class EntityList {
    //List of entities
    private ArrayList<Entity> entityList = new ArrayList<>();

    public EntityList(Table table){
        for (int i =0; i < table.getSize(); i++){

            int id = Integer.valueOf(table.get(i, "id"));
            String name = table.get(i, "name");
            int bonus = Integer.valueOf(table.get(i, "bonus"));

            entityList.add(i, new Entity(id, name, bonus));
        }
    }

    public Entity getEntity(int index){
        return entityList.get(index);
    }

    @Override 
    public String toString(){
        String str = ""; 
        for (int i = 0; i < entityList.size(); i++){
            str += entityList.get(i).toString() + "\n";
        }
        return str;
    }
}
