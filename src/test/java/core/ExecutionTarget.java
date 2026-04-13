package core;

public class ExecutionTarget {

    public String type; // local/cloud
    public String udid;
    public String deviceName;
    public int systemPort;

    public ExecutionTarget(String type, String udid, String deviceName, int systemPort) {
        this.type = type;
        this.udid = udid;
        this.deviceName = deviceName;
        this.systemPort = systemPort;
    }
}