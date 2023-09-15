package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import tech.jimothy.db.Database;
import tech.jimothy.design.CharacterItem;
import tech.jimothy.design.Entity;
import tech.jimothy.gui.custom.SearchAndSelectWidget;
import tech.jimothy.gui.nav.Nav;

public class CreateCampController {

    /**Injectable TextField for the campaign name */
    @FXML private TextField campaignTextField;
    /**Injectable warning label */
    @FXML private Label warningLabel;
    //root of scene
    @FXML private AnchorPane root;
    //*SearchAndSelect */
    SearchAndSelectWidget searchAndSelect;
    /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();

    /**
     * Method runs when the FXML for this controller is built with FXMLLoader
     */
    @FXML
    protected void initialize(){

        // Testing SearchAndSelect widget ------------------
        this.searchAndSelect = new SearchAndSelectWidget(new CharacterItem());
        this.searchAndSelect.setHasButton(false);
        this.searchAndSelect.setLayoutX(252);
        this.searchAndSelect.setLayoutY(271);
        this.searchAndSelect.setPrefSize(200, 145);
        root.getChildren().add(this.searchAndSelect);
    }

    //TODO: Rows are still being added despite SQL duplicate error. Implement a better way to validate non-dupe campaigns.
    public void createCampaign(ActionEvent event) throws SQLException, IOException{
        String campaignName = campaignTextField.getText();
        ObservableList<Entity> charSelections = FXCollections.observableArrayList();
        for (Object object : this.searchAndSelect.getSelections()){
            if (object instanceof Entity){
                charSelections.add((Entity)object);
            }
        }
        Database database = new Database("./sqlite/inibase"); 
        try{
            database.insert("INSERT INTO campaigns(name) VALUES(?)", new String[] {campaignName});
            database.modify("ALTER TABLE characters ADD " + campaignName + " INTEGER");
            for(Entity character : charSelections){
                // System.out.println(character.getID());
                database.modify("UPDATE characters SET " + campaignName + " = 1" + 
                                " WHERE id = " + character.getID());
            }
            database.close();
            navigation.goToLastPage();
        } catch(SQLException e){
            warningLabel.setVisible(true);
        }
     }

     public void goBack(){
        navigation.goToLastPage();
     }
}
