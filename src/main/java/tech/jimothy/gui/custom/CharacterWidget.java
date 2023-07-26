package tech.jimothy.gui.custom;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CharacterWidget extends HBox {
    
    public CharacterWidget(int spacing, String name, String bonus){
        super(spacing);

        String characterName = charactersTable.get(i, "name");

        HBox characterBox = new HBox(20.0);
        characterBox.setStyle("-fx-border-style: solid;" + 
                                "-fx-border-width: 1px;");
        characterBox.setMaxWidth(charactersVBox.getPrefWidth()*.8);
        Label characterNameLabel = new Label(characterName);

        characterBox.getChildren().add(characterNameLabel);
    }
}
