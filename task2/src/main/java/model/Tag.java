package model;

public class Tag {
    private long nodeId;
    private String key;
    private String value;

    public Tag(long nodeId, String key, String value) {
        this.nodeId = nodeId;
        this.key = key;
        this.value = value;
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
