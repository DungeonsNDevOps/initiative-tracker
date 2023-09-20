package tech.jimothy.design;

public class Entity {
    //numerical identification of the entity
    private int id;
    //the name of the identity
    private String name;
    //the entity's initiative bonus
    private int bonus;
    //the entity's type
    private String type;

    public Entity(int id, String name, int bonus, String type){
        this.id = id;
        this.name = name;
        this.bonus = bonus;
        this.type = type;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public int getID(){
        return this.id;
    }

    public int getBonus(){
        return this.bonus;
    }

    public String getType(){
        return this.type;
    }
}
