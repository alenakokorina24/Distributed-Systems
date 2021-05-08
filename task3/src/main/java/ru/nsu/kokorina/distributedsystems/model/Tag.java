package ru.nsu.kokorina.distributedsystems.model;

import lombok.Builder;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String key;

    private String value;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private NodeEntity node;

    public Tag() {

    }

    public Tag(long id, String key, String value, NodeEntity node) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.node = node;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public NodeEntity getNode() {
        return node;
    }

    public void setNode(NodeEntity node) {
        this.node = node;
    }
}
