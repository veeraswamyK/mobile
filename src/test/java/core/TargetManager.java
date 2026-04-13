package core;

import java.util.ArrayList;
import java.util.List;

public class TargetManager {

    private static final List<ExecutionTarget> targets =
            new ArrayList<>();

    private TargetManager() {
    }

    public static void addTarget(
            ExecutionTarget target) {

        targets.add(target);
    }

    public static List<ExecutionTarget> getTargets() {
        return targets;
    }

    public static ExecutionTarget getTarget(int index) {
        return targets.get(index);
    }

    public static int size() {
        return targets.size();
    }

    public static void clear() {
        targets.clear();
    }
}