package tech.jimothy.gui.custom;

import javafx.scene.layout.VBox;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import tech.jimothy.utils.Entity;

import java.sql.SQLException;


import javafx.scene.control.Button;

public class KillableCharacterWidget extends CharacterWidget {

    private Button killButton;
    
    public KillableCharacterWidget( 
                           int spacing, 
                           String name, 
                           int bonus, 
                           int ID){
        super(spacing, name, bonus, ID);
        //Create vbox for buttons
        VBox buttonBox = new VBox(10);

        //Add buttons
        this.killButton = new Button("Kill");
        this.killButton.setOnAction(event -> {kill();});
        buttonBox.getChildren().add(killButton);

        this.getChildren().add(buttonBox);
    }

    public KillableCharacterWidget(
                           Entity soul, 
                           int spacing){
                            
        super(soul, spacing); 
        //Create vbox for buttons
        VBox buttonBox = new VBox(10);

        //Add buttons
        this.killButton = new Button("Kill");
        this.killButton.setOnAction(event -> {kill();});
        buttonBox.getChildren().add(killButton);

        this.getChildren().add(buttonBox);
    }

    //Method for 'killing' the character
    private void kill(){
        Database database = new Database("./sqlite/inibase");
        Table campaignTable = database.query("SELECT * FROM campaigns");
        DataShare campaignData = DataShare.getInstance();
        int campaignID = campaignData.getInt();
        String campaignName = campaignTable.get(campaignID-1, "name");
        try {
            database.modify("UPDATE characters SET " + 
                            campaignName + " = 0 WHERE id = " 
                            + this.getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.getContainer().getChildren().remove(this);
    }
}
