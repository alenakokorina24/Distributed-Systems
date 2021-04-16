package model;

import java.util.List;

public class Node {
    private long id;
    private String user;
    private Double lon;
    private Double lat;
    private List<Tag> tags;

    public Node(long id, String user, Double lon, Double lat, List<Tag> tags) {
        this.id = id;
        this.user = user;
        this.lon = lon;
        this.lat = lat;
        this.tags = tags;
    }

    public Node(long id, String user, Double lon, Double lat) {
        this.id = id;
        this.user = user;
        this.lon = lon;
        this.lat = lat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
}
