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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import tech.jimothy.utils.Integers;

/**
 * Database object is an inuitive interface for the JDBC driver library. 
 * It makes accessing and modifying sqlite databases straightforward and easy.
 * @author Timothy Newton
 */
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
     * @param url The string path to the database 
     */
    public Database(String url){
        //set the instance variable for the db path
        this.url = url;
        String OS = System.getProperty("os.name").toLowerCase();

        System.out.println("Initializing Database... If one doesn't exist, One will be created.");
        System.out.println("Database: OS is " + OS);
        //check the OS
        //prepend JDBC specific protocol syntax to the db path
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
            //set the instance variable for the connection 
            this.conn = DriverManager.getConnection(url);
            if (conn!= null){
                //get the meta data about the connection object
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());  
                System.out.println("Connection to database successful!"); 
                //pass the path to database string to execSchema method call
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

    /**
     * Gets the current database connection
     * @return returns the database connection object
     */
    public Connection getConn(){
        return this.conn;
    }

    /**
     * Method checks a given directory path to see if the immediate
     * parent directory exists.
     * @param url the dir path to be checked
     */
    private boolean parentDirectoryExists(String url){
        String[] urlist = url.split("/");
        this.parentDirectory = "";

        for (int i = 0; i < urlist.length - 1; i++){
            this.parentDirectory += urlist[i] + "/"; 
        }

        Path path = Paths.get(this.parentDirectory);
        return (Files.exists(path));
    }

    /**
     * Method executes the given sql schema file. It does this by using a 
     * Scanner object to scan each line of the given sql file.
     * @param schema The given File object to be execute
     * @throws FileNotFoundException Throws if the file is not found
     */
    private void execSchema(File schema) throws FileNotFoundException{
        Scanner schemaScan = new Scanner(schema);
        File dbFile = new File(this.url);
        String sql = "";

        while (schemaScan.hasNext()){
            sql += schemaScan.nextLine() + "\n";
        }

        String [] sqlStatements = sql.split(";");

        try{
            if(dbFile.length() == 0){
                for(String stmt : sqlStatements){
                    Statement statement = this.conn.createStatement();
                    statement.execute(stmt);
                }
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
            if(Integers.isInteger(args[i])){
                int num = Integer.parseInt(args[i]);
                pstmt.setInt(i + 1, num);
            } else{
                pstmt.setString(i + 1, args[i]);
            }
        }
        pstmt.executeUpdate();
    }

    /**
     * Method executes a query sql statement and returns 
     * a Table object.
     * @param statement The sql query statement to be executed
     * @return Returns a Table object
     */
    public Table query(String statement){
        
        try { 
            Statement stmt= this.conn.createStatement();
            ResultSet rSet = stmt.executeQuery(statement);
            return new Table(rSet);

        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void close(){
        try {
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method executes a modifying type sql Statement
     * @param sql The given sql statement
     * @throws SQLException Throws if there is an SQL error
     */
    public void modify(String sql) throws SQLException{
        Statement stmt = this.conn.createStatement();

        stmt.execute(sql);
        
    }

    /**
     * To be determined
     * @param sql
     * @param args
     */
    public void set(String sql, String[] args){
        ;
    }

}
