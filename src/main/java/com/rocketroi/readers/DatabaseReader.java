package com.rocketroi.readers;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by morci7 on 27/12/15.
 */
public class DatabaseReader {

    public final static Logger log = Logger.getLogger(PostgresqlReader.class.getName());

    public Connection connection = null;
    public Long limit = 500L;
    public Long offset = 0L;
    public String select = null;
    public String[] columnNames;
    public Boolean moreRows = true;


    public DatabaseReader(Properties prop) {
        if(prop.getProperty("limit")!=null){
            limit = Long.valueOf(prop.getProperty("limit"));
        }
    }

    public void killConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getRows() throws SQLException {

        PreparedStatement statement = connection.prepareStatement(select + " limit "+limit+" offset "+offset);
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();

        ArrayList<String> list = new ArrayList<String>();
        while (rs.next()) {
            String data = new String();
            for (int i = 1; i < rsmd.getColumnCount(); i++) {
                if (i != 1) data += ",";
                if (rsmd.getColumnType(i) == 12) {
                    data += "\""+rs.getString(i)+"\"";
                }else{
                    data += rs.getString(i);
                }
            }
            list.add(data);
        }

        if(list.size() < limit) moreRows = false;
        else{
            offset += limit;
        }

        return list;
    }

    public void prepareSelect(String select) throws SQLException {
        this.select = select;

        PreparedStatement statement = connection.prepareStatement(select + " limit 1");
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        columnNames = new String[rsmd.getColumnCount()];
        for(int i = 0; i < rsmd.getColumnCount(); i++){
            columnNames[i] = rsmd.getColumnName(i+1);
        }

        return;
    }

    public Boolean hasMoreRows() {
        return moreRows;
    }


    public String[] getColumnNames(){
        return columnNames;
    }
}
