package com.payconiq.rm.stocks.app.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double currentPrice;
    private long timeCreated;
    private long lastUpdated;

    public Stock(String name, double price) {
        this.name = name;
        this.currentPrice = price;
    }
}
