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

    /**The child that's in the spotlight */
    private Node spotlightChild;
    //The index of the child currntly within the spotlight. 
    private int spotlightIndex = 0;
    //this X coordinate of the static spotlight position
    private double focusPosX;
    //the Y coordinate of the static spotlight position
    private double focusPosY;




    
    public SpotlightPane(double gap, double widthFactor){
        super();
        this.gap = gap;
        this.widthFactor = widthFactor;
        this.setStyle("-fx-border-style: dotted; -fx-border-width: 2px;");
        //ensure that when the  user clicks on this node, input focus
        //is set for this node. This is important for future
        //input event handling within this node.
        this.setOnMousePressed(event -> {
            this.requestFocus();
        });

        //
        this.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DOWN){
                this.shiftChildrenDown();
            } else if (event.getCode() == KeyCode.UP){
                this.shiftChildrenUp();
            }
        });

    }

    @Override
    protected void layoutChildren(){
        super.layoutChildren();

        ObservableList<Node> children = this.getChildren();

        double childWidth = this.getMinWidth()*this.widthFactor;

        this.focusPosX = (this.getMinWidth()/2.0) - (childWidth/2);
        double offsetY = this.focusPosY = this.getMinHeight()/2.0;

        int childIndex = 0;
        for (Node child : children){
            if(child instanceof Region){
                if(childIndex == 0){
                    this.spotlightChild = child;
                }
                ((Region)child).setMinWidth(childWidth);
                child.relocate(this.focusPosX, offsetY);
                offsetY += gap;                
            }
            childIndex += 1; 
        }
    }

    private void shiftChildrenDown(){
        ObservableList<Node> children = this.getChildren();

        //Check to make sure we won't go out of bounds
        if (spotlightIndex + 1 < children.size()){
            spotlightIndex += 1;
            this.spotlightChild = children.get(spotlightIndex);

            for(int i = 0; i < children.size(); i++){
                Node child = children.get(i);
                if (i < spotlightIndex){

                    //The distance between the current child per iteration and the spotlight position
                    double distance = 0;
                    //iterate from the current child to the spotlight index, counting the distance
                    //this is to get the current distance of the current child away from the 
                    //spotlight position
                    for(int ii = i; ii < this.spotlightIndex; ii++){
                        /*check to make sure child is an instance of region
                         * so we can cast it to region and get it's height for
                         * calculating distance.
                         */
                        if (children.get(ii) instanceof Region){
                           distance += ((Region)children.get(ii)).getHeight() + this.gap;
                        }
                        
                    }
                    if (child instanceof Region){
                        ((Region)child).relocate(this.focusPosX, this.focusPosY-distance);
                        System.out.println("/");
                    }

                }else if (i == spotlightIndex){
                    if(child instanceof Region){
                        ((Region)child).relocate(this.focusPosX, this.focusPosY);
                        System.out.println("*");
                    }
                } else{
                    //The distance between the current child per iteration and the spotlight position
                    double distance = 0;
                    /*Iterate from the child at the spotlightIndex through to the 
                     * current child per iteration, counting the distance.
                     */
                    for(int ii = spotlightIndex; ii < i; ii++){
                        if(children.get(ii) instanceof Region){
                            distance += ((Region)child).getHeight() + this.gap;

                        }
                    }

                    if(child instanceof Region){
                        ((Region)child).relocate(this.focusPosX, this.focusPosY + distance);
                        System.out.println("/");
                    }
                }

            }
            
        }
    }

    private void shiftChildrenUp(){
        ObservableList<Node> children = this.getChildren();

        //Check to make sure we won't go out of bounds
        if (spotlightIndex > 0){
            spotlightIndex -= 1;
            this.spotlightChild = children.get(spotlightIndex); 
        }
    }
}

