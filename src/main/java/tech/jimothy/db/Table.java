package tech.jimothy.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Table is an object that represents an SQL table
 */
public class Table {
    /**An arrayList of HashMaps is how the table data is structured and stored */
    private ArrayList<HashMap<String, Object>> table = new ArrayList<>();

    /**
     * Constructor method of table 
     */
    public Table(ResultSet rs){
        try { 
            ResultSetMetaData rSetMeta = rs.getMetaData();
            int columnCount = rSetMeta.getColumnCount();
            
            while(rs.next()){
                HashMap<String, Object> row = new HashMap<>();
                for (int i = 0; columnCount > i; i++){
                    String columnName = rSetMeta.getColumnName(i+1);
                    row.put(columnName, rs.getObject(i+1));
                }
                this.table.add(row);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> row(int index){
        return this.table.get(index);
    }

    public String get(int row, String column){
        return table.get(row).get(column).toString();
    }

    public int getSize(){
        return this.table.size();
    }
}
