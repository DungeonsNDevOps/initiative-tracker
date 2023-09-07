package tech.jimothy.gui.custom;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import tech.jimothy.design.Effect;
import tech.jimothy.design.Entity;

/**
 * A javafx component or 'widget' that is for displaying character/monster data.
 * This is the base for all other character widget classes.
 * CharacterWidget can be thought of as a complete representation of a character/monster.
 * It contains not only the code necessary to display information within the GUI but data
 * about the character as well. 
 */

public class CharacterWidget extends HBox{

    /**Name of the character */
    private String name;
    /**The character's unique ID */
    private int id;
    /**The character's initiative bonus */
    private int bonus; 
    /**The character's initiative */
    private int initiative;
    /**The effects the character is currently under */
    private ArrayList<Effect> effects = new ArrayList<>();
    /**The character's effect immunities */
    private ArrayList<Effect> immunities = new ArrayList<>();

    public CharacterWidget(){

        this.name = null;
        this.id = 0;
        this.bonus = 0;

        //Configuration
        this.setStyle("-fx-background-color: #AEB6B7;"+
                      "-fx-border-radius: 10 10 10 10;"
                      );

        /*Make Children */
        Label characterNameLabel = new Label(name);

        //Create blank in between label and button
        Region blankSpace = new Region();
        HBox.setHgrow(blankSpace, Priority.ALWAYS);

        this.getChildren().add(characterNameLabel);
        this.getChildren().add(blankSpace);

        

    }


    /**
     * Constructor takes an Entity object and gets the character's data from it. 
     * @param soul The essence of this class; Rather, all the important data is
     * extracted from this object. 
     */
    public CharacterWidget(Entity soul, int spacing){
        super(spacing);

        this.name = soul.toString();
        this.id = soul.getID();
        this.bonus = soul.getBonus();

        //Configuration
        this.setStyle("-fx-background-color: #AEB6B7;"+
                      "-fx-border-radius: 10 10 10 10;"
                      );

        /*Make Children */
        Label characterNameLabel = new Label(name);

        //Create blank in between label and button
        Region blankSpace = new Region();
        HBox.setHgrow(blankSpace, Priority.ALWAYS);

        this.getChildren().add(characterNameLabel);
        this.getChildren().add(blankSpace);

    }

    public CharacterWidget(int spacing, 
                           String name, 
                           int bonus, 
                           int id){
        super(spacing);
        
        this.name = name;
        this.id = id;
        this.bonus = bonus;

        //Configuration
        this.setStyle("-fx-background-color: #AEB6B7;"+
                      "-fx-border-radius: 10 10 10 10;"
                      );

        /*Make Children */
        Label characterNameLabel = new Label(name);

        //Create blank in between label and rest of children
        Region blankSpace = new Region();
        HBox.setHgrow(blankSpace, Priority.ALWAYS);

        this.getChildren().add(characterNameLabel);
        this.getChildren().add(blankSpace);

    }

    public Pane getContainer(){
        if(this.getParent() instanceof Pane){
            return (Pane)this.getParent();
        } else {
            return null;
        }
    }

    public String getName(){
        return this.name;
    }

    public int getID(){
        return this.id;
    }

    public int getBonus(){
        return this.bonus;
    }

    public int getInitiative(){
        return this.initiative;
    }

    public void setInitiative(int initiative){
        this.initiative = initiative + this.bonus;
    }

    public void addEffect(Effect effect){
        boolean alreadyContains = false;
        for(Effect anEffect : this.effects){
            if (anEffect.getID() == effect.getID()){
                alreadyContains = true;
                break;
            }
        }
        if(!alreadyContains){
            this.effects.add(effect);
        } else {
            ; //TODO: Create custom exception for when a duplicate effect has been attempted
        }
    }

    public ArrayList<Effect> getEffects(){
        return this.effects;
    }

    public void addImmunity(Effect immunity){
        this.immunities.add(immunity);
    }

    public ArrayList<Effect> getImmunities(){
        return this.immunities;
    }

    /**
     * Gets the first found occurence of effect with the name property as the one given
     * @param name The name to look for 
     * @return returns the first occurence of the effect - returns null if nothing matches
     */
    public Effect getEffect(String name){
        
        for(Effect effect : this.effects){
            if (effect.getName().equals(name)){
                return effect;
            }
        }
        return null;
    }

    /**
     * Gets the effect with a particular id. This method of obtaining an effect is preferred over 
     * getEffect(String name)
     * @param id the id to look for
     * @return returns an effect that has the given id - returns null if nothing matches
     */
    public Effect getEffect(int id){
        for (Effect effect : this.effects){
            if (effect.getID() == id){
                return effect;
            }
        }
        return null;
    }

    /**
     * Removes an effect from the character based on the given id
     * @param id id of effect to remove
     */
    public void removeEffect(int id){
        for (Effect effect : this.effects){
            if (effect.getID() == id){
                this.effects.remove(effect);
            }
        }
    }
    

}

