package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.FrameworkConstants;
import org.slf4j.Logger;

import java.io.InputStream;

public final class JsonUtils {

    private static final Logger LOG = LoggerUtil.getLogger(JsonUtils.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {}

    /**
     * Reads a JSON file from the testdata classpath directory and returns the root node.
     *
     * @param fileName e.g. "login.json"
     */
    public static JsonNode readJson(String fileName) {
        String path = FrameworkConstants.TESTDATA_DIR + fileName;
        try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Test data file not found on classpath: " + path);
            }
            return MAPPER.readTree(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON: " + path, e);
        }
    }

    /** Returns a string value at a dot-separated path, e.g. "validUser.username". */
    public static String getString(String fileName, String jsonPath) {
        return navigate(readJson(fileName), jsonPath).asText();
    }

    /** Returns an int value at the given dot-separated path. */
    public static int getInt(String fileName, String jsonPath) {
        return navigate(readJson(fileName), jsonPath).asInt();
    }

    /** Returns a boolean value at the given dot-separated path. */
    public static boolean getBoolean(String fileName, String jsonPath) {
        return navigate(readJson(fileName), jsonPath).asBoolean();
    }

    /** Deserializes the full JSON file into the given POJO class. */
    public static <T> T readAs(String fileName, Class<T> type) {
        String path = FrameworkConstants.TESTDATA_DIR + fileName;
        try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("File not found on classpath: " + path);
            }
            return MAPPER.readValue(is, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize " + fileName + " to " + type.getSimpleName(), e);
        }
    }

    private static JsonNode navigate(JsonNode root, String path) {
        JsonNode current = root;
        for (String part : path.split("\\.")) {
            current = current.get(part);
            if (current == null) {
                throw new RuntimeException("JSON path segment '" + part + "' not found in path: " + path);
            }
        }
        return current;
    }
}
