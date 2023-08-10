package tech.jimothy.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import tech.jimothy.gui.custom.SpotlightPane;
import tech.jimothy.gui.custom.CharacterWidget;

public class PrelimCombatController {

    @FXML Pane root;

    @FXML
    protected void initialize(){
        SpotlightPane spotlightTest = new SpotlightPane(30.0, 0.2);
        spotlightTest.requestFocus();
        spotlightTest.setMaxWidth(root.getPrefWidth());
        spotlightTest.setMaxHeight(root.getPrefHeight());
        spotlightTest.setMinWidth(root.getPrefWidth());
        spotlightTest.setMinHeight(root.getPrefHeight());
        for(int i = 0; i < 10; i++){
            spotlightTest.getChildren().add(new CharacterWidget(0, 20, "Jimmy", "10", "1"));
        }
        root.getChildren().add(spotlightTest);
    }
}
