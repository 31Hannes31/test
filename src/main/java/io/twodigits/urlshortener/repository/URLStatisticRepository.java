package io.twodigits.urlshortener.repository;

import io.twodigits.urlshortener.model.URLStatistic;
import org.springframework.data.repository.CrudRepository;

public interface URLStatisticRepository extends CrudRepository<URLStatistic, Long> {

    Iterable<URLStatistic> findURLStatisticByUrl_Id(String id); // NOSONAR
}
