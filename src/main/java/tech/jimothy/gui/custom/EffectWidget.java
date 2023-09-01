package tech.jimothy.gui.custom;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import tech.jimothy.utils.Effect;

public class EffectWidget extends Pane{
    
    private Effect effect;
    private ImageView icon;
    private Label name;
    private Button deleteButton;
    private CharacterWidget associatedChar;

    private HBox innerContainer;

    public EffectWidget(){
        this.effect = null;
        this.associatedChar = null;
    }

    public EffectWidget(Effect effect, CharacterWidget associatedChar){
        this.effect = effect;
        this.associatedChar = associatedChar;

        this.innerContainer = new HBox(5);
        this.getChildren().add(this.innerContainer);

        icon = new ImageView();
        this.innerContainer.getChildren().add(this.icon);

        this.name = new Label(effect.getName());
        this.innerContainer.getChildren().add(this.name);

        this.deleteButton = new Button("X");
        this.getChildren().add(deleteButton);
        this.deleteButton.setLayoutX(this.getWidth());
        this.deleteButton.setLayoutY(0);
        setDeleteFunc();

    }
    
    public Effect grabEffect(){
        return this.effect;
    }

    public void setEffect(Effect effect){
        this.effect = effect;
    }

    /**
     * Sets the functionality of the delete button
     */
    private void setDeleteFunc(){
        //set what happens when button is pressed
        this.deleteButton.setOnAction(event -> {
            //remove the effect from the associated character
            this.associatedChar.removeEffect(this.effect.getID());
            if (this.getParent() instanceof Pane){
                //remove this effect widget from the parent
                ((Pane)this.getParent()).getChildren().remove(this);
            }
            
        });
    }
}
