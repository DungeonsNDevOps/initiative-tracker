package tech.jimothy.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;

public class SelectedCampController {
    

    private Stage stage;
    private Scene scene;
    @FXML private Parent root;
    @FXML VBox charactersVBox;

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

    @FXML
    /**
     * Method is invoked when the scene graph is loaded by FXMLloader
     */
    protected void initialize(){
        Database database = new Database("./sqlite/inibase");
        DataShare dataShare = DataShare.getInstance();
        int campaignID = dataShare.getInt();
        System.out.println(campaignID);
        Table campaignsTable = database.query("SELECT * FROM campaigns");
        String campaignName = campaignsTable.get(campaignID - 1, "name");
        Table charactersTable = database.query("SELECT * FROM characters WHERE " +
                                                campaignName + " = 1");
        
        for (int i = 0; i < charactersTable.getSize(); i++){
            String characterName = charactersTable.get(i, "name");

            HBox characterBox = new HBox(20.0);
            characterBox.setStyle("-fx-border-style: solid;" + 
                                    "-fx-border-width: 1px;");
            characterBox.setMaxWidth(charactersVBox.getPrefWidth()*.8);
            Label characterNameLabel = new Label(characterName);

            characterBox.getChildren().add(characterNameLabel);

            this.charactersVBox.getChildren().add(characterBox);
        }
        database.close();
    }
}
