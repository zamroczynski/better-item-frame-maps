package btw.community.betteritemframemaps;

import btw.BTWAddon;

public class BetterItemFrameMapsAddon extends BTWAddon {
    private static BetterItemFrameMapsAddon instance;

    public BetterItemFrameMapsAddon() {
        super();
        instance = this;
    }

    @Override
    public void initialize() {
        BFMConfig config = BFMConfig.getInstance();
        config.log("Initializing Version " + this.getVersionString() + "...");
        config.log("Rotation Enabled: " + config.enableRotation);
        config.log("Debug Logs Enabled: " + config.enableDebugLogs);
    }

    public static BetterItemFrameMapsAddon getInstance() {
        return instance;
    }
}