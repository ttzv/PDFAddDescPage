package fpdf.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Configuration {

    public static final String IN_PATH = "inPath";
    public static final String OUT_PATH = "outPath";
    public static final String CLEAR_ON_DROP = "clearOnDrop";

    private final Path CONFIG_PATH = Paths.get(System.getProperty("user.dir"), "app", "conf", "fpdf.properties");
    private Properties properties;
    private static Configuration instance;

    private Configuration() throws IOException {
        properties = new Properties();
        if (Files.exists(CONFIG_PATH)){
            properties.load(new FileInputStream(new File(CONFIG_PATH.toUri())));
        } else {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.createFile(CONFIG_PATH);
        }
    }

    public Properties get(){
        return properties;
    }

    public void store() throws IOException {
        properties.store(new FileOutputStream(new File(CONFIG_PATH.toUri())),null);
    }

    public static Configuration getInstance() throws IOException {
        if(instance == null) instance = new Configuration();
        return instance;
    }

}
