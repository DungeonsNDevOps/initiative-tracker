package tech.jimothy.db;

/**
 * DataShare is an object that utilizes the Singleton pattern. It is intended to allow 
 * JavaFX controllers to share data. It allows for the storage of three data types.  
 */
public class DataShare {
    
    private String str;
    private Double dbl;
    private int num;

    private final static DataShare INSTANCE = new DataShare();

    private DataShare(){}

    public static DataShare getInstance(){
        return INSTANCE;
    }

    public String getString(){
        return this.str;
    }

    public Double getDouble(){
        return this.dbl;
    }

    public int getInt(){
        return this.num;
    }

    public void setDouble(Double dbl){
        this.dbl = dbl;
    }

    public void setString(String str){
        this.str = str;
    }

    public void setInt(int num){
        this.num = num;
    }

}
