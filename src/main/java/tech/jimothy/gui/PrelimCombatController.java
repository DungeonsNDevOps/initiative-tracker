package tech.jimothy.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import tech.jimothy.gui.custom.CharacterWidget;
import tech.jimothy.gui.custom.KillableCharacterWidget;
import tech.jimothy.gui.custom.SpotlightPane;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public class PrelimCombatController {

    @FXML Pane root;
    @FXML Button finishButton;
    @FXML Button backButton;

    SpotlightPane spotlightPane;

    @FXML
    protected void initialize(){
        spotlightPane = new SpotlightPane(30.0, 0.2);
        spotlightPane.setMaxWidth(root.getPrefWidth());
        spotlightPane.setMaxHeight(root.getPrefHeight());
        spotlightPane.setMinWidth(root.getPrefWidth());
        spotlightPane.setMinHeight(root.getPrefHeight());


        Database database = new Database("./sqlite/inibase");
        DataShare dataShare = DataShare.getInstance();
        int campaignID = dataShare.getInt();
        Table campaignsTable = database.query("SELECT * FROM campaigns");
        String campaignName = campaignsTable.get(campaignID - 1, "name");
        Table charactersTable = database.query("SELECT * FROM characters WHERE " +
                                                campaignName + " = 1");
        
        for (int i = 0; i < charactersTable.getSize(); i++){
            CharacterWidget character = new CharacterWidget(   0,
                                                                charactersTable.get(i, "name"),
                                                                charactersTable.get(i, "bonus"),
                                                                charactersTable.get(i, "id")
            );
            spotlightPane.getChildren().add(character);

            TextField bonusTextField = new TextField();
            Label bonusLabel = new Label('+' + charactersTable.get(i, "bonus"));
            
            character.getChildren().add(bonusTextField);
            character.getChildren().add(bonusLabel);
            
            bonusTextField.setPrefWidth(35.0);

            //* Sets the onkeypressed event handle function to the handle function of the */
            //* onkeypressed event handle function of SpotlightPane */
            //* This allows for SpotlightPane to shift children even when the input focus is currently */
            //* set on the particular textfield */
            //TODO: Review lambda expressions and how they work in the context of javafx events
            bonusTextField.setOnKeyPressed(event -> {
                bonusTextField.getParent().getParent().getOnKeyPressed().handle(event);
                ((CharacterWidget)spotlightPane.getChildren() //get the children in the spotlightpane
                                               .get(spotlightPane.getSpotlightIndex())) //get the node at the spotlight index and cast it to CharacterWidget
                                               .getChildren() //get the children from the children of the characterwidget
                                               .get(2) // get the child at the second index, which is the text field
                                               .requestFocus(); // request that the text field get the input focus
            });
        }

        root.getChildren().add(spotlightPane);

        //*Send the spotlightpane to the back of the children list so that buttons are usable */
        spotlightPane.toBack();
    }

    public void finish(ActionEvent event) throws IOException{
        //instantiate an arraylist to hold our CharacterWidgets
        ArrayList<Object> characterArray = new ArrayList<>();
        ObservableList<Node> spotlightChildren = this.spotlightPane.getChildren();
        DataShare dataShare = DataShare.getInstance();

        for(Node child : spotlightChildren){
            if (child instanceof CharacterWidget){
                CharacterWidget charWidget = (CharacterWidget)child;
                String initiative = ((TextField)charWidget.getChildren()
                                                          .get(2))
                                                          .getText();
                KillableCharacterWidget killableChar = new KillableCharacterWidget(
                                                                                    0,
                                                                                    charWidget.getName(),
                                                                                    charWidget.getBonus(),
                                                                                    charWidget.getID());
                //set the initiative for our killableChar
                killableChar.setInitiative(Integer.valueOf(initiative));
                characterArray.add(killableChar);
            }
        }
        //pass our newly made array to our datashare singleton class for use later
        dataShare.setArray(characterArray);

        Stage stage = (Stage)this.root.getScene().getWindow();
        this.root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/combat-page.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event){
        ;
    }
}
