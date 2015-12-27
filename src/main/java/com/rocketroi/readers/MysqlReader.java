package com.rocketroi.readers;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by morci7 on 24/12/15.
 */
public class MysqlReader {

    private final static Logger log = Logger.getLogger(MysqlReader.class.getName());

    private Connection connection = null;
    private Long limit = 500L;
    private Long offset = 0L;
    private String select = null;
    private String[] columnNames;
    private Boolean moreRows = true;


    public MysqlReader(Properties prop) {

        //Inicializamos la conexion
        initConnection(prop);

        //Preparamos la consulta
        try {
            prepareSelect(prop.getProperty("select"));
        }catch (Exception e){
            e.printStackTrace();
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

    public void initConnection(Properties prop) {

        log.debug("-------- mysql "
                + "JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            log.error("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!", e);
            return;

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");



        try {

            connection = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("user"),
                    prop.getProperty("password"));

        } catch (SQLException e) {

            log.error("Connection Failed! Check output console", e);
            return;

        }

        if (connection != null) {
            log.debug("You made it, take control your database now!");
        } else {
            log.debug("Failed to make connection!");
        }
    }

    public Boolean hasMoreRows() {
        return moreRows;
    }


    public String[] getColumnNames(){
        return columnNames;
    }

}
