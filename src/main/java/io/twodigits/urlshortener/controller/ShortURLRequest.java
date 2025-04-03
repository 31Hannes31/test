package io.twodigits.urlshortener.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortURLRequest {
    private String user;
    private String originalUrl;
}
