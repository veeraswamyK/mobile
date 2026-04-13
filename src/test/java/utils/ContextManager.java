package utils;

public class ContextManager {

    private static ThreadLocal<TextContext> scenarioContext =
            ThreadLocal.withInitial(TextContext::new);

    public static TextContext getContext() {
        return scenarioContext.get();
    }

    public static void unload() {
        scenarioContext.remove();
    }
}