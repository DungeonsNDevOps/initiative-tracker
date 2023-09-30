package tech.jimothy.gui.custom;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import tech.jimothy.design.Effect;
import tech.jimothy.design.Observable;
import tech.jimothy.design.Observer;

public class EffectWidget extends VBox implements Observer{
    
    private Effect effect;
    private ImageView icon;
    private Label name;
    private Button deleteButton;
    private EntityWidget associatedChar;
    Label timeLeft;

    private boolean descDisplayed = false;

    private HBox innerContainer;

    private Observable observableEffect;

    public EffectWidget(){
        this.effect = null;
        this.associatedChar = null;
    }

    public EffectWidget(Effect effect, EntityWidget associatedChar){
        this.effect = effect;
        this.associatedChar = associatedChar;

        //Register this EffectWidget as an observer of the effect
        this.effect.registerObserver(this);

        //Create and add the inner HBox
        this.innerContainer = new HBox(5);
        //set styling for now
        this.innerContainer.setStyle("-fx-background-color: #AEB6B7;");
        this.getChildren().add(this.innerContainer);

        //*for future ability to add icon to effect 
        // this.icon = new ImageView();
        // this.innerContainer.getChildren().add(this.icon);

        //create and add name label
        this.name = new Label(effect.getName());
        this.name.setAlignment(Pos.CENTER);
        this.innerContainer.getChildren().add(this.name);

        //puts a space in between name and delete button, pushing the delete button to the horizontal edge
        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);
        this.innerContainer.getChildren().add(space);

        //create and add label for displaying remaining time on the effect
        timeLeft = new Label(this.grabEffect().getDuration() + "s");
        this.innerContainer.getChildren().add(timeLeft);

        //Create and add delete button
        this.deleteButton = new Button("X");
        this.deleteButton.setFont(new Font(8));
        this.innerContainer.getChildren().add(deleteButton);
        setDeleteFunc();

        //Set on click functionality - should display effect description on click
        this.setOnMouseClicked((event) -> {displayDesc();});

        //Ensure displayed time is up to date
        updateDisplayedTimeLeft();

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
            //remove the effect from the associated entity
            this.associatedChar.removeEffect(this.effect.getID());
            if (this.getParent() instanceof Pane){
                //remove this effect widget from the parent
                ((Pane)this.getParent()).getChildren().remove(this);
                this.effect.removeObserver(this);
            }
        });
    }

    private void displayDesc(){
        if (!this.descDisplayed){
            Text description = new Text(10, 10, this.grabEffect().getDesc());
            double effectWidgetParentWidth = ((Pane)this.getParent()).getWidth();
            description.setWrappingWidth(effectWidgetParentWidth);
            this.getChildren().add(description);
            this.descDisplayed = true;
        } else{
            this.getChildren().remove(1);
            this.descDisplayed = false;
        }
    }

    public void updateDisplayedTimeLeft(){
        this.timeLeft.setText((this.grabEffect().getDuration() - this.grabEffect().getTimePassed()) + "s");
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
