package tech.jimothy.gui.custom;

import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.DatabaseConfig;
import tech.jimothy.db.Table;
import tech.jimothy.design.Entity;

import java.sql.SQLException;


import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class OptionEntityWidget extends EntityWidget {

    private MenuItem killButton;
    /**Options menu button */
    MenuButton optionsMenu;
    
    public OptionEntityWidget( 
                           int spacing, 
                           String name, 
                           int bonus, 
                           int ID,
                           String type){
        super(spacing, name, bonus, ID, type);
        //Create vbox for buttons
        this.optionsMenu = new MenuButton("Options");

        //Add buttons
        this.killButton = new MenuItem("Kill");
        this.killButton.setOnAction(event -> {kill();});



        this.optionsMenu.getItems().addAll(killButton);

        this.getChildren().add(this.optionsMenu);
    }

    public OptionEntityWidget(
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

    //Method for 'killing' the entities
    private void kill(){
        Database database = new Database(DatabaseConfig.URL);
        Table campaignTable = database.query("SELECT * FROM campaigns");
        DataShare campaignData = DataShare.getInstance();
        int campaignID = campaignData.getInt();
        String campaignName = campaignTable.get(campaignID-1, "name");

        String entityType = this.getType();

        if (entityType.equals("monster")){
            int storedMonsterQuantity = Integer.valueOf(database.query("SELECT " + campaignName + 
            " FROM entities WHERE id = " + this.getID()).get(0, campaignName));
            //subtract 1 from this monster's quantity
            int newMonsterQuantity = storedMonsterQuantity - 1; 

            //update the db with the new quantity
            try {
                database.modify("UPDATE entities SET " + 
                                campaignName + " = " + 
                                newMonsterQuantity + 
                                " WHERE id = " + this.getID()
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                database.modify("UPDATE entities SET " + 
                                campaignName + " = 0 WHERE id = " 
                                + this.getID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        this.getContainer().getChildren().remove(this);
    }

    public MenuButton getOptionsMenu(){
        return this.optionsMenu;
    }
}
