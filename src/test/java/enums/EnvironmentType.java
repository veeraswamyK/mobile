package enums;

public enum EnvironmentType {
    QA("qa"),
    STAGING("staging"),
    PROD("prod");

    private final String value;

    EnvironmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EnvironmentType from(String value) {
        for (EnvironmentType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return QA;
    }
}
