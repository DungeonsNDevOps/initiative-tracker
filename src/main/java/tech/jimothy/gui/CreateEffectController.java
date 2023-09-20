package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.gui.nav.Nav;
import tech.jimothy.utils.Integers;

public class CreateEffectController {
    @FXML private TextField nameTextField;
    @FXML private TextArea descTextArea;
    @FXML private TextField durationTextField;

    @FXML private Label nameWarningLabel;
    @FXML private Label descWarningLabel;
    @FXML private Label durationWarningLabel;
    /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();

    @FXML
    protected void initialize(){
        ;
    }

    public void createEffect(ActionEvent event) throws IOException, StageNotSetForNav{
        Database database = new Database(DatabaseConfig.URL);
        //Get the text from the appropriate text field
        String effectName = nameTextField.getText();
        String desc = descTextArea.getText();
        String duration = durationTextField.getText();


        if(inputValid()){
            //add a new row in the effects table for the effect
            try {
            database.insert("INSERT INTO effects(name, desc, duration) VALUES(?, ?, ?)",
                            new String[] {effectName, desc, duration});
            } catch (SQLException e) {
                e.printStackTrace();
            }

            navigation.goToLastPage();
        }
        

        database.close();
        
    }

    public void goBack(){
        navigation.goToLastPage();
     }

    private boolean inputValid(){
        boolean inputValid = true;
        if (nameTextField.getText().equals(null) || nameTextField.getText().equals("")){
            inputValid = false;
            this.nameWarningLabel.setText("Name is required!");
        }
        if (descTextArea.getText().equals(null) || descTextArea.getText().equals("")){
            inputValid = false;
            this.descWarningLabel.setText("Description is required!");
        }
        if (durationTextField.getText().equals(null) || durationTextField.getText().equals("")){
            inputValid = false;
            this.durationWarningLabel.setText("Duration is required!");
        }
        if(!Integers.isInteger(durationTextField.getText())){
            inputValid = false;
            this.durationWarningLabel.setText("Duration must be an integer!");
        }
        return inputValid;
    }
}