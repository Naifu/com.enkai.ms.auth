package com.enkai.ms.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Hilfs Utility um ein Property File zu lesen und in ein Property Objekt umzuwandeln
 * 
 * @author	Dirk
 * @version	1.0
 */

public class PropertyReader {

    public PropertyReader() {}

    /**
     * Hauptroutine, liest und transformiert Properties File
     * Geht davon aus, dass die Property Files im src Verzeichnis sind
     * 
     * @author	Dirk
     * @version	1.0
     * 
     * @param	fileName	Dateiname des Property Files
     * @return				Property Objekt mit Werten aus dem File
     * @throws				IOException
     */
    public Properties getProperties(String fileName) throws IOException {
    	
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

        Properties properties = new Properties();
        properties.load(inputStream);

        return properties;	    

    }

}