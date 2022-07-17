package com.example.cryptocurrencywatcher.service;

import com.example.cryptocurrencywatcher.entity.Cryptocurrency;
import com.example.cryptocurrencywatcher.entity.Currency;
import com.example.cryptocurrencywatcher.entity.TrackedCryptocurrency;
import com.example.cryptocurrencywatcher.entity.User;
import com.example.cryptocurrencywatcher.repository.TrackedCryptocurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrackedCryptocurrencyService {
    private final CryptocurrencyService cryptocurrencyService;
    private final UserService userService;
    private final TrackedCryptocurrencyRepository trackedCryptocurrencyRepository;

    public TrackedCryptocurrencyService(CryptocurrencyService cryptocurrencyService, UserService userService, TrackedCryptocurrencyRepository trackedCryptocurrencyRepository) {
        this.cryptocurrencyService = cryptocurrencyService;
        this.userService = userService;
        this.trackedCryptocurrencyRepository = trackedCryptocurrencyRepository;
    }

    public Optional<TrackedCryptocurrency> save(TrackedCryptocurrency trackedCryptocurrency) {
        if (trackedCryptocurrency == null) {
            return Optional.empty();
        } else {
            return Optional.of(trackedCryptocurrencyRepository.save(trackedCryptocurrency));
        }
    }

    public List<TrackedCryptocurrency> findAllByCryptocurrency(Currency currency) {
        List<TrackedCryptocurrency> trackedCryptocurrencies = trackedCryptocurrencyRepository.findAllByCryptocurrencyId(currency.getId());
        if (trackedCryptocurrencies == null || trackedCryptocurrencies.isEmpty()) {
            return Collections.emptyList();
        } else {
            return trackedCryptocurrencies;
        }
    }

    public Optional<TrackedCryptocurrency> findByCryptocurrency(Cryptocurrency cryptocurrency) {
        return trackedCryptocurrencyRepository.findByCryptocurrencyId(cryptocurrency.getId());
    }

    public void notify(String username, String symbol) throws IllegalArgumentException {
        if (username == null || symbol == null) {
            throw new IllegalArgumentException();
        } else {
            Optional<? extends Currency> currency = cryptocurrencyService.findBySymbol(symbol);
            Optional<User> user = userService.findByUsername(username);
            if (currency.isPresent() && user.isPresent()) {
                TrackedCryptocurrency trackedCryptocurrency = TrackedCryptocurrency.builder()
                        .currency(currency.get())
                        .startingSavedPrice(currency.get().getPrice())
                        .user(user.get())
                        .build();
                save(trackedCryptocurrency);
            }
        }
    }

    public List<TrackedCryptocurrency> getTrackedCryptocurrencyForCryptocurrencyWherePriceChangePercentageMoreThanGiven(Currency currency, double percentage) {
        if (currency == null) {
            return Collections.emptyList();
        } else {
            return findAllByCryptocurrency(currency).stream()
                    .filter(trackedCryptocurrency -> CalculationService.isDifferenceBetweenValuesMoreThanPercentage(currency.getPrice(), trackedCryptocurrency.getStartingSavedPrice(), percentage))
                    .collect(Collectors.toList());
        }
    }

    public List<TrackedCryptocurrency> getTrackedCryptocurrencyForCryptocurrenciesWherePriceChangePercentageMoreThanGiven(List<Currency> currencies, double percentage) {
        return currencies.stream()
                .map(currency -> getTrackedCryptocurrencyForCryptocurrencyWherePriceChangePercentageMoreThanGiven(currency, percentage))
                .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
    }

    public void notifyAboutPriceChange(List<TrackedCryptocurrency> trackedCryptocurrencies) {
        trackedCryptocurrencies.forEach(trackedCryptocurrency -> log.warn(String.join(", ",
                trackedCryptocurrency.getCurrency().getSymbol(),
                trackedCryptocurrency.getUser().getUsername(),
                String.valueOf(CalculationService.getPercentageBetweenValues(trackedCryptocurrency.getCurrency().getPrice(),
                        trackedCryptocurrency.getStartingSavedPrice())))));
    }
}
