package utils;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class ADBUtils {

    private static final Logger LOG = LoggerUtil.getLogger(ADBUtils.class);

    private ADBUtils() {}

    /** Executes an ADB command and returns its stdout lines. */
    public static List<String> execute(String... command) {
        List<String> output = new ArrayList<>();
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                LOG.warn("ADB command exited with code {}: {}", exitCode, String.join(" ", command));
            }
        } catch (Exception e) {
            LOG.error("Failed to execute ADB command: {}", String.join(" ", command), e);
        }
        return output;
    }

    /** Executes a shell command on the connected device. */
    public static List<String> shell(String udid, String shellCommand) {
        return execute(resolveAdb(), "-s", udid, "shell", shellCommand);
    }

    /** Installs an APK on the specified device. */
    public static void installApk(String udid, String apkPath) {
        LOG.info("Installing APK on {}: {}", udid, apkPath);
        execute(resolveAdb(), "-s", udid, "install", "-r", apkPath);
    }

    /** Clears app data on the specified device. */
    public static void clearAppData(String udid, String packageName) {
        LOG.info("Clearing data for {} on {}", packageName, udid);
        shell(udid, "pm clear " + packageName);
    }

    /** Returns path to the adb executable, preferring ANDROID_HOME env var. */
    public static String resolveAdb() {
        String androidHome = System.getenv("ANDROID_HOME");
        if (androidHome != null && !androidHome.isEmpty()) {
            String separator = System.getProperty("os.name", "").toLowerCase().contains("win") ? "\\" : "/";
            return androidHome + separator + "platform-tools" + separator + "adb";
        }
        return "adb"; // fall back to PATH
    }
}
