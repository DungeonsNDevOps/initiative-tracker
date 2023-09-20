package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.gui.nav.Nav;
import tech.jimothy.utils.Integers;

public class CreateMonsterController {
    @FXML TextField monsterNameField;
    @FXML TextField monsterBonusField;
    @FXML Label nameWarningLabel;
    @FXML Label bonusWarningLabel;
    /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();


    public void createMonster(ActionEvent event) throws IOException, SQLException, StageNotSetForNav{
        Database database = new Database(DatabaseConfig.URL);
        String monsterName = monsterNameField.getText();
        String monsterBonus = monsterBonusField.getText();

        if (inputValid()){
            database.insert("INSERT INTO entities(name, bonus, type) VALUES(?, ?, ?)", new String[] {monsterName, monsterBonus, "monster"});
            navigation.goToLastPage();
        }
        database.close();
    }
    

    public void goBack(){
        navigation.goToLastPage();
     }


     private boolean inputValid(){
        boolean inputValid = true;
        if (monsterNameField.getText().equals(null) || monsterNameField.getText().equals("")){
            inputValid = false;
            this.nameWarningLabel.setText("Name is required!");
        }
        if (monsterBonusField.getText().equals(null) || monsterBonusField.getText().equals("")){
            inputValid = false;
            this.bonusWarningLabel.setText("Bonus is required!");
        }
        if (!Integers.isInteger(this.monsterBonusField.getText())){
            inputValid = false;
            this.bonusWarningLabel.setText("Bonus must be an integer!");
        }

        return inputValid;
    }
}
