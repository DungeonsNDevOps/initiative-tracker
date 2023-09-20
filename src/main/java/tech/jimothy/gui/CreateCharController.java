package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.gui.nav.Nav;
import tech.jimothy.utils.Integers;

public class CreateCharController {
    @FXML TextField characterNameField;
    @FXML TextField characterBonusField;
    @FXML Label nameWarningLabel;
    @FXML Label bonusWarningLabel;
    @FXML Label typeWarningLabel;
    @FXML ChoiceBox<String> typeChoiceBox;
    /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();

    @FXML
    protected void initialize(){
        typeChoiceBox.getItems().addAll("npc", "player");
    }

    public void createCharacter(ActionEvent event) throws IOException, SQLException, StageNotSetForNav{
        Database database = new Database(DatabaseConfig.URL);
        String characterName = characterNameField.getText();
        String characterBonus = characterBonusField.getText();
        String type = this.typeChoiceBox.getSelectionModel().getSelectedItem();

        if (inputValid()){
            database.insert("INSERT INTO entities(name, bonus, type) VALUES(?, ?, ?)", new String[] {characterName, characterBonus, type});
            navigation.goToLastPage();
        } 

        database.close();
        
    }

    public void goBack(){
        navigation.goToLastPage();
     }

    private boolean inputValid(){
        boolean inputValid = true;
        if (characterNameField.getText().equals(null) || characterNameField.getText().equals("")){
            inputValid = false;
            this.nameWarningLabel.setText("Name is required!");
        }
        if (characterBonusField.getText().equals(null) || characterBonusField.getText().equals("")){
            inputValid = false;
            this.bonusWarningLabel.setText("Bonus is required!");
        }
        if (!Integers.isInteger(this.characterBonusField.getText())){
            inputValid = false;
            this.bonusWarningLabel.setText("Bonus must be an integer!");
        }
        if (this.typeChoiceBox.getSelectionModel().isEmpty()){
            inputValid = false;
            this.typeWarningLabel.setVisible(true);
        }
        return inputValid;
    }
}
