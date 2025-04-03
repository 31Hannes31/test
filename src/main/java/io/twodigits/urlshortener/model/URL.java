package io.twodigits.urlshortener.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table
public class URL {
    /**
     * The unique ID of an URL
     */
    @Id
    private String id;

    /**
     * The URL for which a short URL is provided
     */
    private String originalUrl;

    /**
     * The ID of a user to which this URL belongs
     */
    private String urlUser;

    /**
     * How often the Short-URL has been called
     */
    private Integer pageImpressions;

    @OneToMany(orphanRemoval = true, mappedBy="url")
    private Set<URLStatistic> urlStatisticSet;

    public URL() {}

    public URL(String id, String urlUser, String originalUrl) {
        this.id = id;
        this.urlUser = urlUser;
        this.originalUrl = originalUrl;
        this.pageImpressions = 0;
    }

    public void incrementPageImpressions() {
        this.pageImpressions++;
    }
}
