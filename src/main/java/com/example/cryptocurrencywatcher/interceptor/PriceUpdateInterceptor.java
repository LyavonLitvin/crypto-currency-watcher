package com.example.cryptocurrencywatcher.interceptor;


import com.example.cryptocurrencywatcher.entity.Currency;
import com.example.cryptocurrencywatcher.entity.TrackedCryptocurrency;
import com.example.cryptocurrencywatcher.service.CurrencyService;
import com.example.cryptocurrencywatcher.service.TrackedCryptocurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class PriceUpdateInterceptor extends EmptyInterceptor {

    private TrackedCryptocurrencyService trackedCryptocurrencyService;
    @Value("${changingPercentage}")
    private double percentage;

    @Lazy
    @Autowired
    public void setTrackedCryptocurrencyService(TrackedCryptocurrencyService trackedCryptocurrencyService) {
        this.trackedCryptocurrencyService = trackedCryptocurrencyService;
    }

    @Override
    public void postFlush(Iterator entities) {
        List<Currency> updatedCurrencies = CurrencyService.getObjectsOnlyCurrencyType(entities);
        List<TrackedCryptocurrency> trackedCryptocurrencies = trackedCryptocurrencyService.getTrackedCryptocurrencyForCryptocurrenciesWherePriceChangePercentageMoreThanGiven(updatedCurrencies, percentage);
    }
}
