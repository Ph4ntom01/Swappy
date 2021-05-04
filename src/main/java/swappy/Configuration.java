package swappy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration {

    private static final Logger LOG = Logger.getLogger(Configuration.class.getName());
    private static final String PROPERTIES_PATH = "./config.properties";

    private File file;

    protected Configuration() {
        file = new File(PROPERTIES_PATH);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    defineDefaultValues();
                }
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Cannot create file : {0}", file.getName());
                System.exit(1);
            }
        }
    }

    private void defineDefaultValues() {
        try (OutputStream output = new FileOutputStream(PROPERTIES_PATH)) {
            Properties prop = new Properties();
            prop.setProperty(EProperties.DARK_MODE.getKey(), "true");
            prop.setProperty(EProperties.MINIMIZE.getKey(), "false");
            prop.setProperty(EProperties.DNS_WINDOWS.getKey(), "84.200.69.80");
            prop.setProperty(EProperties.DNS_ALTERNATIVE.getKey(), "80.67.169.12");
            prop.setProperty(EProperties.NET_ADAPTER.getKey(), "Ethernet");
            prop.store(output, null);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "An error occurred while writing value into the file : {0} !", file.getName());
        }
    }

    protected void updateValue(String key, String value) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_PATH)) {
            prop.load(input);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "An error occurred while reading the file : {0} !", file.getName());
        }
        try (OutputStream output = new FileOutputStream(PROPERTIES_PATH)) {
            prop.setProperty(key, value);
            prop.store(output, null);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "An error occurred while writing value into the file : {0} !", file.getName());
        }
    }

    protected String readValue(EProperties key) {
        String value = "";
        try (InputStream input = new FileInputStream(PROPERTIES_PATH)) {
            Properties prop = new Properties();
            prop.load(input);
            value = prop.getProperty(key.getKey());
        } catch (IOException io) {
            LOG.log(Level.WARNING, "An error occurred while reading the file : {0} !", file.getName());
        }
        return value;
    }

}
