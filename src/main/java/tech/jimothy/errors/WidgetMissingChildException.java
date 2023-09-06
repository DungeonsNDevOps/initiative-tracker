package tech.jimothy.errors;

import javafx.scene.Node;

public class WidgetMissingChildException extends Exception{
    public WidgetMissingChildException(Node member){
        super("Widget is does not have child: " + member.toString());
    }
}
