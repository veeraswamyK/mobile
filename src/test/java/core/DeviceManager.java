package core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeviceManager {

    public static List<String> getConnectedDevices() {

        List<String> devices = new ArrayList<>();

        try {

            String adbPath =
                    System.getenv("ANDROID_HOME")
                            + "\\platform-tools\\adb.exe";

            Process process =
                    new ProcessBuilder(adbPath, "devices")
                            .redirectErrorStream(true)
                            .start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.endsWith("\tdevice")) {

                    String udid =
                            line.split("\\s+")[0];

                    devices.add(udid);
                }
            }

            process.waitFor();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to detect devices", e);
        }

        return devices;
    }
}