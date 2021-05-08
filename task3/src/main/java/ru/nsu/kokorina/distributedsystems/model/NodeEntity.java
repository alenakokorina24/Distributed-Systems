package ru.nsu.kokorina.distributedsystems.model;

import lombok.Builder;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Builder
@Table(name = "nodes")
public class NodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private Double lon;

    private Double lat;

    @OneToMany(mappedBy = "node")
    private List<Tag> tags;

    public NodeEntity() {

    }

    public NodeEntity(long id, String username, Double lon, Double lat, List<Tag> tags) {
        this.id = id;
        this.username = username;
        this.lon = lon;
        this.lat = lat;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public NodeDTO convertToDTO() {
        Map<String, String> tags = this.getTags()
                .stream()
                .collect(Collectors.toMap(Tag::getKey, Tag::getValue));
        return new NodeDTO(id, username, lon, lat, tags);
    }
}
