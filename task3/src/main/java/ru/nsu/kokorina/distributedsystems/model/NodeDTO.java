package ru.nsu.kokorina.distributedsystems.model;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class NodeDTO {
    @NotNull
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private Double lon;

    @NotNull
    private Double lat;

    @NotNull
    private Map<String, String> tags;

    public NodeDTO(Long id, String username, Double lon, Double lat, Map<String, String> tags) {
        this.id = id;
        this.username = username;
        this.lon = lon;
        this.lat = lat;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
}
