package tech.jimothy.gui.custom;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import tech.jimothy.utils.Entity;
import tech.jimothy.utils.EntityList;

/**
 * CharAddWidget is a custom JavaFX component that 
 */
public class CharAddWidget extends VBox{

    TextField searchTextField = new TextField();
    ListView<Entity> addCharacterListView = new ListView<>();
    Button addCharacterButton = new Button("Add");
    
    public CharAddWidget(int spacing){
        super(spacing);

        //Configuration
        this.setPrefHeight(250);
        this.setPrefWidth(150);
        this.searchTextField.setPromptText("Search");
        this.addCharacterButton.setPrefWidth(this.getPrefWidth());
        this.addCharacterListView.getSelectionModel()
                                 .setSelectionMode(SelectionMode.MULTIPLE);

        //Add nodes to the instance of this object
        this.getChildren().add(searchTextField);
        this.getChildren().add(addCharacterListView);
        this.getChildren().add(addCharacterButton);

        populateCharacters();
        setButtonFunction();
    }

    private void populateCharacters(){
        Database database = new Database("./sqlite/inibase");
        Table charactersTable = database.query("SELECT * FROM characters");
        EntityList characterList = new EntityList(charactersTable);
        
        for(int i = 0; i < charactersTable.getSize(); i++){
            addCharacterListView.getItems()
                                .add(characterList.getEntity(i));
        }
    }

    public ObservableList<Entity> getSelections(){
        ObservableList<Entity> selections = this.addCharacterListView
                                                .getSelectionModel()
                                                .getSelectedItems();
        return selections;
    }

    /*
     * Current problem: I need to come up with a way to store meta-data for the characters we list and 
     * select from the ListView component. Using the index of the ListView item will not work since
     * we want to implement a search function eventually. 
     */
    private void setButtonFunction(){
        Database database = new Database("./sqlite/inibase");
        int currentCampaignID = DataShare.getInstance()
                                         .getInt();
        String currentCampaignName = database.query("SELECT * FROM campaigns")
                                             .get(currentCampaignID, "name");

        this.addCharacterButton.setOnAction(event -> {

            for(Entity character : getSelections()){
                System.out.println(character.getID());
                try {
                    System.out.println("Updating database...");
                    database.modify("UPDATE characters SET " + 
                                    currentCampaignName + " = 1 WHERE id = " + 
                                    character.getID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
