package utils;

public final class DeviceUtils {

    private DeviceUtils() {}

    public static String getDeviceName() {
        return System.getProperty("deviceName", "Android Emulator");
    }

    public static String getUdid() {
        return System.getProperty("udid", "emulator-5554");
    }

    public static int getSystemPort() {
        return Integer.parseInt(System.getProperty("systemPort", "8200"));
    }
}
