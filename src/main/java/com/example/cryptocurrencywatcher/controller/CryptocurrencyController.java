package com.example.cryptocurrencywatcher.controller;

import com.example.cryptocurrencywatcher.dto.RequestedCryptocurrency;
import com.example.cryptocurrencywatcher.entity.Cryptocurrency;
import com.example.cryptocurrencywatcher.service.CryptocurrencyService;
import com.example.cryptocurrencywatcher.service.TrackedCryptocurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/cryptocurrency")
public class CryptocurrencyController {
    private final TrackedCryptocurrencyService trackedCryptocurrencyService;
    private final CryptocurrencyService cryptocurrencyService;

    public CryptocurrencyController(TrackedCryptocurrencyService trackedCryptocurrencyService, CryptocurrencyService cryptocurrencyService) {
        this.trackedCryptocurrencyService = trackedCryptocurrencyService;
        this.cryptocurrencyService = cryptocurrencyService;
    }

    @GetMapping("/notify")
    public ResponseEntity<Object> notify(String username, String symbol) {
        trackedCryptocurrencyService.notify(username, symbol);
        return ResponseEntity.ok().build();
    }

    @GetMapping("showAllCryptocurrency")
    public ResponseEntity<Set<RequestedCryptocurrency>> findRequestedCryptocurrencies() {
        return ResponseEntity.ok(cryptocurrencyService.findRequestedCryptocurrencies());
    }

    @GetMapping("/price/findBySymbol")
    public ResponseEntity<Cryptocurrency> findRequestedCryptocurrencyPriceBySymbol(String symbol) {
        return cryptocurrencyService.findRequestedCryptocurrencyPriceBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
