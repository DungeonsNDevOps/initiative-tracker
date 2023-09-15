package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML; 
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tech.jimothy.db.Database;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.gui.nav.Nav;

public class CreateEffectController {
    @FXML private TextField nameTextField;
    @FXML private TextArea descTextArea;
    @FXML private TextField durationTextField;
    /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();

    @FXML
    protected void initialize(){
        ;
    }

    public void createEffect(ActionEvent event) throws IOException, StageNotSetForNav{
        Database database = new Database("./sqlite/inibase");
        //Get the text from the appropriate text field
        String effectName = nameTextField.getText();
        String desc = descTextArea.getText();
        String duration = durationTextField.getText();

        //add a new row in the effects table for the effect
        try {
            database.insert("INSERT INTO effects(name, desc, duration) VALUES(?, ?, ?)",
                            new String[] {effectName, desc, duration});
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // //create a column in characters table for the effect
        // try {
        //     //we append the duration to the name to create uniqueness. 
        //     //!We should raise an error message if a user attempts to make a duplicate column
        //     database.modify("ALTER TABLE characters ADD " + effectName + duration + " INTEGER");
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }

        database.close();
        navigation.goToLastPage();
    }

    public void goBack(){
        navigation.goToLastPage();
     }
}