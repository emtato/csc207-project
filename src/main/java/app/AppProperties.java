package app;

import java.io.IOException;
import java.util.Properties;

public class AppProperties {

    private final Properties prop;

    public AppProperties() {
        this.prop = new Properties();
        try {
            //load a properties file from class path, inside static method
            prop.load(AppProperties.class.getClassLoader().getResourceAsStream("apikeys.properties"));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Properties getProperties() {
        return prop;
    }
}
