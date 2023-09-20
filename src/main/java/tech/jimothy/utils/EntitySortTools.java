package tech.jimothy.utils;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import tech.jimothy.gui.custom.EntityWidget;

public class EntitySortTools {
    
    /**
     * Method sorts the entitys based on their initiative in descending order. It uses the 
     * Insertion Sort algorithm implementation in order to acheive this. 
     * @param array The arraylist of entityWidget's
     * @return Returns a sorted ArrayList of entityWidget's
     */
    public static ArrayList<EntityWidget> charSortDesc(ArrayList<EntityWidget> array){
        for(int i = 1; i < array.size(); i++){
            //store the current element that will be compared 
            EntityWidget current = array.get(i);
            //index j will be what we move backwards to compare previous elements to the current
            //it should start one behind the current
            int j = i - 1;

            //while j doesn't isn't going out of bounds and the element at j is less than our current
            while(j > -1 && array.get(j).getInitiative() < current.getInitiative()){

                //move the element from index j up one
                array.set(j + 1, array.get(j));
                //move index j back one to compare the previous element with the current
                j -= 1;
            }
            //once j reaches 1 before the beginning or element at j is not less than our current,
            //we set our current to one above where j currently is
            array.set(j+1, current);
        }
        return array;
    }

    public static EntityWidget greatestInitiative(ObservableList<Node> observableList){
        EntityWidget greatestInitiative = new EntityWidget(); 

        if(observableList.get(0) instanceof EntityWidget){
            greatestInitiative = (EntityWidget)observableList.get(0);
            for(int i = 1; i < observableList.size(); i++){
                EntityWidget entity = (EntityWidget)observableList.get(i);
                if (entity.getInitiative() > greatestInitiative.getInitiative()){
                    greatestInitiative = entity;
                }
            }  
        }

        return greatestInitiative;
    }

    public static EntityWidget greatestInitiative(ArrayList<EntityWidget> entities){
        EntityWidget greatestInitiative = entities.get(0);
        for(int i = 1; i < entities.size(); i++){
            EntityWidget entity = (EntityWidget)entities.get(i);
            if (entity.getInitiative() > greatestInitiative.getInitiative()){
                greatestInitiative = entity;
            }
        }  

        return greatestInitiative;
    }
}
