package core;

public class ExecutionTarget {

    private final String type;
    private final String udid;
    private final String deviceName;
    private final int systemPort;

    public ExecutionTarget(String type, String udid, String deviceName, int systemPort) {
        this.type       = type;
        this.udid       = udid;
        this.deviceName = deviceName;
        this.systemPort = systemPort;
    }

    public String getType()       { return type; }
    public String getUdid()       { return udid; }
    public String getDeviceName() { return deviceName; }
    public int    getSystemPort() { return systemPort; }

    @Override
    public String toString() {
        return String.format("ExecutionTarget{type='%s', device='%s', udid='%s', port=%d}",
                type, deviceName, udid, systemPort);
    }
}
