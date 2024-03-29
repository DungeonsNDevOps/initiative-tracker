package tech.jimothy.gui;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.db.Table;
import tech.jimothy.gui.nav.Nav;

public class Window extends Application{

    @Override
    public void start(Stage stage) throws IOException, SQLException{
        //DATABASE TEST -------------------------------------------
        Database database = new Database(DatabaseConfig.URL);
        Table table = database.query("SELECT name FROM sqlite_schema WHERE type = 'table' AND name NOT LIKE 'sqlite_%';");
        for(int i = 0; i < table.getSize(); i++){
            System.out.println(table.get(i, "name"));
        }
        //---------------------------------------------------------
        //Set up the Nav class by statically setting the stage
        Nav.setStage(stage);
        
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
