package au.edu.rmit.sept.webapp.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "resources")
public class Resources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resourceId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(name = "resource_type", nullable = false)
    private String resourceType; // This will store "Article", "Video", or "Guide"

    @Lob
    @Column(nullable = true)
    private String body; // Nullable because videos won't have a body

    @Column(nullable = true)
    private String url; // For videos or external links

    @Column(nullable = false)
    private String tag; // "Trending" or "Best Practices"

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    // Getters and setters

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int id) {
        this.resourceId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

