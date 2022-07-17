package com.example.cryptocurrencywatcher.service;

import com.example.cryptocurrencywatcher.dto.RequestedCryptocurrency;
import com.example.cryptocurrencywatcher.entity.Cryptocurrency;
import com.example.cryptocurrencywatcher.entity.Currency;
import com.example.cryptocurrencywatcher.mapper.CryptocurrencyMapper;
import com.example.cryptocurrencywatcher.repository.CryptocurrencyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CryptocurrencyService {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    public static final String NAME_OF_FILE_FOR_AVAILABLE_CRYPTOCURRENCIES = "available-cryptocurrencies.json";
    public static final String CRYPTOCURRENCY_INFORMATION_URL = "https://api.coinlore.net/api/ticker/";

    public CryptocurrencyService(ObjectMapper objectMapper, ResourceLoader resourceLoader, CryptocurrencyRepository cryptocurrencyRepository) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    public Optional<? extends Currency> findBySymbol(String symbol) {
        return cryptocurrencyRepository.findBySymbol(symbol);
    }

    public Set<RequestedCryptocurrency> findRequestedCryptocurrencies() {
        return CryptocurrencyMapper.mapToRequestedCryptocurrenciesSet(getRequestedCryptocurrenciesFromResources(NAME_OF_FILE_FOR_AVAILABLE_CRYPTOCURRENCIES));
    }

    private Set<Cryptocurrency> getRequestedCryptocurrenciesFromResources(String fileName) {
        try (InputStream inputStream = resourceLoader.getResource(String.join("", "classpath:", fileName))
                .getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>(){});
        } catch (IOException e) {
            log.error(String.join(" ", "Problems with reading JSON file", fileName));
            return null;
        }
    }

    public Optional<Cryptocurrency> findRequestedCryptocurrencyPriceBySymbol(String symbol) throws IllegalArgumentException {
        if (symbol != null && exists(symbol)) {
            return cryptocurrencyRepository.findBySymbol(symbol);
        } else  {
            throw new IllegalArgumentException(String.join(": ", "No currency with symbol", symbol));
        }
    }

    private boolean exists(String symbol) {
        Set<Cryptocurrency> currencies = getRequestedCryptocurrenciesFromResources(NAME_OF_FILE_FOR_AVAILABLE_CRYPTOCURRENCIES);
        if (currencies == null || currencies.isEmpty()) {
            return false;
        } else {
            return currencies.stream().anyMatch(currency -> currency.getSymbol().equals(symbol));
        }
    }

    @Scheduled(fixedRate = 60000)
    private void updateStoredCryptocurrencyPrices() {
        String url = createUrlToGetDataOfCryptocurrencies(getRequestedCryptocurrenciesFromResources(NAME_OF_FILE_FOR_AVAILABLE_CRYPTOCURRENCIES));
        List<Cryptocurrency> cryptocurrencies = getActualDataForRequestedCurrenciesFromResource(url);
        if (cryptocurrencies != null && !cryptocurrencies.isEmpty()) {
            cryptocurrencyRepository.saveAll(cryptocurrencies);
        }
    }

    private String createUrlToGetDataOfCryptocurrencies(Set<? extends Currency> cryptocurrencies) {
        if (cryptocurrencies == null || cryptocurrencies.isEmpty()) {
            return null;
        } else {
            String urlParameters = getParametersForCryptocurrencies(cryptocurrencies);
            return generateUrlToAnotherResource(CRYPTOCURRENCY_INFORMATION_URL, urlParameters);
        }
    }

    private String getParametersForCryptocurrencies(Set<? extends Currency> cryptocurrencies) {
        if (cryptocurrencies == null || cryptocurrencies.isEmpty()) {
            return null;
        } else {
            return cryptocurrencies.stream()
                    .map(cryptocurrency -> String.valueOf(cryptocurrency.getId()))
                    .collect(Collectors.joining(","));
        }
    }

    private String generateUrlToAnotherResource(String resourceUrl, String urlParameters) {
        if (resourceUrl == null || urlParameters == null) {
            return null;
        } else {
            return String.join("", resourceUrl, "?id=",  urlParameters);
        }
    }

    private List<Cryptocurrency> getActualDataForRequestedCurrenciesFromResource(String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Cryptocurrency[] cryptocurrencies = restTemplate.getForEntity(url, Cryptocurrency[].class).getBody();
            return cryptocurrencies == null || cryptocurrencies.length == 0 ? null : List.of(cryptocurrencies);
        } catch (RuntimeException exception) {
            log.error("Problem with getting the current state of the cryptocurrency");
            return null;
        }
    }
}
