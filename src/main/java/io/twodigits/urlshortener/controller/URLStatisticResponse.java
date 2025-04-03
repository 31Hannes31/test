package io.twodigits.urlshortener.controller;

import io.twodigits.urlshortener.shared.URLStatisticBase;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class URLStatisticResponse {
    private String urlId;
    /**
     * How often the Short-URL has been called
     */
    private Integer pageImpressions;

    private List<URLStatisticBase> urlStatistics;

    URLStatisticResponse() {
        this.urlStatistics = new ArrayList<>();
    }
}
