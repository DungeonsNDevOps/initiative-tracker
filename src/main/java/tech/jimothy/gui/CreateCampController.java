package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.design.CharacterItemType;
import tech.jimothy.design.Entity;
import tech.jimothy.gui.custom.SearchAndSelectWidget;
import tech.jimothy.gui.nav.Nav;

public class CreateCampController {

    /**Injectable TextField for the campaign name */
    @FXML private TextField nameTextField;
    /**Injectable warning label */
    @FXML private Label warningLabel;
    //root of scene
    @FXML private AnchorPane root;
    //Warning label for name text field
    @FXML private Label nameWarningLabel;
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
        this.searchAndSelect = new SearchAndSelectWidget(new CharacterItemType());
        this.searchAndSelect.setHasButton(false);
        this.searchAndSelect.setLayoutX(252);
        this.searchAndSelect.setLayoutY(271);
        this.searchAndSelect.setPrefSize(200, 145);
        root.getChildren().add(this.searchAndSelect);
    }

    //TODO: Rows are still being added despite SQL duplicate error. Implement a better way to validate non-dupe campaigns.
    public void createCampaign(ActionEvent event) throws SQLException, IOException{
        String campaignName = nameTextField.getText();
        ArrayList<Entity> charSelections = new ArrayList<>();
        for (Object object : this.searchAndSelect.getSelections()){
            if (object instanceof Entity){
                charSelections.add((Entity)object);
            }
        }
        Database database = new Database(DatabaseConfig.URL); 
        try{

            if(inputValid()){
                database.insert("INSERT INTO campaigns(name) VALUES(?)", new String[] {campaignName});
                //add a column to the entities table for the specified campaign for indication of association
                //then, initialize with zeros
                database.modify("ALTER TABLE entities ADD " + campaignName + " INTEGER;" + 
                                "UPDATE entities SET " + campaignName + " = 0");
                //set value as 1 for characters that have been selected
                for(Entity entity : charSelections){
                    // System.out.println(entity.getID());
                    database.modify("UPDATE entities SET " + campaignName + " = 1" + 
                                    " WHERE id = " + entity.getID());
                }

                navigation.goToLastPage();
            }
            
            database.close();

        } catch(SQLException e){
            warningLabel.setVisible(true);
        }
     }

     public void goBack(){
        navigation.goToLastPage();
     }

     private boolean inputValid(){
        boolean inputValid = true;

        if(nameTextField.getText().equals(null) || nameTextField.getText().equals("")){
            inputValid = false;
            this.nameWarningLabel.setText("Name is required!");
        }
        return inputValid;
     }
}
