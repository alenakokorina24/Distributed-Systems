package ru.nsu.kokorina.distributedsystems.model;

import ru.nsu.kokorina.distributedsystems.generated.Node;

public class Tag {
    private long nodeId;
    private Node.Tag tag;

    public Tag(long nodeId, String key, String value) {
        this.nodeId = nodeId;
        this.tag = new Node.Tag();
        this.tag.setK(key);
        this.tag.setV(value);
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public Node.Tag getTag() {
        return tag;
    }

    public void setTag(Node.Tag tag) {
        this.tag = tag;
    }

    public void setK(String key) {
        this.tag.setK(key);
    }

    public void setV(String value) {
        this.tag.setV(value);
    }
}
