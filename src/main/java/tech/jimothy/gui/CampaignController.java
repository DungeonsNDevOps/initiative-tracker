package tech.jimothy.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.db.Table;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.gui.nav.Nav;

public class CampaignController {
     
    @FXML private Parent root;
    @FXML VBox campaignsVBox;

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
                navigation.goToCampaignPage();
            } catch (IOException | StageNotSetForNav e) {
                e.printStackTrace();
            }
        });
    }

    public void goToCreateCharacter(ActionEvent event) throws IOException, StageNotSetForNav{
        navigation.goToCreateCharacter();
        navigation.setLastPage(() -> {
            try {
                navigation.goToCampaignPage();
            } catch (IOException | StageNotSetForNav e) {
                e.printStackTrace();
            }
        });
    }

    public void goToCreateEffect(ActionEvent event) throws IOException, StageNotSetForNav{
        navigation.goToCreateEffect();
        navigation.setLastPage(() -> {
            try {
                navigation.goToCampaignPage();
            } catch (IOException | StageNotSetForNav e) {
                e.printStackTrace();
            }
        });
    }

    public void goToCreateMonster(ActionEvent event) throws IOException, StageNotSetForNav{
        navigation.goToCreateMonster();
        navigation.setLastPage(() -> {
            try {
                navigation.goToCampaignPage();
            } catch (IOException | StageNotSetForNav e) {
                e.printStackTrace();
            }
        });
    }

//*--------------------------------------------------------------------------------------------- */


    @FXML
    protected void initialize(){
        Database database = new Database(DatabaseConfig.URL);
        Table campaignsTable = database.query("SELECT * FROM campaigns");
        
        campaignsVBox.setSpacing(50.0);
        
        for(int i = 0; i < campaignsTable.getSize(); i++){
            Button campaignButton = new Button(campaignsTable.get(i, "name"));
            int campaignID = i+1;
            campaignButton.setOnAction(actionEvent -> {             
                try {
                    navigation.goToSelectedCampaignPage(campaignID);
                } catch (IOException | StageNotSetForNav e) {
                    e.printStackTrace();
                }
            });
            campaignButton.setMinWidth(campaignsVBox.getPrefWidth()-(campaignsVBox.getPrefWidth()*.2));
            campaignButton.setMinHeight(60);
            campaignsVBox.getChildren().add(campaignButton);
        }
        database.close();
    }
}
