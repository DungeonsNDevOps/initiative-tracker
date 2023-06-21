package app.tech.jimothy.initracker;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application{
    @Override
    public void start(Stage stage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("resources/scene-graphs/campaign-page.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Initiative Tracker");
        stage.show();
    }
    public static void createWindow(String[] args){
        launch(args);
    }
}