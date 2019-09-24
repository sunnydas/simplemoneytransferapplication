package com.transfer.money.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static Properties configProps;

    private static String configFileName = "moneytransferapp.properties";

    static{
        configProps = new Properties();
        loadProperties();
    }

    private static void loadProperties() {
        FileReader propFileReader = null;
        try{
            File propFileHandle = getConfigFileHandle();
            if(propFileHandle.exists()){
                propFileReader = new FileReader(propFileHandle);
                configProps.load(propFileReader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSilently(propFileReader);
        }
    }

    private static File getConfigFileHandle() {
        return new File(
                        ConfigManager.class.
                                getClassLoader().
                                getResource(configFileName).getFile()
                );
    }

    private static void closeSilently(FileReader propFileReader) {
        if(propFileReader != null){
            try {
                propFileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getProperty(String propertyKey){
        return configProps.getProperty(propertyKey);
    }


}
