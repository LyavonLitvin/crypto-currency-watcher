package com.example.cryptocurrencywatcher.repository;

import com.example.cryptocurrencywatcher.entity.TrackedCryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackedCryptocurrencyRepository extends JpaRepository<TrackedCryptocurrency, Long> {

    List<TrackedCryptocurrency> findAllByCryptocurrencyId(long cryptocurrencyId);

    Optional<TrackedCryptocurrency> findByCryptocurrencyId(long cryptocurrencyId);
}
