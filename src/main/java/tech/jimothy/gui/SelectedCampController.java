package tech.jimothy.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import tech.jimothy.gui.custom.CharAddWidget;

public class SelectedCampController {
    

    private Stage stage;
    private Scene scene;
    @FXML private Parent root;
    @FXML VBox charactersVBox;
    @FXML AnchorPane anchorPane;

    private VBox addCharacterWidget;
    private boolean addCharacterWidgetExists = false;

    public void goToCampaignPage(ActionEvent event) throws IOException{
        stage = (Stage)root.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/campaign-page.fxml"));
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

    public void addCharacter(ActionEvent event){
        if(!addCharacterWidgetExists){
            CharAddWidget addCharacterWidget = new CharAddWidget(0);
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
