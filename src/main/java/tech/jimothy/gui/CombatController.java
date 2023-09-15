package tech.jimothy.gui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import tech.jimothy.db.DataShare;
import tech.jimothy.design.Effect;
import tech.jimothy.design.EffectItem;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.errors.WidgetMissingChildException;
import tech.jimothy.gui.custom.CharacterWidget;
import tech.jimothy.gui.custom.EffectWidget;
import tech.jimothy.gui.custom.OptionCharacterWidget;
import tech.jimothy.gui.custom.SearchAndSelectWidget;
import tech.jimothy.gui.nav.Nav;
import tech.jimothy.utils.CharacterSortTools;

import java.io.IOException;
import java.util.ArrayList;

public class CombatController {

    @FXML Parent root;
    @FXML VBox charactersContainer;
    @FXML Label roundsLabel;
    @FXML Label timeLabel;
    @FXML private VBox effectsView;
    //main view of the page
    @FXML VBox mainView;
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
    //Indicates whether ot not the AddEffectsWidget currently exists
    boolean addEffectsWidgetExists = false;

        /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();
    

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

        //Add characters to their VBox container, configuring them as needed
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

            //create new 'add effect' menu option for character widget
            MenuItem addEffectsOption = new MenuItem("Add Effect");
            //set the action for the new menu option
            addEffectsOption.setOnAction(event -> {manifestAddEffectsWidget(character);});
            ((OptionCharacterWidget)character).getOptionsMenu().getItems().add(addEffectsOption);

            character.getChildren().add(new Label("Init: " + String.valueOf(character.getInitiative())));
            charactersContainer.getChildren().add(character);
        }
        //Set the character that is at the top of turn order
        this.topOfList = CharacterSortTools.greatestInitiative(this.charactersContainer.getChildren());

        //Set this first character as the character being focused on and then populate the views for that character
        this.focusedCharacter = this.topOfList;
        this.focusedCharacter.setStyle(this.focusedCharacter.getStyle() + "; -fx-border-width: 3px; -fx-border-color: Blue;");
        populateEffectsView();
    }

    private void manifestAddEffectsWidget(CharacterWidget assocChar){

        if(!this.addEffectsWidgetExists){
            this.addEffectsWidgetExists = true;

            SearchAndSelectWidget addEffectsWidget = new SearchAndSelectWidget(new EffectItem());
            try{
                addEffectsWidget.setOnButtonPress(event -> {
                    for(Object effectObj : addEffectsWidget.getSelections()){
                         if(effectObj instanceof Effect){
                            Effect effect = (Effect)effectObj;
                            
                            //add an effect widget to the effects view IF this char is currently the one focused on
                            //AND the associated character widget does not currently have an instance of the effect already(compare effect id)
                            if(assocChar.equals(this.focusedCharacter) && !assocChar.hasEffect(effect.getID())){
                                this.effectsView.getChildren().add(new EffectWidget(effect, assocChar));
                                //add effect to the character which is associated with this addEffectsWidget
                                assocChar.addEffect(effect);
                            }
                         }
                    }
                    this.mainView.getChildren().remove(addEffectsWidget);
                    this.addEffectsWidgetExists = false;
                });
            } catch (WidgetMissingChildException e){
                e.printStackTrace();
            }
            this.mainView.getChildren().add(addEffectsWidget);
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
        //remove previous population
        this.effectsView.getChildren().removeAll(this.effectsView.getChildren());

        ArrayList<Effect> charEffects = this.focusedCharacter.getEffects();

        for (Effect effect : charEffects){
            System.out.println(effect.getName());
            EffectWidget effectWidget = new EffectWidget(effect, this.focusedCharacter);
            //add padding to effect widget to create space between widgets
            effectWidget.setPadding(new Insets(0, 0, 10, 0));
            this.effectsView.getChildren().add(effectWidget);

        }
    }

    private void populatePropertiesView(){
        ;
    }


    public void endCombat() throws IOException, StageNotSetForNav{
        navigation.goToSelectedCampaignPage(DataShare.getInstance().getInt());
    }
}
 