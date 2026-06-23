package com.pocketpick.card.domain.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sets")
@Getter
@NoArgsConstructor
public class CardSet {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String series;

    private int printedTotal;

    private int total;

    private String ptcgoCode;

    private LocalDate releaseDate;

    private String symbolImageUrl;

    private String logoImageUrl;

    @OneToMany(mappedBy = "set", fetch = FetchType.LAZY)
    private List<Card> cards = new ArrayList<>();
}
