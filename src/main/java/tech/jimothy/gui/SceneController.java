package tech.jimothy.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import javafx.scene.Node;

public class SceneController {
     
    private Stage stage;
    private Scene scene;
    @FXML private Parent root;
    @FXML VBox campaignsVBox;

    public void goToCampaignPage(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/campaign-page.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void goToCreateCampaign(ActionEvent event) throws IOException{
        stage = (Stage)root.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/create-campaign-page.fxml"));
        
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToCreateCharacter(ActionEvent event) throws IOException{
        stage = (Stage)root.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/create-character-page.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToSelectedCampaignPage(int campaignID) throws IOException{
        DataShare dataShare = DataShare.getInstance();
        dataShare.setInt(campaignID);
        stage = (Stage)root.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/selected-campaign-page.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void initialize(){
        Database database = new Database("./sqlite/inibase");
        Table campaignsTable = database.query("SELECT * FROM campaigns");
        
        campaignsVBox.setSpacing(50.0);
        
        for(int i = 0; i < campaignsTable.getSize(); i++){
            Button campaignButton = new Button(campaignsTable.get(i, "name"));
            int campaignID = i+1;
            campaignButton.setOnAction(actionEvent -> {             
                try {
                    goToSelectedCampaignPage(campaignID);
                } catch (IOException e) {
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
