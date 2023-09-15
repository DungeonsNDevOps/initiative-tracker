package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tech.jimothy.db.Database;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.gui.nav.Nav;
import tech.jimothy.utils.Integers;

public class CreateCharController {
    @FXML TextField characterNameField;
    @FXML TextField characterBonusField;
    @FXML Label bonusWarningMessage;
    /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();


    public void createCharacter(ActionEvent event) throws IOException, SQLException, StageNotSetForNav{
        Database database = new Database("./sqlite/inibase");
        String characterName = characterNameField.getText();
        String characterBonus = characterBonusField.getText();

        if (Integers.isInteger(characterBonus)){
            database.insert("INSERT INTO characters(name, bonus) VALUES(?, ?)", new String[] {characterName, characterBonus});
            navigation.goToCampaignPage();
        } else{
            bonusWarningMessage.setText("Bonus must be an integer!");
        }
        database.close();
        navigation.goToLastPage();
    }

    public void goBack(){
        navigation.goToLastPage();
     }
}
