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
import tech.jimothy.gui.custom.KillableCharacterWidget;
import tech.jimothy.utils.SortTools;

import java.util.ArrayList;

public class CombatController {

    @FXML Parent root;
    @FXML VBox charactersContainer;
    @FXML Label roundsLabel;
    @FXML Label timeLabel;

    //the amount of rounds that have taken place
    int rounds = 0;
    //time in seconds that has taken place in combat
    int time = 0;
    //the amount of turns that have taken place
    int turns = 0;
    //the first character in turn order
    Node firstCharacter;

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
        ArrayList<CharacterWidget> sortedCharacters = SortTools.charSortDesc(characters);

        //Add characters to their VBox container
        for (CharacterWidget character : sortedCharacters){
            character.getChildren().add(new Label("Init: " + String.valueOf(character.getInitiative())));
            charactersContainer.getChildren().add(character);
        }

        this.firstCharacter = charactersContainer.getChildren().get(0);
    }

    public void cycle(ActionEvent event){
        ObservableList<Node> characters = charactersContainer.getChildren();
        Node first = characters.get(0);

        characters.remove(0);
        characters.add(first);

        if (characters.indexOf(this.firstCharacter) == 0){
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
}
 