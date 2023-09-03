package tech.jimothy.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tech.jimothy.db.Database;

public class CreateEffectController {
    @FXML private TextField nameTextField;
    @FXML private TextArea descTextArea;
    @FXML private TextField durationTextField;

    @FXML
    protected void initialize(){
        ;
    }

    public void createEffect(ActionEvent event){
        Database database = new Database("./sqlite/inibase");
        

    }
}