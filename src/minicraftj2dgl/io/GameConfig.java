package minicraftj2dgl.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class GameConfig {

    private static final String CONFIG_FILE_PATH = "config/config.dat";

    private final HashMap<String, Object> config;

    public GameConfig() {
        this.config = loadConfig();
    }

    public void putConfig(String key, Object value) {
        config.put(key, value);
        persistConfig(config);
    }

    public <T> T getEntry(String key, Class<T> type) {
        return type.cast(config.get(key));
    }

    private static HashMap<String, Object> loadConfig() {
        try (ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(CONFIG_FILE_PATH))) {
            return (HashMap<String, Object>) objectStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            // TODO: better error handling

            // Make sure the directory exists and save the default config.
            new File("./config").mkdir();
            HashMap<String, Object> defaultConfig = getDefaultConfig();
            persistConfig(defaultConfig);
            return defaultConfig;
        }
    }

    private static void persistConfig(HashMap<String, Object> gameConfig) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (ObjectOutputStream objectStream = new ObjectOutputStream(
                        new FileOutputStream(CONFIG_FILE_PATH))) {
                    objectStream.writeObject(gameConfig);
                    objectStream.flush();
                } catch (IOException ex) {
                    // TODO: better error handling
                    JOptionPane.showMessageDialog(null,
                            "Error saving game configuration,\nthe game will now close.",
                            "Error Persisting Config",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        }).start();
    }

    private static HashMap<String, Object> getDefaultConfig() {
        HashMap<String, Object> defaultConfig = new HashMap<>();
        defaultConfig.put("lastSave", null);
        return defaultConfig;
    }
}
