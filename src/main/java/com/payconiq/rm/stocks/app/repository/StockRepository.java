package com.payconiq.rm.stocks.app.repository;

import com.payconiq.rm.stocks.app.model.Stock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StockRepository extends CrudRepository<Stock, Long> {
    Optional<Stock> findByName(String stockName);
}
