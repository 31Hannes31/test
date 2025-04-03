package io.twodigits.urlshortener.service;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLStatistic;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface URLShortenerService {
    /**
     * Get a list of all URLs that belong to a user.
     *
     * @return a list of users
     */
    Iterable<URL> listURLs(String user);

    /**
     * Add a new URL to the collection of URLs for a user.
     *
     * @return The URL object which has been created
     */
    URL addURL(String user, String url);

    /**
     * Get a specific URL of a user by its ID.
     * @return The URL object
     */
    Optional<URL> getURL(String user, String id);

    /**
     * Return a specific URL by ID.
     *
     * @return The URL object
     */
    Optional<URL> getURL(String id);

    /**
     * Delete a specific URL which belongs to a user.
     */
    void deleteURL(String user, String id);

    String getOriginalURLAndStoreStatistics(HttpServletRequest request);

    Iterable<URLStatistic> getURLStatistics(String id);
}
