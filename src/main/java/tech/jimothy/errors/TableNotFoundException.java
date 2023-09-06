package tech.jimothy.errors;



public class TableNotFoundException extends Exception {
    public TableNotFoundException(){
        super("Table was not found");
    }
}
