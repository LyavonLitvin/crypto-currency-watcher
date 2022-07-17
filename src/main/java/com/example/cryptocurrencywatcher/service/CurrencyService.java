package com.example.cryptocurrencywatcher.service;

import com.example.cryptocurrencywatcher.entity.Currency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CurrencyService {

    public static List<Currency> getObjectsOnlyCurrencyType(Iterator entities){
        List<Currency> currencies = new ArrayList<>();
        Object entity;
        while (entities.hasNext()){
            entity = entities.next();
            if (entity instanceof Currency) {
                currencies.add((Currency) entity);
            }
        }
        return currencies;
    }
}
