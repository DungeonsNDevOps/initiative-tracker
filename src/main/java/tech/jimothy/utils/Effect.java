package tech.jimothy.utils;

/**
 * Class represents an in-game effect. It stores all of the pertinent data of an effect. 
 */

public class Effect {
    /**Description for effect*/
    private String desc;
    /**Name of effect */
    private String name;
    /**The unique ID of the effect */
    private int id;
    /**The amount of time in seconds that the effect lasts */
    int duration;

    public Effect(){
        this.desc = null;
        this.name = null;
        this.id = 0;
        this.duration = 0;
    }

    public Effect(String desc, String name, int id, int duration){
        this.desc = desc;
        this.name = name;
        this.id = id;
        this.duration = duration;
    }

    public String getDesc(){
        return this.desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getID(){
        return this.id;
    }
}
