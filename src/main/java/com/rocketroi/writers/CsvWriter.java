package com.rocketroi.writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by morci7 on 24/12/15.
 */
public class CsvWriter implements InterfaceWriter {

    File outputFile = null;
    FileWriter fw;
    BufferedWriter bw;

    public CsvWriter(String pathTofile) {
        super();
        init(pathTofile);
    }

    public void write(ArrayList<String> list) throws Exception {

        for(String s : list){
            bw.write(s+"\n");
        }

    }

    public void closeFile() throws Exception {

        bw.close();

    }

    public void init(String pathTofile) {

        outputFile = new File(pathTofile);

        // if file doesnt exists, then create it
        try {
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
        }catch (Exception e){}
    }

    public void writeColumnsName(String[] columnNames) throws Exception
    {
        fw = new FileWriter(outputFile.getAbsoluteFile());
         bw = new BufferedWriter(fw);
        for(int i = 0; i < columnNames.length; i++){
            if(i != 0) bw.write(",");
            bw.write(columnNames[i]);
        }
    }
}
