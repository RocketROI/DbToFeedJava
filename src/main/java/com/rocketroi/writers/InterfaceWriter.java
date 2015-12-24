package com.rocketroi.writers;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by morci7 on 24/12/15.
 */
public interface InterfaceWriter {


    public void write(ArrayList<String> list) throws Exception;

    public void init(String outputFile);

}
