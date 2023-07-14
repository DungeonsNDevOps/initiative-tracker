package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tech.jimothy.db.Database;
import tech.jimothy.utils.Integers;

public class CreateCharController {
    @FXML TextField characterNameField;
    @FXML TextField characterBonusField;
    @FXML Label bonusWarningMessage;


    public void createCharacter(ActionEvent event) throws IOException, SQLException{
        Database database = new Database("./sqlite/inibase");
        String characterName = characterNameField.getText();
        String characterBonus = characterBonusField.getText();

        if (Integers.isInteger(characterBonus)){
            database.insert("INSERT INTO characters(name, bonus) VALUES(?, ?)", new String[] {characterName, characterBonus});
            new SceneController().goToCampaignPage(event);
        } else{
            bonusWarningMessage.setText("Bonus must be an integer!");
        }
        database.close();
    }


}
