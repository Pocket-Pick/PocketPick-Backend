package com.pocketpick.card.domain.controller;

import com.pocketpick.card.domain.domain.PokemonType;
import com.pocketpick.card.domain.domain.Rarity;
import com.pocketpick.card.domain.domain.Supertype;
import com.pocketpick.card.domain.dto.CardSearchRequest;
import com.pocketpick.card.domain.dto.CardSummaryResponse;
import com.pocketpick.card.domain.dto.PageResponse;
import com.pocketpick.card.domain.service.CardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardUseCase cardUseCase;

    @GetMapping
    public ResponseEntity<PageResponse<CardSummaryResponse>> searchCards(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) PokemonType type,
            @RequestParam(required = false) Rarity rarity,
            @RequestParam(required = false) String setId,
            @RequestParam(required = false) Supertype supertype,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        CardSearchRequest request = new CardSearchRequest(name, type, rarity, setId, supertype);
        return ResponseEntity.ok(PageResponse.from(cardUseCase.searchCards(request, pageable)));
    }
}
