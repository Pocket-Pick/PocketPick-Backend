package com.pocketpick.card.domain.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cards", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"set_id", "number"})
})
@Getter
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id", nullable = false)
    private CardSet set;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Supertype supertype;

    private String subtype;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    private String imageSmallUrl;

    private String imageLargeUrl;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardType> types = new ArrayList<>();
}
