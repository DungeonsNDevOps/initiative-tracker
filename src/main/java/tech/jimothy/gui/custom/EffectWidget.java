package tech.jimothy.gui.custom;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import tech.jimothy.design.Effect;
import tech.jimothy.design.Observable;
import tech.jimothy.design.Observer;
import javafx.scene.layout.Region;

public class EffectWidget extends Pane implements Observer{
    
    private Effect effect;
    private ImageView icon;
    private Label name;
    private Button deleteButton;
    private CharacterWidget associatedChar;

    private HBox innerContainer;

    private Observable observableEffect;

    public EffectWidget(){
        this.effect = null;
        this.associatedChar = null;
    }

    public EffectWidget(Effect effect, CharacterWidget associatedChar){
        this.effect = effect;
        this.associatedChar = associatedChar;

        //Register this EffectWidget as an observer of the effect
        this.effect.registerObserver(this);

        

        this.innerContainer = new HBox(5);
        this.getChildren().add(this.innerContainer);

        this.icon = new ImageView();
        this.innerContainer.getChildren().add(this.icon);

        this.name = new Label(effect.getName());
        this.name.setAlignment(Pos.CENTER);
        this.innerContainer.getChildren().add(this.name);

        this.deleteButton = new Button("X");
        this.deleteButton.setFont(new Font(8));
        this.innerContainer.getChildren().add(deleteButton);
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

//*-------------------Observer Methods---------------------------------------------

    /**
     * Called only when the effect has notified this EffectWidget that it has expired
     */
    @Override
    public void update(){
        this.observableEffect.removeObserver(this);
        ((Pane)this.getParent()).getChildren().remove(this);
        this.effect = null;
        this.observableEffect = null;
    }

    @Override
    public void registerObservable(Observable o){
        this.observableEffect = o;
    }

//*--------------------------------------------------------------------------------
}
