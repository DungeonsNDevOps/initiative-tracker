package tech.jimothy.gui.custom;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import tech.jimothy.utils.Entity;

/**
 * A javafx component or 'widget' that is for displaying character/monster data.
 * This is the base for all other character widget classes.
 */

public class CharacterWidget extends HBox{

    /**Name of the character */
    private String name;
    /**The character's unique ID */
    private String id;
    /**The character's initiative bonus */
    private String bonus; 

    /**
     * Constructor takes an Entity object and gets the character's data from it. 
     * @param soul The essence of this class; Rather, all the important data is
     * extracted from this object. 
     */
    public CharacterWidget(Entity soul, int spacing, double width){
        super(spacing);

        this.name = soul.toString();
        this.id = Integer.toString(soul.getID());
        this.bonus = Integer.toString(soul.getBonus());

        //Configuration
        this.setStyle("-fx-background-color: #AEB6B7;"+
                      "-fx-border-radius: 10 10 10 10;"
                      );
        this.setMinWidth(width);

        /*Make Children */
        Label characterNameLabel = new Label(name);

        //Create blank in between label and button
        Region blankSpace = new Region();
        HBox.setHgrow(blankSpace, Priority.ALWAYS);

        this.getChildren().add(characterNameLabel);
        this.getChildren().add(blankSpace);

    }

    public CharacterWidget(int spacing, 
                           double width, 
                           String name, 
                           String bonus, 
                           String id){
        super(spacing);
        
        this.name = name;
        this.id = id;
        this.bonus = bonus;

        //Configuration
        this.setStyle("-fx-background-color: #AEB6B7;"+
                      "-fx-border-radius: 10 10 10 10;"
                      );
        this.setMinWidth(width);

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

    public String getID(){
        return this.id;
    }

    public String getBonus(){
        return this.bonus;
    }
}