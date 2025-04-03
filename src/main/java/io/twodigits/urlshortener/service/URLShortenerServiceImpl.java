package io.twodigits.urlshortener.service;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLStatistic;
import io.twodigits.urlshortener.repository.URLRepository;
import io.twodigits.urlshortener.repository.URLStatisticRepository;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class URLShortenerServiceImpl implements URLShortenerService {
    @Autowired
    private URLRepository repository;

    @Autowired
    private URLStatisticRepository urlStatisticRepository;

    @Override
    public Iterable<URL> listURLs(String user) {
        var urls = repository.findByUrlUser(user);
        urls.forEach(url -> url.setUrlStatisticSet(null));
        return urls;
    }

    @Override
    public Optional<URL> getURL(String user, String id) {
        var optionalURL = repository.findByIdAndUrlUser(id, user);
        optionalURL.ifPresent(url -> url.setUrlStatisticSet(null));
        return optionalURL;
    }

    @Override
    public Optional<URL> getURL(String id) {
        var optionalURL = repository.findById(id);
        optionalURL.ifPresent(url -> url.setUrlStatisticSet(null));
        return optionalURL;
    }

    @Override
    public URL addURL(String user, String originalUrl) {
        return repository.save(new URL(generateRandomStringId(), user, originalUrl));
    }

    private String generateRandomStringId() {
        RandomStringGenerator randomStringGenerator =
                new RandomStringGenerator.Builder()
                        .withinRange('0', 'z')
                        .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                        .build();
        String randomStringId = randomStringGenerator.generate(4);

        // ID already exists? -> Create a new one
        if (repository.existsById(randomStringId)) {
            randomStringId = randomStringGenerator.generate(4);
        }
        return randomStringId;
    }

    @Override
    public void deleteURL(String user, String id) {
        Optional<URL> url = repository.findByIdAndUrlUser(id, user);
        url.ifPresent(value -> repository.delete(value));
    }

    @Override
    public String getOriginalURLAndStoreStatistics(HttpServletRequest request) {
        String originalURL = "";
        Optional<URL> optionalURL = repository.findById(request.getRequestURI().replace("/", ""));
        if (optionalURL.isPresent()) {
            storeURLStatistics(request, optionalURL.get());
            originalURL = optionalURL.get().getOriginalUrl();
        }
        return originalURL;
    }

    private void storeURLStatistics(HttpServletRequest request, URL url) {
        url.incrementPageImpressions();
        repository.save(url);

        URLStatistic urlStatistic = new URLStatistic();
        urlStatistic.setUrl(url);
        urlStatistic.setReferrer(request.getHeader(HttpHeaders.REFERER));
        urlStatistic.setPageViewDate(LocalDateTime.now());
        urlStatistic.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        urlStatisticRepository.save(urlStatistic);
    }

    @Override
    public Iterable<URLStatistic> getURLStatistics(String id) {
        return urlStatisticRepository.findURLStatisticByUrl_Id(id);
    }
}
