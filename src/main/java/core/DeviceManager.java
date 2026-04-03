package core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeviceManager {

    public static List<String> getConnectedDevices() throws Exception {

        List<String> devices = new ArrayList<>();

        Process process = Runtime.getRuntime().exec("adb devices");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;

        while ((line = reader.readLine()) != null) {
            if (line.endsWith("device") && !line.startsWith("List")) {
                devices.add(line.split("\\s")[0]);
            }
        }

        return devices;
    }

        public static void setupDevices() throws Exception {

            List<String> devices = DeviceManager.getConnectedDevices();

            for (int i = 0; i < devices.size(); i++) {

                String udid = devices.get(i);
                int port = 8200 + i;

                System.out.println("Device: " + udid + " Port: " + port);
            }
        }
    }
