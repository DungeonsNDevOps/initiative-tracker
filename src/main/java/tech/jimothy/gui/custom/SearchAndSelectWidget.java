package tech.jimothy.gui.custom;

import java.sql.SQLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.design.Entity;
import tech.jimothy.design.ItemType;
import tech.jimothy.errors.TableNotFoundException;
import tech.jimothy.errors.WidgetMissingChildException;

//TODO: Consider refactoring code to no longer use 'Entity' class as character representation in ViewLists
/**
 * Widget is a reusable javafx component for searching items in a specified database table and selecting those items.
 * @author Timothy Newton
 * 
 */
public class SearchAndSelectWidget extends VBox{

    TextField searchTextField = new TextField();
    ListView<Object> itemListView = new ListView<>();
    Button button = new Button("Add");
    ObservableList<Object> itemList;

    /**The type of item that will be populated in the ViewList of this widget */
    ItemType itemType;
    
    /**Pane that is tied to this widget -- often times to be accessed by a method within' this class */
    Pane associatedPane;
    /**
     * Constructor builds out the inner components of the widget
     * @param associatedPane The pane associated with this widget -- allows for inner methods to easily affect an outside pane
     * @param table The database table that will be populated in the inner ListView
     * @throws TableNotFoundException
     */
    public SearchAndSelectWidget(Pane associatedPane, ItemType itemType){
        //call the super constructor method
        super();

        this.associatedPane = associatedPane;
        this.itemType = itemType;

        //Configuration
        this.setPrefHeight(250);
        this.setPrefWidth(150);
        this.searchTextField.setPromptText("Search");
        this.button.setPrefWidth(this.getPrefWidth());
        this.setAlignment(Pos.CENTER);
        this.itemListView.getSelectionModel()
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
        this.getChildren().add(itemListView);
        this.getChildren().add(button);

        populateItems(itemType);
        this.button.setOnAction(event -> {setButtonFunction();});
    }

    public SearchAndSelectWidget(ItemType itemType) {
        //call the super constructor method
        super();

        this.associatedPane = null;
        this.itemType = itemType;

        //Configuration
        this.setPrefHeight(250);
        this.setPrefWidth(150);
        this.searchTextField.setPromptText("Search");
        this.button.setPrefWidth(this.getPrefWidth());
        this.setAlignment(Pos.CENTER);
        this.itemListView.getSelectionModel()
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
        this.getChildren().add(itemListView);
        this.getChildren().add(button);

        populateItems(itemType);
        this.button.setOnAction(event -> {setButtonFunction();});
    }

    public void setHasButton(boolean hasButton){
        if(hasButton){
            if(this.button == null){
                this.button = new Button("Add");
                this.getChildren().add(button);
            }
        } else{
            this.getChildren().remove(button);
            this.button = null;
        }
    }
    /**
     * Method populates this widget's ListView component with items depending on the specified
     * item type. More specifically, the type will determine which table from the database to pull from
     * and what kind of object to place into the ViewList
     * @param itemType The item type 
     */
    private void populateItems(ItemType itemType){
        itemType.populateItems(this.itemListView);
        this.itemList = this.itemListView.getItems();
    }

    /**
     * Gets the objects stored in the ViewList
     * @return returns an ObservableList of objects 
     */
    public ObservableList<Object> getSelections(){
        ObservableList<Object> selections = this.itemListView
                                                .getSelectionModel()
                                                .getSelectedItems();
        return selections;
    }
    
    /**
     * Sets the default functionality of the button.
     */
    private void setButtonFunction(){
        Database database = new Database("./sqlite/inibase");
        int currentCampaignID = DataShare.getInstance()
                                         .getInt();
        String currentCampaignName = database.query("SELECT name FROM campaigns")
                                             .get(currentCampaignID-1, "name");


        for(Object character : getSelections()){
            boolean alreadyAdded = false;
            //System.out.println(character.getID());
            try {
                //System.out.println("Updating database...");
                if(character instanceof Entity){
                    database.modify("UPDATE characters SET " + 
                                    currentCampaignName + " = 1 WHERE id = " + 
                                    ((Entity)character).getID());
                    for(Node characterWidget : this.associatedPane.getChildren()){
                        if (((CharacterWidget)characterWidget).getID() == ((Entity)character).getID()){
                            alreadyAdded = true;
                        }
                    }                    
                }


                if(!alreadyAdded && character instanceof Entity){
                    
                    //add new characterwidget to charactersVBox
                    this.associatedPane.getChildren().add(
                        new OptionCharacterWidget(
                                                   (Entity)character,
                                                    10));
                } else{
                    ; //? Add warning message animation thing here for when user adds a dupe character maybe?
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Internal method alters the items within the ListView based on the searchValue param. It will
     * filter out any items that do not initially match with the searchValue param. 
     * Example: 'San' matches with 'Sanji'
     * @param searchValue The value being searched for
     */
    private void search(String searchValue){
        System.out.println(searchValue);
        ObservableList<Object> newList = FXCollections.observableArrayList();
        itemListView.setItems(this.itemList);
        //if searchValue does NOT equal an empty string, then we compare items and repopulate
        //Otherwise, we just keep the items set to the original items
        if(!searchValue.equals("")){
            for(Object item : itemListView.getItems()){
                boolean match = false; // set match as false until otherwise proven
                for (int i = 0; i < searchValue.length(); i++){
                    match = (Character.toLowerCase(item.toString()
                                                        .charAt(i)) == Character.toLowerCase(searchValue.charAt(i)));
                    if (!match){
                        break;
                    }
                }

                if (match && !newList.contains(item)){
                    newList.add(item);
                }
            }
            itemListView.setItems(newList);
        }
        
    }

    //Set new button functionality for this widget's button if it has one
    public void setOnButtonPress(EventHandler<ActionEvent> func) throws WidgetMissingChildException{
        if (this.button != null){
            this.button.setOnAction(func);
        } else{
            throw new WidgetMissingChildException(this.button);
        }
    }
}
