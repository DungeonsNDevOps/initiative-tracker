package tech.jimothy.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Database {
    //The directory path provided for the location of the database
    private String url;
    //The parent directory of the database
    private String parentDirectory;
    //Connection to database
    private Connection conn;

    /**
     * Constructor for the database object. Initializes database
     * if one does not exist in the specified directory. 
     * @param url
     */
    public Database(String url){
        this.url = url;
        String OS = System.getProperty("os.name").toLowerCase();

        System.out.println("Initializing Database... If one doesn't exist, One will be created.");
        System.out.println("Database: OS is " + OS);
        //check the OS
        if (OS.equals("windows 10")) {
            url = "jdbc:sqlite:C:" + url;
        } else{
            url = "jdbc:sqlite:" + url;
        }

        //if parent directory specified doesn't exist, create it
        if (!parentDirectoryExists(this.url)){
            System.out.println("Database: Parent directory '" + 
            this.parentDirectory + "' doesn't exist. Creating...");
            new File(this.parentDirectory).mkdirs();
        }

        try {
            this.conn = DriverManager.getConnection(url);
            if (conn!= null){
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());  
                System.out.println("Connection to database successful!"); 
                execSchema(new File(getClass().getResource("/tech/jimothy/sql/schema.sql").getPath())); 
            } else{
                System.out.println("Database connect found to be null!");
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private boolean parentDirectoryExists(String url){
        String[] urlist = url.split("/");
        this.parentDirectory = "";

        for (int i = 0; i < urlist.length - 1; i++){
            this.parentDirectory += urlist[i] + "/"; 
        }

        Path path = Paths.get(this.parentDirectory);
        return (Files.exists(path));
    }

    private void execSchema(File schema) throws FileNotFoundException{
        Scanner schemaScan = new Scanner(schema);
        File dbFile = new File(this.url);
        String sql = "";

        while (schemaScan.hasNext()){
            sql += schemaScan.nextLine() + "\n";
        }
        try{
            if(dbFile.length() == 0){
                Statement statement = this.conn.createStatement();
                statement.execute(sql);
                System.out.println("Database: Successfully executed schema.sql");
            } else{
                System.out.println("Database: Schema.sql has already been executed!");
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }

        schemaScan.close();
    }

    /**
     * Method allows inserting records into the database
     * 
     * @param statement The SQL insert statement
     * @param args The values passed into the statement. Currently, only what can be 
     * interpreted as strings or ints are accepted
     * @throws SQLException
     */
    public void insert(String statement, String[] args) throws SQLException{
        PreparedStatement pstmt = this.conn.prepareStatement(statement);
        for(int i = 0; i < args.length; i++){
            if(isInteger(args[i])){
                int num = Integer.parseInt(args[i]);
                pstmt.setInt(i, num);
            } else{
                pstmt.setString(i, args[i]);
            }
        }
        pstmt.executeUpdate();
    }

    public void query(String statement){
        ;//TO-DO: Write method to execute a query
    }

    private boolean isInteger(String s){
        for (int i = 0; i < s.length(); i++){
            if (i == 0 && s.charAt(i) == '-'){
                if (s.length() == 1){
                    return false;
                }            
            } else {
                continue;
            }
            if (!Character.isDigit(s.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
