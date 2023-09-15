package tech.jimothy.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import tech.jimothy.design.CharacterItem;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.gui.custom.SearchAndSelectWidget;
import tech.jimothy.gui.nav.Nav;
import tech.jimothy.gui.custom.OptionCharacterWidget;

public class SelectedCampController {
    

    private Stage stage;
    private Scene scene;
    @FXML private Parent root;
    @FXML VBox charactersVBox;
    @FXML AnchorPane anchorPane;

    private VBox addCharacterWidget;
    private boolean addCharacterWidgetExists = false;
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

    public void goBack(){
        try {
            navigation.goToCampaignPage();
        } catch (IOException | StageNotSetForNav e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addCharacter(ActionEvent event){
        if(!addCharacterWidgetExists){
            SearchAndSelectWidget addCharacterWidget = new SearchAndSelectWidget(this.charactersVBox, new CharacterItem());
            this.addCharacterWidget = addCharacterWidget; 

            AnchorPane.setLeftAnchor(addCharacterWidget, 20.0);
            AnchorPane.setBottomAnchor(addCharacterWidget, 65.0);

            anchorPane.getChildren().add(addCharacterWidget);

            this.addCharacterWidgetExists = true;           
        } else{
            anchorPane.getChildren().remove(this.addCharacterWidget);
            
            addCharacterWidgetExists = false;
        }
    }

    @FXML
    /**
     * Method is invoked when the scene graph is loaded by FXMLloader
     */
    protected void initialize(){
        Database database = new Database("./sqlite/inibase");
        DataShare dataShare = DataShare.getInstance();
        int campaignID = dataShare.getInt();
        //System.out.println(campaignID);
        Table campaignsTable = database.query("SELECT * FROM campaigns");
        String campaignName = campaignsTable.get(campaignID - 1, "name");
        Table charactersTable = database.query("SELECT * FROM characters WHERE " +
                                                campaignName + " = 1");
        
        for (int i = 0; i < charactersTable.getSize(); i++){
            String characterName = charactersTable.get(i, "name");
            int characterBonus = Integer.valueOf(charactersTable.get(i, "bonus"));
            int characterID = Integer.valueOf(charactersTable.get(i, "id"));
            OptionCharacterWidget character = new OptionCharacterWidget(20, 
                                                            characterName, characterBonus,
                                                            characterID
                                                            );
            character.setPrefWidth(100);
            this.charactersVBox.getChildren().add(character);
        }
        database.close();
    }
}
