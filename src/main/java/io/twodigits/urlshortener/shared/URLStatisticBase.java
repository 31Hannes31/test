package io.twodigits.urlshortener.shared;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class URLStatisticBase {
    private LocalDateTime pageViewDate;
    private String userAgent;
    private String referrer;

    public URLStatisticBase withPageViewDate(LocalDateTime pageViewDate) {
        this.pageViewDate = pageViewDate;
        return this;
    }
    public URLStatisticBase withUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
    public URLStatisticBase withReferrer(String referrer) {
        this.referrer = referrer;
        return this;
    }
}
