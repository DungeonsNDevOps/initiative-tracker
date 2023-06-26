package tech.jimothy.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tech.jimothy.db.Database;

public class Window extends Application{

    @Override
    public void start(Stage stage) throws IOException{
        Database database = new Database("./sqlite/inibase");
        Parent root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/campaign-page.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Initiative Tracker");
        stage.show();

    }
    public static void createWindow(String[] args){
        launch(args);
    }
}
