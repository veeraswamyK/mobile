package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TextContext {

    private final Map<String, Object> store = new HashMap<>();

    public void set(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        return store.get(key);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(store.get(key));
    }

    public <T> Optional<T> getOptional(String key, Class<T> type) {
        Object value = store.get(key);
        return value == null ? Optional.empty() : Optional.of(type.cast(value));
    }

    public boolean contains(String key) {
        return store.containsKey(key);
    }

    public void remove(String key) {
        store.remove(key);
    }

    public void clear() {
        store.clear();
    }
}
