package tech.jimothy.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import tech.jimothy.gui.custom.SpotlightPane;
import tech.jimothy.db.DataShare;
import tech.jimothy.db.Database;
import tech.jimothy.db.Table;
import tech.jimothy.gui.custom.CharacterWidget;

public class PrelimCombatController {

    @FXML Pane root;

    @FXML
    protected void initialize(){
        SpotlightPane spotlightTest = new SpotlightPane(30.0, 0.2);
        spotlightTest.setMaxWidth(root.getPrefWidth());
        spotlightTest.setMaxHeight(root.getPrefHeight());
        spotlightTest.setMinWidth(root.getPrefWidth());
        spotlightTest.setMinHeight(root.getPrefHeight());

        Database database = new Database("./sqlite/inibase");
        DataShare dataShare = DataShare.getInstance();
        int campaignID = dataShare.getInt();
        Table campaignsTable = database.query("SELECT * FROM campaigns");
        String campaignName = campaignsTable.get(campaignID - 1, "name");
        Table charactersTable = database.query("SELECT * FROM characters WHERE " +
                                                campaignName + " = 1");
        
        for (int i = 0; i < charactersTable.getSize(); i++){
            spotlightTest.getChildren().add(new CharacterWidget(
                                                                0,
                                                                20,
                                                                charactersTable.get(i, "name"),
                                                                charactersTable.get(i, "bonus"),
                                                                charactersTable.get(i, "id")
            ));
        }
        root.getChildren().add(spotlightTest);
    }
}
