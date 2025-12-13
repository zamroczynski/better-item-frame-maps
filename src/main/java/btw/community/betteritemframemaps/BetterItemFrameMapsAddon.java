package btw.community.betteritemframemaps;

import btw.AddonHandler;
import btw.BTWAddon;
import java.util.Map;

public class BetterItemFrameMapsAddon extends BTWAddon {
    private static BetterItemFrameMapsAddon instance;

    public static boolean enableRotation;
    public static boolean enableDebugLogs;

    public BetterItemFrameMapsAddon() {
        super();
        instance = this;
    }

    @Override
    public void preInitialize() {
        registerProperty("enableRotation", "true", "True: You can rotate the map by right-clicking. False: Fixed North-up orientation.");
        registerProperty("enableDebugLogs", "true", "Enables debug messages in console.");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    @Override
    public void handleConfigProperties(Map<String, String> propertyValues) {
        enableRotation = Boolean.parseBoolean(propertyValues.get("enableRotation"));
        enableDebugLogs = Boolean.parseBoolean(propertyValues.get("enableDebugLogs"));

        log("Rotation Enabled: " + enableRotation);
        log("Debug Logs Enabled: " + enableDebugLogs);
    }

    public static BetterItemFrameMapsAddon getInstance() {
        return instance;
    }

    public static void log(String message) {
        AddonHandler.logMessage("[BFM] " + message);
    }

    public static void debug(String message) {
        if (enableDebugLogs) {
            AddonHandler.logMessage("[BFM Debug] " + message);
        }
    }
}