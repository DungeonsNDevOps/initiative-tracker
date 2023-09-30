package tech.jimothy.gui.custom;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import tech.jimothy.design.Effect;
import tech.jimothy.design.Entity;
import tech.jimothy.design.Observable;
import tech.jimothy.design.Observer;

/**
 * A javafx component or 'widget' that is for displaying entity data.
 * This is the base for all other entity widget classes.
 * entityWidget can be thought of as a complete representation of an entity.
 * It contains not only the code necessary to display information within the GUI but data
 * about the entity as well. 
 */

public class EntityWidget extends HBox implements Observer{

    /**Name of the entity */
    private String name;
    /**The entity's unique ID */
    private int id;
    /**The entity's initiative bonus */
    private int bonus; 
    /**The entity's type ('player', 'npc', 'monster')*/
    private String type;
    /**The entity's initiative */
    private int initiative;
    /**The effects the entity is currently under */
    private ArrayList<Effect> effects = new ArrayList<>();
    /**The entity's effect immunities */
    private ArrayList<Effect> immunities = new ArrayList<>();

    /**Remember that this is the current effect needing to be removed. 
     * entityWidget takes a look at this whenever the Observer.update() method is called
     * It is set by Observer.registerObservable()
     * It will change depending on which effect is currely notifying this Observer
     */
    Observable effectReadyForRemoval;

    public EntityWidget(){

        this.name = null;
        this.id = 0;
        this.bonus = 0;
        this.type = null;

        //Configuration
        this.setStyle("-fx-background-color: #AEB6B7;"+
                      "-fx-border-radius: 10 10 10 10;"
                      );

        /*Make Children */
        Label entityNameLabel = new Label(name);

        //Create blank in between label and button
        Region blankSpace = new Region();
        HBox.setHgrow(blankSpace, Priority.ALWAYS);

        this.getChildren().add(entityNameLabel);
        this.getChildren().add(blankSpace);

        

    }


    /**
     * Constructor takes an Entity object and gets the entity's data from it. 
     * @param soul The essence of this class; Rather, all the important data is
     * extracted from this object. 
     */
    public EntityWidget(Entity soul, int spacing){
        super(spacing);

        this.name = soul.toString();
        this.id = soul.getID();
        this.bonus = soul.getBonus();
        this.type = soul.getType();

        //Configuration
        this.setStyle("-fx-background-color: #AEB6B7;");

        /*Make Children */
        Label entityNameLabel = new Label(name);

        //Create blank in between label and button
        Region blankSpace = new Region();
        HBox.setHgrow(blankSpace, Priority.ALWAYS);

        this.getChildren().add(entityNameLabel);
        this.getChildren().add(blankSpace);

    }

    public EntityWidget(int spacing, 
                           String name, 
                           int bonus, 
                           int id,
                           String type){
        super(spacing);
        
        this.name = name;
        this.id = id;
        this.bonus = bonus;
        this.type = type;

        //Configuration
        this.setStyle("-fx-background-color: #AEB6B7;");

        /*Make Children */
        Label entityNameLabel = new Label(name);

        //Create blank in between label and rest of children
        Region blankSpace = new Region();
        HBox.setHgrow(blankSpace, Priority.ALWAYS);

        this.getChildren().add(entityNameLabel);
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

    public String getType(){
        return this.type;
    }

    public int getInitiative(){
        return this.initiative;
    }

    public void setInitiative(int initiative){
        this.initiative = initiative + this.bonus;
    }

    public boolean hasEffect(int effectID){
        for(Effect anEffect : this.effects){
            if (anEffect.getID() == effectID){
                return true;
            }
        }
        return false;
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

            //register this entity widget as an observer of the effect
            effect.registerObserver(this);
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
     * Removes an effect from the entity based on the given id
     * @param id id of effect to remove
     */
    public void removeEffect(int id){
        for (Effect effect : this.effects){
            if (effect.getID() == id){
                this.effects.remove(effect);
                break;
            }
        }
    }

    public void removeEffects(ArrayList<Effect> effects){
        this.effects.removeAll(effects);
    }
    

//*-------------------Observer Methods---------------------------------------------

    /**
     * Called only when an effect is ready for removal(it has expired)
     */
    @Override
    public void update(){
        removeEffect(((Effect)effectReadyForRemoval).getID());
        this.effectReadyForRemoval.removeObserver(this);
        this.effectReadyForRemoval = null;
    }

    @Override
    public void registerObservable(Observable o){
        this.effectReadyForRemoval = o;
    }

//*--------------------------------------------------------------------------------

}

