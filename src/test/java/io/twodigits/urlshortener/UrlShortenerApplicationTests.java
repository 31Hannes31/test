package io.twodigits.urlshortener;

import io.twodigits.urlshortener.controller.ShortURLRequest;
import io.twodigits.urlshortener.controller.URLShortenerController;
import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.repository.URLRepository;
import io.twodigits.urlshortener.repository.URLStatisticRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UrlShortenerApplicationTests {

    private static final String HOST = "http://localhost:8080/";
    private static final String USER = "johannes.ilisei@accenture.com";
    private String urlId1;
    private String urlId2;
    @Autowired
    URLShortenerController urlShortenerController;
    @Autowired
    private URLRepository repository;
    @Autowired
    private URLStatisticRepository urlStatisticRepository;

    @Test
    void contextLoads() {
        assertThat(urlShortenerController).isNotNull();
        assertThat(repository).isNotNull();
        assertThat(urlStatisticRepository).isNotNull();
    }

    @Test
    void urlShortenerService() {
        /* Create URLs */
        urlId1 = createAndAssertShortURL("https://www.accenture.com/de-de/about/company/company-index");
        urlId2 = createAndAssertShortURL("https://www.xing.com/pages/accenture");

        /* ListURLs */
        var urls = urlShortenerController.listURLs(USER);
        assertURLs(urls);

        /* GetUrl */
        var getUrl = urlShortenerController.getUrl(Optional.of(USER), urlId1);
        assertURL(getUrl);

        /* Call URL-1 two times */
        callURL();
        callURL();

        /* Check statistics */
        assertStatistics();

        /* Delete */
        urlShortenerController.deleteUrl(USER, urlId1);
        assertThat(urlShortenerController.getUrl(Optional.of(USER), urlId1)).isNull();
    }

    private void assertURLs(Iterable<URL> urls) {
        assertThat(urls).hasSize(2);
        var urlsList = new ArrayList<URL>();
        urls.forEach(urlsList::add);
        var urlIDs = urlsList.stream().map(URL::getId).collect(Collectors.toList());
        assertThat(urlIDs).hasSize(2).contains(urlId1, urlId2);
    }

    private String createAndAssertShortURL(String originalURL) {
        var shortURLRequest = new ShortURLRequest();
        shortURLRequest.setUser(USER);
        shortURLRequest.setOriginalUrl(originalURL);
        var shortURL = urlShortenerController.createShortUrl(shortURLRequest);
        var shortURLId = shortURL.replace(HOST, "");
        assertThat(shortURLId).hasSize(4);
        return shortURLId;
    }

    private void assertURL(URL getUrl) {
        assertThat(getUrl.getId()).isEqualTo(urlId1);
        assertThat(getUrl.getOriginalUrl()).isEqualTo("https://www.accenture.com/de-de/about/company/company-index");
        assertThat(getUrl.getPageImpressions()).isZero();
        assertThat(getUrl.getUrlUser()).isEqualTo(USER);
    }

    private void callURL() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName(HOST);
        request.setRequestURI("/" + urlId1);
        var redirectView = urlShortenerController.redirectToOriginalUrl(request);
        assertThat(redirectView.getUrl()).isEqualTo("https://www.accenture.com/de-de/about/company/company-index");
    }

    private void assertStatistics() {
        var statistics = urlShortenerController.getStatistics(urlId1);
        assertThat(statistics.getUrlId()).isEqualTo(urlId1);
        assertThat(statistics.getPageImpressions()).isEqualTo(2);
        assertThat(statistics.getUrlStatistics()).hasSize(2);
    }
}
