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
        BFMConfig.getInstance();

        BFMConfig.log("Initializing Version " + this.getVersionString() + "...");
        BFMConfig.log("Rotation Enabled: " + BFMConfig.enableRotation);
        BFMConfig.log("Debug Logs Enabled: " + BFMConfig.enableDebugLogs);
    }

    public static BetterItemFrameMapsAddon getInstance() {
        return instance;
    }
}