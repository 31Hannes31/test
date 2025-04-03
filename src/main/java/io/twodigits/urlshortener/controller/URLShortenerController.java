package io.twodigits.urlshortener.controller;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLStatistic;
import io.twodigits.urlshortener.service.URLShortenerService;
import io.twodigits.urlshortener.shared.URLStatisticBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class URLShortenerController {

    @Autowired
    URLShortenerService urlShortenerService;

    private static final String HOST = "http://localhost:8080/";

    @PostMapping(path = "/createshorturl",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String createShortUrl(@RequestBody ShortURLRequest shortURLRequest) {
        URL url = urlShortenerService.addURL(shortURLRequest.getUser(), shortURLRequest.getOriginalUrl());
        return HOST + url.getId();
    }

    @GetMapping("/**")
    public RedirectView redirectToOriginalUrl(HttpServletRequest request) {
        return new RedirectView(urlShortenerService.getOriginalURLAndStoreStatistics(request));
    }

    @GetMapping("/urls")
    public Iterable<URL> listURLs(@RequestParam String user) {
        return urlShortenerService.listURLs(user);
    }

    @GetMapping("/url")
    public URL getUrl(@RequestParam Optional<String> user, @RequestParam String id) {
        if (user.isPresent()) {
            return urlShortenerService.getURL(user.get(), id).orElse(null);
        } else {
            return urlShortenerService.getURL(id).orElse(null);
        }
    }

    @DeleteMapping("/url")
    public void deleteUrl(@RequestParam String user, @RequestParam String id) {
        urlShortenerService.deleteURL(user, id);
    }

    @GetMapping("/statistic")
    public URLStatisticResponse getStatistics(@RequestParam String id) {
        return createUrlStatisticResponse(id, urlShortenerService.getURLStatistics(id),
                urlShortenerService.getURL(id).orElse(new URL()));
    }

    private static URLStatisticResponse createUrlStatisticResponse(String id, Iterable<URLStatistic> urlStatistics, URL url) {
        var response = new URLStatisticResponse();
        response.setUrlId(id);
        response.setPageImpressions(url.getPageImpressions());
        urlStatistics.forEach(urlStatistic -> response.getUrlStatistics()
                .add(new URLStatisticBase()
                        .withUserAgent(urlStatistic.getUserAgent())
                        .withReferrer(urlStatistic.getReferrer())
                        .withPageViewDate(urlStatistic.getPageViewDate())));
        return response;
    }
}
