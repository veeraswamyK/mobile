package enums;

public enum PlatformType {
    ANDROID("Android"),
    IOS("iOS");

    private final String value;

    PlatformType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PlatformType from(String value) {
        for (PlatformType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return ANDROID;
    }
}
