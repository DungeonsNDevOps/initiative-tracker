package tech.jimothy.gui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tech.jimothy.db.DataShare;
import tech.jimothy.gui.custom.CharacterWidget;
import tech.jimothy.gui.custom.EffectWidget;
import tech.jimothy.utils.CharacterSortTools;
import tech.jimothy.utils.Effect;

import java.util.ArrayList;

public class CombatController {

    @FXML Parent root;
    @FXML VBox charactersContainer;
    @FXML Label roundsLabel;
    @FXML Label timeLabel;
    @FXML private VBox effectsView;

    //the amount of rounds that have taken place
    int rounds = 0;
    //time in seconds that has taken place in combat
    int time = 0;
    //the amount of turns that have taken place
    int turns = 0;
    //character with the current largest initiative
    CharacterWidget topOfList;
    //Character that is being focused on for the purpose of viewing effects/properties
    CharacterWidget focusedCharacter;

    @FXML
    protected void initialize(){

        //config VBox for characters
        charactersContainer.setSpacing(5.0);

        //get the array of character widgets passed stored in our datashare singleton class
        DataShare dataShare = DataShare.getInstance();
        ArrayList<Object> characterObjects = dataShare.getArray();

        ArrayList<CharacterWidget> characters = new ArrayList<>();
        //convert our characterObjects back into CharacterWidgets, storing them in characters ArrayList
        for(Object characterObj : characterObjects){
            if (characterObj instanceof CharacterWidget){
                characters.add((CharacterWidget)characterObj);
            }
        }
        //sort the characters in descending order based on their initiative
        ArrayList<CharacterWidget> sortedCharacters = CharacterSortTools.charSortDesc(characters);

        //Add characters to their VBox container
        for (CharacterWidget character : sortedCharacters){
            //set on click to populate effects and properties view
            character.setOnMouseClicked(event -> {
                //if a character has been selected in the past, reset its style.
                if(this.focusedCharacter != null){
                    //? Find a way to not append the CSS?
                    this.focusedCharacter.setStyle(character.getStyle() + "; -fx-border-width: 1px; -fx-border-color: Black;");
                }
                //highlight the border of the character and set it as the focused character
                character.setStyle(character.getStyle() + "; -fx-border-width: 3px; -fx-border-color: Blue;");
                this.focusedCharacter = character;

                //populate the views
                populateEffectsView();
                populatePropertiesView();
            });
            character.getChildren().add(new Label("Init: " + String.valueOf(character.getInitiative())));
            charactersContainer.getChildren().add(character);
        }


    }

    public void cycle(ActionEvent event){
        ObservableList<Node> characters = charactersContainer.getChildren();
        Node first = characters.get(0);

        //character with greatest initiative will be the marker for each new round
        topOfList = CharacterSortTools.greatestInitiative(characters);

        characters.remove(0);
        characters.add(first);

        if (characters.indexOf(topOfList) == 0){
            this.rounds += 1;

            String[] temp = roundsLabel.getText().split(" ");

            roundsLabel.setText(temp[0] + " " + String.valueOf(this.rounds));

            addTime();

        }

    }

    public void addTime(){
        this.time += 6;
        
        String[] temp = this.timeLabel.getText().split(" ");

        if (this.time < 60){
            this.timeLabel.setText(temp[0] + " " + String.valueOf(this.time) + " s");
        } else{
            this.timeLabel.setText(temp[0] + " " + String.valueOf(this.time/60) + " m " + String.valueOf(this.time % 60) + " s");
        }
    }

    private void populateEffectsView(){
        ArrayList<Effect> charEffects = this.focusedCharacter.getEffects();

        for (Effect effect : charEffects){
            this.effectsView.getChildren().add(new EffectWidget(effect, this.focusedCharacter));
        }
    }

    private void populatePropertiesView(){
        ;
    }
}
 