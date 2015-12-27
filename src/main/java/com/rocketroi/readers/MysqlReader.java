package com.rocketroi.readers;


import java.sql.*;
import java.util.Properties;

/**
 * Created by morci7 on 24/12/15.
 */
public class MysqlReader extends DatabaseReader{

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

    public void initConnection(Properties prop) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("Where is your Mysql JDBC Driver? "
                    + "Include in your library path!", e);
            return;
        }

        try {
            String url = "jdbc:mysql://"+prop.getProperty("databaseServer")+":"+prop.getProperty("databasePort")+"/"+prop.getProperty("databaseName");
            connection = DriverManager.getConnection(url, prop.getProperty("user"), prop.getProperty("password"));

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


}
