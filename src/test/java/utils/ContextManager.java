package utils;

public final class ContextManager {

    private static final ThreadLocal<TextContext> CONTEXT =
            ThreadLocal.withInitial(TextContext::new);

    private ContextManager() {}

    public static TextContext getContext() {
        return CONTEXT.get();
    }

    public static void unload() {
        CONTEXT.get().clear();
        CONTEXT.remove();
    }
}
