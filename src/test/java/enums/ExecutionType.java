package enums;

public enum ExecutionType {
    LOCAL_EMULATOR("local-emulator"),
    LOCAL_REAL("local-real"),
    CLOUD("cloud"),
    HYBRID("hybrid");

    private final String value;

    ExecutionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ExecutionType from(String value) {
        for (ExecutionType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return LOCAL_EMULATOR;
    }

    public boolean isLocal() {
        return this == LOCAL_EMULATOR || this == LOCAL_REAL;
    }

    public boolean isCloud() {
        return this == CLOUD || this == HYBRID;
    }
}
