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
    /** The index of the child currntly within the spotlight. This index is in relation
     * to this spotlightpane's list of children. 
    */
    private int spotlightIndex = 0;
    /** this X coordinate of the static spotlight position */
    private double spotlightPosX;
    /** the Y coordinate of the static spotlight position */
    private double spotlightPosY;
    /* defines the amount of times layoutChildren() has been executed */
    private int layoutChildrenCount = 0;




    
    public SpotlightPane(double gap, double widthFactor){
        super();
        this.gap = gap;
        this.widthFactor = widthFactor;
        //ensure that when the  user clicks on this node, input focus
        //is set for this node. This is important for future
        //input event handling within this node.
        this.setOnMousePressed(event -> {
            this.requestFocus();
        });

        //
        this.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DOWN){
                this.shiftChildrenUp();
            } else if (event.getCode() == KeyCode.UP){
                this.shiftChildrenDown();
            }
        });
    }

    @Override
    protected void layoutChildren(){
        super.layoutChildren();
        

        ObservableList<Node> children = this.getChildren();

        double childWidth = this.getMinWidth()*this.widthFactor;

        this.spotlightPosX = (this.getWidth()/2.0) - (childWidth/2);
        this.spotlightPosY = this.getHeight()/2.0;

        //The index of child in the proceeding for loop
        int childIndex = 0;
        //*check to ensure that this is the first time we're running layoutChildren()
        //*since we don't want to run this bit of code any subsequent times.
        if(layoutChildrenCount < 1){
            for (Node child : children){
                if (child instanceof Region){
                    ((Region)child).setMinWidth(childWidth);
                }
                if (childIndex == this.spotlightIndex){
                    child.relocate(this.spotlightPosX, this.spotlightPosY);
                    child.setScaleX(2);
                    child.setScaleY(2);
                } else{

                    double distance = 0;
                    for (int i = 0; i < childIndex; i++){
                        if (children.get(i) instanceof Region){
                            distance += ((Region)children.get(i)).getHeight() + this.gap;
                        }
                    }

                    child.relocate(this.spotlightPosX, this.spotlightPosY + distance);
                }
                childIndex += 1;
            }
            layoutChildrenCount += 1;

            this.spotlightChild = children.get(0);
        }
    }
    /**
     * Method shifts children up, placing a the next child in line in the spotlight position
     */
    private void shiftChildrenUp(){
        ObservableList<Node> children = this.getChildren();

        //*Check to make sure we won't go out of bounds
        if (spotlightIndex + 1 < children.size()){

            /*
             * Changes the index of the child that should be in the spotlight positon.
             * This is what determines whether children will be shifted up or down. 
             * In this case, since we are adding, children will be shifted up. 
             */
            spotlightIndex += 1;
            this.spotlightChild = children.get(spotlightIndex);

            /*
             * Iterate through each child in this spotlightpane's list of children, 
             * relocating them as needed
             */
            for(int i = 0; i < children.size(); i++){
                //The current child of the iteration
                Node child = children.get(i);

                /*
                 * If index of the current child is below the spotlight index 
                 * (spatially above the child in the spotlight position), we want to find the 
                 * calculate how far away this child should be from the spotlight position and use 
                 * that distance to position the current child
                 */
                if (i < spotlightIndex){

                    //The distance between the current child per iteration and the spotlight position
                    double distance = 0;
                    //iterate from the current child to the spotlight index, counting each gap and
                    //and height of each child - this is to get the current distance of the current child away from the 
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
                    /*
                     * Now that we have a distance away from the spotlight positon for this 
                     * particular child, we can translate this child on the Y axis for the 
                     * calculated distance. We use the Node class's method relocate() to 
                     * set the new position of the child. We also ensure that the scale factor
                     * is set to 1 (100%)
                     */
                    child.relocate(this.spotlightPosX, this.spotlightPosY-distance);
                    child.setScaleX(1);
                    child.setScaleY(1);

                /*
                 * else, if the index of the current child is the index of the child that 
                 * should be at the spotlight index, then set its position to the spotlight position
                 * and scale the child by a factor of 2 (200%)
                 */
                }else if (i == spotlightIndex){ 

                    child.relocate(this.spotlightPosX, this.spotlightPosY);
                    child.setScaleX(2);
                    child.setScaleY(2);


                /*
                 * Else (if the index of the current child is greater than the index
                 * of the child at the spotlight position), we want to again count the distance
                 * between the current child and the spotlight position, using this said distance
                 * to position the child away from the spotlight position
                 */
                } else{
                    //The distance between the current child per iteration and the spotlight position
                    double distance = 0;
                    /*Iterate from the child at the spotlightIndex through to the 
                     * current child per iteration, counting the distance.
                     */
                    for(int ii = spotlightIndex; ii < i; ii++){
                        if(children.get(ii) instanceof Region){
                            distance += ((Region)children.get(ii)).getHeight() + this.gap;

                        }
                    }

                    child.relocate(this.spotlightPosX, this.spotlightPosY + distance);
                    child.setScaleX(1);
                    child.setScaleY(1);

                }
            }    
        }
    }

    /**
     * Method shifts children down, placing the next child in line in the spotlight position
     */
    private void shiftChildrenDown(){
        ObservableList<Node> children = this.getChildren();

        //*Check to make sure we won't go out of bounds
        if (spotlightIndex > 0){
            spotlightIndex -= 1;
            this.spotlightChild = children.get(spotlightIndex);

            for(int i = 0; i < children.size(); i++){
                Node child = children.get(i);
                if (i < spotlightIndex){

                    //The distance between the current child per iteration and the spotlight position
                    double distance = 0;
                    //iterate from the current child to the spotlight index, counting each gap and
                    //and height of each child - this is to get the current distance of the current child away from the 
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
                    child.relocate(this.spotlightPosX, this.spotlightPosY-distance);
                    child.setScaleX(1);
                    child.setScaleY(1);


                }else if (i == spotlightIndex){

                    child.relocate(this.spotlightPosX, this.spotlightPosY);
                    child.setScaleX(2);
                    child.setScaleY(2);

                } else{
                    //The distance between the current child per iteration and the spotlight position
                    double distance = 0;
                    /*Iterate from the child at the spotlightIndex through to the 
                     * current child per iteration, counting the distance.
                     */
                    for(int ii = spotlightIndex; ii < i; ii++){
                        if(children.get(ii) instanceof Region){
                            distance += ((Region)children.get(ii)).getHeight() + this.gap;

                        }
                    }

                    child.relocate(this.spotlightPosX, this.spotlightPosY + distance);
                    child.setScaleX(1);
                    child.setScaleY(1);

                }
            }    
        }
    }

    public int getSpotlightIndex(){
        return this.spotlightIndex;
    }

    public Node getSpotlightChild(){
        return this.spotlightChild;
    }
}

