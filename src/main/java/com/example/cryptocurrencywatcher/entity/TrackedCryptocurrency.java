package com.example.cryptocurrencywatcher.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "tracked_cryptocurrencies")
public class TrackedCryptocurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "cryptocurrency_id")
    private Cryptocurrency cryptocurrency;
    private double startingSavedPrice;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
