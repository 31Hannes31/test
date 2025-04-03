package io.twodigits.urlshortener.repository;

import io.twodigits.urlshortener.model.URL;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface URLRepository extends CrudRepository<URL, String> {

    /**
     * Find a URL by the identifier of the user.
     *
     * @param urlUser the ID of a user
     * @return a collection of URL objects
     */
    Iterable<URL> findByUrlUser(String urlUser);

    /**
     * Find a URL by the identifier of the user as well as its ID.
     *
     * @param id the unique ID of an URL
     * @param urlUser the ID of a user
     * @return a collection of URL objects
     */
    Optional<URL> findByIdAndUrlUser(String id, String urlUser);

}
