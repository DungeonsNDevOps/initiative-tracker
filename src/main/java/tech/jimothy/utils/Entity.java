package tech.jimothy.utils;

public class Entity {
    //numerical identification of the entity
    private int id;
    //the name of the identity
    private String name;
    //the entity's initiative bonus
    private int bonus;

    public Entity(int id, String name, int bonus){
        this.id = id;
        this.name = name;
        this.bonus = bonus;
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
}
