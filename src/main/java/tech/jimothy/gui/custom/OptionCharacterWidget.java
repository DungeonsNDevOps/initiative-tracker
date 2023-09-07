package tech.jimothy.gui.custom;

import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import tech.jimothy.design.Entity;

import java.sql.SQLException;


import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class OptionCharacterWidget extends CharacterWidget {

    private MenuItem killButton;
    /**Options menu button */
    MenuButton optionsMenu;
    
    public OptionCharacterWidget( 
                           int spacing, 
                           String name, 
                           int bonus, 
                           int ID){
        super(spacing, name, bonus, ID);
        //Create vbox for buttons
        this.optionsMenu = new MenuButton("Options");

        //Add buttons
        this.killButton = new MenuItem("Kill");
        this.killButton.setOnAction(event -> {kill();});



        this.optionsMenu.getItems().addAll(killButton);

        this.getChildren().add(this.optionsMenu);
    }

    public OptionCharacterWidget(
                           Entity soul, 
                           int spacing){
                            
        super(soul, spacing); 
       //Create vbox for buttons
        this.optionsMenu = new MenuButton("Options");

        //Add buttons
        this.killButton = new MenuItem("Kill");
        this.killButton.setOnAction(event -> {kill();});

        this.optionsMenu.getItems().addAll(killButton);

        this.getChildren().add(this.optionsMenu);
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

    public MenuButton getOptionsMenu(){
        return this.optionsMenu;
    }
}
