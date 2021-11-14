package com.payconiq.rm.stocks.app.repository;

import com.payconiq.rm.stocks.app.model.Stock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * StockRepository to work with Stock Table and fetch data related to the Scheme.
 *
 * @author Rajagopal
 */
public interface StockRepository extends CrudRepository<Stock, Long> {
    /**
     * FindByName method to search the scheme for any stock with given name.
     *
     * @param stockName to be searched in the table
     * @return Optional<Stock> if found with given name.
     */
    Optional<Stock> findByName(String stockName);
}
