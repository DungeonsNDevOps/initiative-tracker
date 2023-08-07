package tech.jimothy.gui.custom;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * A Javafx pane that lists out it's children, focusing on only one item at a time.
 * The child that has the focus/spotlight is the only child that can be interacted with
 * at a time. The child in spotlight is magnified and located in the center of the pane.
 * The children in the list maintain their order at all times while taking turns in the 
 * 'spotlight.'
 */
public class SpotlightPane extends Pane{

    /**The gap between children */
    private double gap; 
    private double widthFactor;

    /**The index of the child that's in the spotlight */
    private int spotlightIndex;
    
    public SpotlightPane(double gap, double widthFactor){
        super();
        this.gap = gap;
        this.widthFactor = widthFactor;
        this.setStyle("-fx-border-style: dotted; -fx-border-width: 2px;");

        setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DOWN){
                System.out.println("Down was pressed");
            } else{
                System.out.println("You're dumb");
            }
        });

    }

    @Override
    protected void layoutChildren(){
        super.layoutChildren();

        ObservableList<Node> children = this.getChildren();

        double childWidth = this.getMinWidth()*this.widthFactor;

        double initialXPos = (this.getMinWidth()/2.0) - (childWidth/2);
        double initialYPos = this.getMinHeight()/2.0;

        for (Node child : children){
            if(child instanceof Region){
                ((Region)child).setMinWidth(childWidth);
                child.relocate(initialXPos, initialYPos);
                initialYPos += gap;                
            }
        }
    }
}
