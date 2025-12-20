package btw.community.betteritemframemaps;

import api.AddonHandler;
import api.BTWAddon;
import api.config.AddonConfig;

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
        super.preInitialize();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        log("Initialization started. Checking config values...");
        log("Config - Rotation Enabled: " + enableRotation);
        log("Config - Debug Logs Enabled: " + enableDebugLogs);
    }

    @Override
    public void registerConfigProperties(AddonConfig config) {
        config.registerBoolean("enableRotation", true,
                "True: You can rotate the map by right-clicking.",
                "False: Fixed North-up orientation.");

        config.registerBoolean("enableDebugLogs", false,
                "Enables verbose debug messages in console.",
                "Warning: This may spam the console if many Item Frames are visible.");
    }

    @Override
    public void handleConfigProperties(AddonConfig config) {
        enableRotation = config.getBoolean("enableRotation");
        enableDebugLogs = config.getBoolean("enableDebugLogs");

        System.out.println("[BFM] Config loaded. Rotation: " + enableRotation + ", Debug: " + enableDebugLogs);
    }

    public static BetterItemFrameMapsAddon getInstance() {
        return instance;
    }

    public static void log(String message) {
        AddonHandler.logMessage("[BFM] " + message);
    }

    public static void logWarning(String message) {
        AddonHandler.logWarning("[BFM WARN] " + message);
    }

    public static void debug(String message) {
        if (enableDebugLogs) {
            AddonHandler.logMessage("[BFM Debug] " + message);
        }
    }
}