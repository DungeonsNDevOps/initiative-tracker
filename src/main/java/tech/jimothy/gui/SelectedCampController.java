package tech.jimothy.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.db.Table;
import tech.jimothy.design.CharacterItemType;
import tech.jimothy.design.MonsterItemType;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.gui.custom.SearchAndSelectWidget;
import tech.jimothy.gui.nav.Nav;
import tech.jimothy.gui.custom.OptionEntityWidget;

public class SelectedCampController {
    

    private Stage stage;
    private Scene scene;
    @FXML private Parent root;
    @FXML VBox entityVBox;
    @FXML AnchorPane anchorPane;

    private VBox addCharacterWidget;
    private VBox addMonsterWidget;
    private boolean addCharacterWidgetExists = false;
    private boolean addMonsterWidgetExists = false;

//*----------------------------------------Navigation--------------------------------------------- */
    /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();

    public void goToCampaignPage(ActionEvent event) throws IOException, StageNotSetForNav{
        navigation.goToCampaignPage();
    }

    public void goToCreateCampaign(ActionEvent event) throws IOException, StageNotSetForNav{
        navigation.goToCreateCampaign();
        navigation.setLastPage(() -> {
            try {
                navigation.goToSelectedCampaignPage(DataShare.getInstance().getInt());
            } catch (IOException | StageNotSetForNav e) {
                e.printStackTrace();
            }
        });
    }

    public void goToCreateCharacter(ActionEvent event) throws IOException, StageNotSetForNav{
        navigation.goToCreateCharacter();
        navigation.setLastPage(() -> {
            try {
                navigation.goToSelectedCampaignPage(DataShare.getInstance().getInt());
            } catch (IOException | StageNotSetForNav e) {
                e.printStackTrace();
            }
        });
    }

    public void goToPreliminaryCombatPage() throws IOException{
        stage = (Stage)root.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/preliminary-combat-page.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToCreateEffect() throws IOException, StageNotSetForNav{
        navigation.goToCreateEffect();
        navigation.setLastPage(() -> {
            try {
                navigation.goToSelectedCampaignPage(DataShare.getInstance().getInt());
            } catch (IOException | StageNotSetForNav e) {
                e.printStackTrace();
            }
        });
    }

    public void goToCreateMonster(ActionEvent event) throws IOException, StageNotSetForNav{
        navigation.goToCreateMonster();
        navigation.setLastPage(() -> {
            try {
                navigation.goToSelectedCampaignPage(DataShare.getInstance().getInt());
            } catch (IOException | StageNotSetForNav e) {
                e.printStackTrace();
            }
        });
    }

    public void goBack(){
        try {
            navigation.goToCampaignPage();
        } catch (IOException | StageNotSetForNav e) {
            e.printStackTrace();
        }
    }

//*--------------------------------------------------------------------------------------------- */

    //TODO: Figure out how to make positioning of widget dynamic instead of hard coded
    public void manifestAddCharacterWidget(ActionEvent event){
        if(!addCharacterWidgetExists){
            SearchAndSelectWidget addCharacterWidget = new SearchAndSelectWidget(this.entityVBox, new CharacterItemType());
            this.addCharacterWidget = addCharacterWidget; 

            AnchorPane.setLeftAnchor(addCharacterWidget, 30.0);
            AnchorPane.setBottomAnchor(addCharacterWidget, 65.0);

            anchorPane.getChildren().add(addCharacterWidget);

            this.addCharacterWidgetExists = true;           
        } else{
            anchorPane.getChildren().remove(this.addCharacterWidget);
            
            addCharacterWidgetExists = false;
        }
    }
    //TODO: Figure out how to make positioning of widget dynamic instead of hard coded
    public void manifestAddMonsterWidget(ActionEvent event){
        if(!addMonsterWidgetExists){
            SearchAndSelectWidget addMonsterWidget = new SearchAndSelectWidget(this.entityVBox, new MonsterItemType());
            this.addMonsterWidget = addMonsterWidget; 

            AnchorPane.setLeftAnchor(addMonsterWidget, 245.0);
            AnchorPane.setBottomAnchor(addMonsterWidget, 65.0);

            anchorPane.getChildren().add(addMonsterWidget);

            this.addMonsterWidgetExists = true;           
        } else{
            anchorPane.getChildren().remove(this.addMonsterWidget);
            
            this.addMonsterWidgetExists = false;
        }
    }

    @FXML
    /**
     * Method is invoked when the scene graph is loaded by FXMLloader
     */
    protected void initialize(){
        Database database = new Database(DatabaseConfig.URL);
        DataShare dataShare = DataShare.getInstance();
        int campaignID = dataShare.getInt();
        //System.out.println(campaignID);
        Table campaignsTable = database.query("SELECT * FROM campaigns");
        String campaignName = campaignsTable.get(campaignID - 1, "name");
        Table entitiesTable = database.query("SELECT * FROM entities WHERE " +
                                                campaignName + " >= 1");
        
        for (int i = 0; i < entitiesTable.getSize(); i++){
            String entityName = entitiesTable.get(i, "name");
            int entityBonus = Integer.valueOf(entitiesTable.get(i, "bonus"));
            int entityID = Integer.valueOf(entitiesTable.get(i, "id"));
            String entityType = entitiesTable.get(i, "type");

            //if entity type is monster, add the quantity indicated in the db
            if (entityType.equals("monster")){
                //get the quantity of the particular monster stored
                int storedMonsterQuantity = Integer.valueOf(database.query("SELECT " + campaignName + 
                " FROM entities WHERE id = " + entityID).get(0, campaignName));

                //add a particular monster for the quantity specified in the db
                for (int ii = 0; ii < storedMonsterQuantity; ii++){

                    OptionEntityWidget entity = new OptionEntityWidget(20, 
                                                            entityName, 
                                                            entityBonus,
                                                            entityID,
                                                            entityType
                                                            );
                    entity.setPrefWidth(100);
                    this.entityVBox.getChildren().add(entity);
                }
            } else { //else just add one of the particular entity since it isn't a monster
                OptionEntityWidget entity = new OptionEntityWidget(20, 
                                                                entityName, 
                                                                entityBonus,
                                                                entityID,
                                                                entityType
                                                                );
                entity.setPrefWidth(100);
                this.entityVBox.getChildren().add(entity);
            }

        }
        database.close();
    }
}
