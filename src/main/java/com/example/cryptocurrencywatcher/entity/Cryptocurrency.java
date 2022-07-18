package com.example.cryptocurrencywatcher.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "cryptocurrency_id")
public class Cryptocurrency extends Currency{

}
