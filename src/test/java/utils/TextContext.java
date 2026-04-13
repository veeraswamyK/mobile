package utils;

import java.util.HashMap;
import java.util.Map;

public class TextContext {

    private Map<String, Object> context = new HashMap<>();

    public void set(String key, Object value) {
        context.put(key, value);
    }

    public Object get(String key) {
        return context.get(key);
    }
}