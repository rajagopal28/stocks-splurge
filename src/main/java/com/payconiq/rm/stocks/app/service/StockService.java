package com.payconiq.rm.stocks.app.service;

import com.payconiq.rm.stocks.app.model.Stock;
import com.payconiq.rm.stocks.app.repository.StockRepository;
import com.payconiq.rm.stocks.app.util.StockAppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    public Stock addNewStock(Stock stock) {
        // check if the stack object has all required attributes
        if(StockAppUtil.validateStock(stock)) {
            // validate if the name is not there
            Optional<Stock> stockByName = stockRepository.findByName(stock.getName());
            if(stockByName.isPresent()) {
                //TODO:: create custom exception
                throw new RuntimeException("Stock with Name("+stock.getName()+") already found!");
            }
            // set timestamp values
            long now = Instant.now().getEpochSecond();
            stock.setLastUpdated(now);
            stock.setTimeCreated(now);
            // persist the stock object
            return stockRepository.save(stock);
        } else {
            //TODO:: create custom exception
            throw new RuntimeException("Invalid request data passed!");
        }
    }

    public Iterable<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}
