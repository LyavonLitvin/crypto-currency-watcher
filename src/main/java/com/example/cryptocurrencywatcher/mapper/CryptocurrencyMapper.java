package com.example.cryptocurrencywatcher.mapper;

import com.example.cryptocurrencywatcher.dto.RequestedCryptocurrency;
import com.example.cryptocurrencywatcher.entity.Cryptocurrency;

import java.util.Set;
import java.util.stream.Collectors;

public class CryptocurrencyMapper {


    public static RequestedCryptocurrency mapToRequestedCryptocurrency(Cryptocurrency cryptocurrency) {
        if (cryptocurrency == null) {
            return null;
        } else {
            return RequestedCryptocurrency.builder()
                    .id(cryptocurrency.getId())
                    .symbol(cryptocurrency.getSymbol())
                    .build();
        }
    }

    public static Set<RequestedCryptocurrency> mapToRequestedCryptocurrenciesSet(Set<Cryptocurrency> cryptocurrencies) {
        if (cryptocurrencies == null || cryptocurrencies.isEmpty()) {
            return null;
        } else {
            return cryptocurrencies.stream()
                    .map(CryptocurrencyMapper::mapToRequestedCryptocurrency)
                    .collect(Collectors.toSet());
        }
    }
}
