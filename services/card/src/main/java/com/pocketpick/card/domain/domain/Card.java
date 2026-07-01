package com.pocketpick.card.domain.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private String setId;

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
}
