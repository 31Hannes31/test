package io.twodigits.urlshortener.model;

import io.twodigits.urlshortener.shared.URLStatisticBase;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table
public class URLStatistic extends URLStatisticBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private URL url;
}
