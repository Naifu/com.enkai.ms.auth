package com.enkai.ms.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    public PropertyReader() {}

    public Properties getProperties(String fileName) throws IOException {
    	
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

        Properties properties = new Properties();
        properties.load(inputStream);

        return properties;	    

    }

}