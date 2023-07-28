package tech.jimothy.gui.custom;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CharacterWidget extends HBox {
    
    public CharacterWidget(int spacing, int width, String name, String bonus){
        super(spacing);

        HBox characterBox = new HBox(20.0);
        characterBox.setStyle("-fx-border-style: solid;" + 
                                "-fx-border-width: 1px;");
        characterBox.setMaxWidth(width);
        Label characterNameLabel = new Label(name);

        characterBox.getChildren().add(characterNameLabel);
    }
}
