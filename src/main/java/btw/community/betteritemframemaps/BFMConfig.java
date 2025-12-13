package btw.community.betteritemframemaps;

import btw.AddonHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class BFMConfig {
    private static BFMConfig instance;

    public static boolean enableRotation;
    public static boolean enableDebugLogs;

    private final File configFile;

    private BFMConfig() {
        this.configFile = new File("config", "BetterItemFrameMaps.properties");
        this.loadConfig();
    }

    public static BFMConfig getInstance() {
        if (instance == null) {
            instance = new BFMConfig();
        }
        return instance;
    }

    private void loadConfig() {
        Properties props = new Properties();

        boolean defaultRotation = true;
        boolean defaultDebug = true;

        try {
            if (!configFile.exists()) {
                if (configFile.getParentFile().mkdirs() || configFile.getParentFile().exists()) {
                    configFile.createNewFile();
                }
                saveConfig(defaultRotation, defaultDebug);
            }
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
            }
            enableRotation = Boolean.parseBoolean(props.getProperty("enableRotation", String.valueOf(defaultRotation)));
            enableDebugLogs = Boolean.parseBoolean(props.getProperty("enableDebugLogs", String.valueOf(defaultDebug)));
        } catch (IOException e) {
            AddonHandler.logMessage("BetterItemFrameMaps: Failed to load config! Using defaults. Error: " + e.getMessage());
            enableRotation = defaultRotation;
            enableDebugLogs = defaultDebug;
        }
    }

    private void saveConfig(boolean rotation, boolean debug) {
        Properties props = new Properties();
        props.setProperty("enableRotation", String.valueOf(rotation));
        props.setProperty("enableDebugLogs", String.valueOf(debug));

        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            props.store(fos, "Better Item Frame Maps Configuration");
        } catch (IOException e) {
            AddonHandler.logMessage("BetterItemFrameMaps: Failed to save config! Error: " + e.getMessage());
        }
    }

    public static void debug(String message) {
        if (enableDebugLogs) {
            AddonHandler.logMessage("[BFM Debug] " + message);
        }
    }

    public static void log(String message) {
        AddonHandler.logMessage("[BFM] " + message);
    }
}