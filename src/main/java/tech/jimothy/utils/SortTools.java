package tech.jimothy.utils;

import java.util.ArrayList;

import tech.jimothy.gui.custom.CharacterWidget;

public class SortTools {
    
    /**
     * Method sorts the characters based on their initiative in descending order. It uses the 
     * Insertion Sort algorithm implementation in order to acheive this. 
     * @param array The arraylist of CharacterWidget's
     * @return Returns a sorted ArrayList of CharacterWidget's
     */
    public static ArrayList<CharacterWidget> charSortDesc(ArrayList<CharacterWidget> array){
        for(int i = 1; i < array.size(); i++){
            //store the current element that will be compared 
            CharacterWidget current = array.get(i);
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
}
