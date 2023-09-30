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
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.db.Table;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.gui.custom.EntityWidget;
import tech.jimothy.gui.custom.OptionEntityWidget;
import tech.jimothy.gui.custom.SpotlightPane;
import tech.jimothy.gui.nav.Nav;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public class PrelimCombatController {

    @FXML Pane root;
    @FXML Button finishButton;
    @FXML Button backButton;

    SpotlightPane spotlightPane;

        /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();

    @FXML
    protected void initialize(){
        spotlightPane = new SpotlightPane(30.0, 0.2);
        spotlightPane.setMaxWidth(root.getPrefWidth());
        spotlightPane.setMaxHeight(root.getPrefHeight());
        spotlightPane.setMinWidth(root.getPrefWidth());
        spotlightPane.setMinHeight(root.getPrefHeight());


        Database database = new Database(DatabaseConfig.URL);
        DataShare dataShare = DataShare.getInstance();
        int campaignID = dataShare.getInt();
        Table campaignsTable = database.query("SELECT * FROM campaigns");
        String campaignName = campaignsTable.get(campaignID - 1, "name");
        Table entitiesTable = database.query("SELECT * FROM entities WHERE " +
                                                campaignName + " >= 1");
        
        for (int i = 0; i < entitiesTable.getSize(); i++){


            String entityName = entitiesTable.get(i, "name");
            String entityType = entitiesTable.get(i, "type");
            int entityID = Integer.valueOf(entitiesTable.get(i, "id"));
            int entityBonus = Integer.valueOf(entitiesTable.get(i, "bonus"));
            //get the quantity of the particular monster stored
            int monsterQuantity = Integer.valueOf(database.query("SELECT " + campaignName + 
            " FROM entities WHERE id = " + entityID).get(0, campaignName));
            
            //If the entity is a monster, we should add the amount of entity specified in db
            if (entityType.equals("monster")){
                for (int ii = 0; ii < monsterQuantity; ii++){
                    EntityWidget entity = new EntityWidget(   0,
                                                                entityName,
                                                                entityBonus,
                                                                entityID,
                                                                entityType
                    );

                    spotlightPane.getChildren().add(entity);

                    TextField initTextField = new TextField();
                    Label bonusLabel = new Label('+' + entitiesTable.get(i, "bonus"));
                    
                    entity.getChildren().add(initTextField);
                    entity.getChildren().add(bonusLabel);
                    
                    initTextField.setPrefWidth(35.0);

                    //* Sets the onkeypressed event handle function to the handle function of the */
                    //* onkeypressed event handle function of SpotlightPane */
                    //* This allows for SpotlightPane to shift children even when the input focus is currently */
                    //* set on the particular textfield */
                    //TODO: Review lambda expressions and how they work in the context of javafx events
                    initTextField.setOnKeyPressed(event -> {
                        initTextField.getParent().getParent().getOnKeyPressed().handle(event);
                        ((EntityWidget)spotlightPane.getChildren() //get the children in the spotlightpane
                                                    .get(spotlightPane.getSpotlightIndex())) //get the node at the spotlight index and cast it to CharacterWidget
                                                    .getChildren() //get the children from the entitywidget
                                                    .get(2) // get the child at the second index, which is the text field
                                                    .requestFocus(); // request that the text field get the input focus
                    });
                }
            } else { //else just add one of the entity
                EntityWidget entity = new EntityWidget(   0,
                                                            entityName,
                                                            entityBonus,
                                                            entityID,
                                                            entityType
                );

                spotlightPane.getChildren().add(entity);

                TextField initTextField = new TextField();
                Label bonusLabel = new Label('+' + entitiesTable.get(i, "bonus"));
                
                entity.getChildren().add(initTextField);
                entity.getChildren().add(bonusLabel);
                
                initTextField.setPrefWidth(35.0);

                //* Sets the onkeypressed event handle function to the handle function of the */
                //* onkeypressed event handle function of SpotlightPane */
                //* This allows for SpotlightPane to shift children even when the input focus is currently */
                //* set on the particular textfield */
                //TODO: Review lambda expressions and how they work in the context of javafx events
                initTextField.setOnKeyPressed(event -> {
                    initTextField.getParent().getParent().getOnKeyPressed().handle(event);
                    ((EntityWidget)spotlightPane.getChildren() //get the children in the spotlightpane
                                                .get(spotlightPane.getSpotlightIndex())) //get the node at the spotlight index and cast it to CharacterWidget
                                                .getChildren() //get the children from the entitywidget
                                                .get(2) // get the child at the second index, which is the text field
                                                .requestFocus(); // request that the text field get the input focus
                });
            }
        }

        root.getChildren().add(spotlightPane);

        //*Send the spotlightpane to the back of the children list so that buttons are usable */
        spotlightPane.toBack();
    }

    public void finish(ActionEvent event) throws IOException{

        if(inputValid()){
            //instantiate an arraylist to hold our CharacterWidgets
            ArrayList<Object> entityArray = new ArrayList<>();

            ObservableList<Node> spotlightChildren = this.spotlightPane.getChildren();
            DataShare dataShare = DataShare.getInstance();

            for(Node child : spotlightChildren){
                if (child instanceof EntityWidget){
                    EntityWidget entityWidget = (EntityWidget)child;
                    String initiative = ((TextField)entityWidget.getChildren()
                                                            .get(2))
                                                            .getText();
                    OptionEntityWidget optionEntityWidget = new OptionEntityWidget(0,
                                                            entityWidget.getName(),
                                                            entityWidget.getBonus(),
                                                            entityWidget.getID(),
                                                            entityWidget.getType());
                    //set the initiative for our killableChar
                    optionEntityWidget.setInitiative(Integer.valueOf(initiative));
                    entityArray.add(optionEntityWidget);
                }
            }
            //pass our newly made array to our datashare singleton class for use later
            dataShare.setArray(entityArray);

            Stage stage = (Stage)this.root.getScene().getWindow();
            this.root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/combat-page.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void goBack(ActionEvent event){
        try {
            navigation.goToSelectedCampaignPage(DataShare.getInstance().getInt());;
        } catch (IOException | StageNotSetForNav e) {
            e.printStackTrace();
        }
    }

    private boolean inputValid(){
        ObservableList<Node> spotlightChildren = this.spotlightPane.getChildren();
        boolean inputValid = true;
        
        for(Node child : spotlightChildren){
            TextField initTextField = ((TextField)((EntityWidget)child).getChildren().get(2));
            if(initTextField.getText().equals(null) || initTextField.getText().equals("")){
                inputValid = false;
                //TODO: Highlight EntityWidgets that are invalid
            }
        }

        return inputValid;
    }

    /**
     * Autorolls initiatives for the entities of the specified types
     */
    public void autoRoll(){
        ObservableList<Node> spotlightChildren = this.spotlightPane.getChildren();
        String [] types = {"monster", "npc"};

        for(Node child : spotlightChildren){
            TextField initTextField = ((TextField)((EntityWidget)child).getChildren().get(2));

            for (String type : types){
                if (((EntityWidget)child).getType().equals(type)){
                    initTextField.setText(String.valueOf(rollD20()));
                }
            }

        }
        ((EntityWidget)spotlightPane.getSpotlightChild()).getChildren().get(2).requestFocus();
    }
    
    private int rollD20(){
        return (int)(Math.random() * 20) + 1;
    }
}
