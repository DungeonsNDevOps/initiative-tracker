package tech.jimothy.errors;

public class StageNotSetForNav extends Exception {
    public StageNotSetForNav(){
        super("Stage was never set for Nav class");
    }
}
