package tech.jimothy.utils;

public class Integers {

    public static boolean isInteger(String s){

        for (int i = 0; i < s.length(); i++){
            if (i == 0 && s.charAt(i) == '-'){
                if (s.length() == 1){
                    //String just has '-'
                    return false;
                } else if(i == 0) {
                    continue;           
                } 
            }
            if (!Character.isDigit(s.charAt(i))){
                //Char at index i does not represent a digit
                return false;
            }
        }
        return true;
    }
}
