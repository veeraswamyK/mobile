package utils;

public class DeviceUtils {

    public static String getDeviceName() {
        return System.getProperty("deviceName", "emulator-5554");
    }
}