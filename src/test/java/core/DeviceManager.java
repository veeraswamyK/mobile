package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ADBUtils;

import java.util.ArrayList;
import java.util.List;

public final class DeviceManager {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceManager.class);

    private DeviceManager() {}

    /** Returns UDIDs of all currently connected ADB devices (physical and emulators). */
    public static List<String> getConnectedDevices() {
        List<String> devices = new ArrayList<>();
        String adb = ADBUtils.resolveAdb();

        List<String> lines = ADBUtils.execute(adb, "devices");
        LOG.debug("adb devices output: {}", lines);

        for (String line : lines) {
            line = line.trim();
            if (line.endsWith("\tdevice")) {
                String udid = line.split("\\s+")[0];
                devices.add(udid);
                LOG.info("Detected device: {}", udid);
            }
        }

        if (devices.isEmpty()) {
            LOG.warn("No connected ADB devices found.");
        }
        return devices;
    }
}
