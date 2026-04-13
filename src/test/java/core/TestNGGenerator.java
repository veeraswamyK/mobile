package core;

import java.io.FileWriter;
import java.util.List;

public class TestNGGenerator {

    public static void generateXML() {

        try {
            List<String> devices = DeviceManager.getConnectedDevices();

            if (devices.isEmpty()) {
                throw new RuntimeException("No devices connected!");
            }

            StringBuilder xml = new StringBuilder();

            xml.append("<!DOCTYPE suite SYSTEM \"https://testng.org/testng-1.0.dtd\">\n");
            xml.append("<suite name=\"Dynamic Suite\" parallel=\"tests\" thread-count=\"")
                    .append(devices.size()).append("\">\n");

            int port = 8200;

            for (int i = 0; i < devices.size(); i++) {

                xml.append("<test name=\"Device_").append(i + 1).append("\">\n");
                xml.append("<parameter name=\"udid\" value=\"").append(devices.get(i)).append("\"/>\n");
                xml.append("<parameter name=\"systemPort\" value=\"").append(port + i).append("\"/>\n");

                xml.append("<classes>\n");
                xml.append("<class name=\"runners.TestRunner\"/>\n");
                xml.append("</classes>\n");

                xml.append("</test>\n");
            }

            xml.append("</suite>");

            FileWriter writer = new FileWriter("dynamic-testng.xml");
            writer.write(xml.toString());
            writer.close();

            System.out.println(" TestNG XML generated for " + devices.size() + " devices");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}