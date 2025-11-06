package newspapercrud.common;


import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class Configuration {
    private Properties prop;
    private static Configuration instance = null;

    public Configuration() {

        try{
            prop = new Properties();
            InputStream is = Configuration.class.getClassLoader().getResourceAsStream("config/propertiers.xml");
            if (is == null) {
                is = Configuration.class.getClassLoader().getResourceAsStream("config.properties.xml");
            }
            if (is == null) {
                is = Configuration.class.getClassLoader().getResourceAsStream("config/properties.xml");
            }
            if (is == null) {
                throw new IllegalStateException("No se encontró el fichero de configuración XML en classpath. Buscado: config/propertiers.xml, config.properties.xml, config/properties.xml");
            }
            try (InputStream autoClose = is) {
                prop.loadFromXML(autoClose);
            }
        }catch (IOException o){
            throw new IllegalStateException("Error cargando configuración desde XML", o);
        }
    }


    public String getProperty(String key){
        return prop.getProperty(key);
    }


    public static Configuration getInstance(){
        if (instance == null){
            instance = new Configuration();
            return instance;
        } else {
            return instance;
        }
    }


    public String getKey(String key) {
        return prop.getProperty(key);
    }

    public String getRequired(String key) {
        String v = getKey(key);
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("Clave de configuración requerida ausente: " + key);
        }
        return v;
    }



}
