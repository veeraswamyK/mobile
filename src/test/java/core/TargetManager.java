package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TargetManager {

    private static final Logger LOG = LoggerFactory.getLogger(TargetManager.class);
    private static final List<ExecutionTarget> TARGETS = new ArrayList<>();

    private TargetManager() {}

    public static synchronized void addTarget(ExecutionTarget target) {
        TARGETS.add(target);
        LOG.info("Registered target: {}", target);
    }

    public static List<ExecutionTarget> getTargets() {
        return Collections.unmodifiableList(TARGETS);
    }

    public static ExecutionTarget getTarget(int index) {
        if (index < 0 || index >= TARGETS.size()) {
            throw new IndexOutOfBoundsException("No target at index " + index + ". Total: " + TARGETS.size());
        }
        return TARGETS.get(index);
    }

    public static int size() {
        return TARGETS.size();
    }

    public static synchronized void clear() {
        TARGETS.clear();
    }
}
