package tech.jimothy.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import tech.jimothy.gui.custom.CharacterWidget;
import tech.jimothy.gui.custom.SpotlightPane;

public class PrelimCombatController {

    @FXML Pane root;
    @FXML Button finishButton;
    @FXML Button backButton;

    @FXML
    protected void initialize(){
        SpotlightPane spotlightTest = new SpotlightPane(30.0, 0.2);
        spotlightTest.setMaxWidth(root.getPrefWidth());
        spotlightTest.setMaxHeight(root.getPrefHeight());
        spotlightTest.setMinWidth(root.getPrefWidth());
        spotlightTest.setMinHeight(root.getPrefHeight());


        Database database = new Database("./sqlite/inibase");
        DataShare dataShare = DataShare.getInstance();
        int campaignID = dataShare.getInt();
        Table campaignsTable = database.query("SELECT * FROM campaigns");
        String campaignName = campaignsTable.get(campaignID - 1, "name");
        Table charactersTable = database.query("SELECT * FROM characters WHERE " +
                                                campaignName + " = 1");
        
        for (int i = 0; i < charactersTable.getSize(); i++){
            CharacterWidget character = new CharacterWidget(   0,
                                                                20,
                                                                charactersTable.get(i, "name"),
                                                                charactersTable.get(i, "bonus"),
                                                                charactersTable.get(i, "id")
            );
            spotlightTest.getChildren().add(character);

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
                ((CharacterWidget)spotlightTest.getChildren() //get the children in the spotlightpane
                                               .get(spotlightTest.getSpotlightIndex())) //get the node at the spotlight index and cast it to CharacterWidget
                                               .getChildren() //get the children from the children of the characterwidget
                                               .get(2) // get the child at the second index, which is the text field
                                               .requestFocus(); // request that the text field get the input focus
            });
        }

        root.getChildren().add(spotlightTest);

        //*Send the spotlightpane to the back of the children list so that buttons are usable */
        spotlightTest.toBack();
    }

    public void finish(ActionEvent event){
        ;
    }

    public void back(ActionEvent event){
        ;
    }
}
