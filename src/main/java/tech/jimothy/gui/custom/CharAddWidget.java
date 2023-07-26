package tech.jimothy.gui.custom;

import java.sql.SQLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
    ObservableList<Entity> characterList;
    
    public CharAddWidget(int spacing){
        super(spacing);

        //Configuration
        this.setPrefHeight(250);
        this.setPrefWidth(150);
        this.searchTextField.setPromptText("Search");
        this.addCharacterButton.setPrefWidth(this.getPrefWidth());
        this.addCharacterListView.getSelectionModel()
                                 .setSelectionMode(SelectionMode.MULTIPLE);
        //register an event listener with searchTextField's textProperty
        this.searchTextField.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue){
                search(newValue);
            }
        });

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
        this.characterList = addCharacterListView.getItems();
    }

    public ObservableList<Entity> getSelections(){
        ObservableList<Entity> selections = this.addCharacterListView
                                                .getSelectionModel()
                                                .getSelectedItems();
        return selections;
    }

    private void setButtonFunction(){
        Database database = new Database("./sqlite/inibase");
        int currentCampaignID = DataShare.getInstance()
                                         .getInt();
        String currentCampaignName = database.query("SELECT * FROM campaigns")
                                             .get(currentCampaignID-1, "name");

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

    private void search(String searchValue){
        System.out.println(searchValue);
        ObservableList<Entity> newList = FXCollections.observableArrayList();
        addCharacterListView.setItems(this.characterList);
        //if searchValue does NOT equal an empty string, then we compare items and repopulate
        //Otherwise, we just keep the items set to the original items
        if(!searchValue.equals("")){
            for(Entity entity : addCharacterListView.getItems()){
                boolean match = false; // set match as false until otherwise proven
                for (int i = 0; i < searchValue.length(); i++){
                    match = (Character.toLowerCase(entity.toString()
                                                        .charAt(i)) == Character.toLowerCase(searchValue.charAt(i)));
                    if (!match){
                        break;
                    }
                }

                if (match && !newList.contains(entity)){
                    newList.add(entity);
                }
            }
            addCharacterListView.setItems(newList);
        }
        
    }
}
