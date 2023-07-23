package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;

public class CreateCampController {

    /**Injectable variable for the character selection MenuButton */
    @FXML private ListView<String> characterListView;
    /**Injectable TextField for the campaign name */
    @FXML private TextField campaignTextField;

    /**
     * Method runs when the FXML for this controller is built with FXMLLoader
     */
    @FXML
    protected void initialize(){
        Database database = new Database("./sqlite/inibase");
        Table charactersTable = database.query("SELECT* FROM characters");
        characterListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        for (int i = 0; i < charactersTable.getSize(); i++){
            HashMap<String, Object> character = charactersTable.row(i);
            characterListView.getItems().add(character.get("name").toString());
        }
        database.close();
    }

    public void createCampaign(ActionEvent event) throws SQLException, IOException{
        String campaignName = campaignTextField.getText();
        ObservableList<Integer> characterSelections = characterListView
                                                    .getSelectionModel()
                                                    .getSelectedIndices();
        Database database = new Database("./sqlite/inibase"); 
        database.insert("INSERT INTO campaigns(name) VALUES(?)", new String[] {campaignName});
        database.modify("ALTER TABLE characters ADD " + campaignName + " INTEGER");
        for(int characterIndex : characterSelections){
            System.out.println(characterIndex);
            database.modify("UPDATE characters SET " + campaignName + " = 1" + 
                            " WHERE id = " + (characterIndex+1));
        }
        new SceneController().goToCampaignPage(event);
        database.close();
    }
}
