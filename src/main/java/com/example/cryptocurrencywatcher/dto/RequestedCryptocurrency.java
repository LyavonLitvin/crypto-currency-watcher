package com.example.cryptocurrencywatcher.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestedCryptocurrency {
    private long id;
    private String symbol;
    private double price;
}
