package com.rocketroi;

import com.rocketroi.readers.DatabaseReader;
import com.rocketroi.readers.MysqlReader;
import com.rocketroi.readers.PostgresqlReader;
import com.rocketroi.writers.CsvWriter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by morci7 on 24/12/15.
 */
public class DbToFeed {

    private final static Logger log = Logger.getLogger(PostgresqlReader.class.getName());


    public static void main(String args[]) throws Exception{

        Properties prop = checkArguments(args);

        if(prop != null) {

            DatabaseReader reader;
            if (prop.getProperty("database").equals("postgres")) {
                reader = new PostgresqlReader(prop);
            } else {
                reader = new MysqlReader(prop);
            }
            CsvWriter writer = new CsvWriter(prop.getProperty("output"));
            writer.writeColumnsName(reader.getColumnNames());
            while (reader.hasMoreRows()) {
                ArrayList<String> list = reader.getRows();
                writer.write(list);
            }
            writer.closeFile();
            reader.killConnection();

            log.info("finalizado");
        }

    }

    private static Properties checkArguments(String args[]){
        //Si no hay argumentos, se para el proceso
        if(args == null || args.length == 0){
            log.info("Not enough arguments");
            return null;
        }

        //Se comprueba si hay un fichero properties correcto
        String propertiesPath = null;
        for (String s : args){
            if(s.startsWith("-file=")){
                propertiesPath = s.replace("-file=", "");
            }
        }

        File file = new File(propertiesPath);
        if(!file.exists()){
            ClassLoader classLoader = DbToFeed.class.getClassLoader();
            propertiesPath = classLoader.getResource("DbToFeed.properties").getFile();
        }
        Properties prop = getProperties(propertiesPath);

        if(prop.getProperty("user") == null || prop.getProperty("database") == null){
            return null;
        }

        return prop;
    }


    private static Properties getProperties(String path){

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(path);

            // load a properties file
            prop.load(input);


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }

}
