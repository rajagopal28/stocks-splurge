package com.payconiq.rm.stocks.app.service;

import com.payconiq.rm.stocks.app.exception.*;
import com.payconiq.rm.stocks.app.model.Stock;
import com.payconiq.rm.stocks.app.repository.StockRepository;
import com.payconiq.rm.stocks.app.util.StockAppUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    public Stock addNewStock(Stock stock) {
        // check if the stack object has all required attributes
        if(StockAppUtil.validateNewStock(stock)) {
            validateIfAnyStockWithGivenName(stock.getName());
            // set timestamp values
            long now = Instant.now().getEpochSecond();
            stock.setLastUpdated(now);
            stock.setTimeCreated(now);
            // persist the stock object
            return stockRepository.save(stock);
        } else {
            throw new InvalidCreateRequestException();
        }
    }

    private void validateIfAnyStockWithGivenName(String name) {
        // validate if the name is not there
        Optional<Stock> stockByName = stockRepository.findByName(name);
        if(stockByName.isPresent()) {
            throw new DuplicateStockException(name);
        }
    }

    public Iterable<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(long id) {
        Optional<Stock> foundStock = stockRepository.findById(id);
        // if not present throw error
        if (foundStock.isEmpty()) {
            throw new StockNotFoundException(id);
        }
        return foundStock.get();
    }

    public Stock deleteStock(long id) {
        // open transaction
        // check if the stock by id
        Stock foundStock = getStockById(id);
        // else delete the entry
        validateIfStockIsInLockPeriod(foundStock);
        stockRepository.delete(foundStock);
        // close transaction
        return foundStock;
    }

    private void validateIfStockIsInLockPeriod(Stock foundStock) {
        // check if the stock is not updated in past 5minutes
        if(StockAppUtil.isWithinLockTime(foundStock)) {
            throw new LockWindowEnabledException();
        }
    }

    public Stock updateStock(long id, Stock stock) {
        if(!StockAppUtil.validateUpdateStock(stock)) {
            throw new InvalidUpdateRequestException();
        }
        // open transaction
        // check if the stock by id
        Stock foundStock = getStockById(id);
        validateIfStockIsInLockPeriod(foundStock);
        if(StringUtils.isNotBlank(stock.getName())) {
            if(!stock.getName().equals(foundStock.getName())) {
                validateIfAnyStockWithGivenName(stock.getName());
            }
            foundStock.setName(stock.getName());
        }
        if(stock.getCurrentPrice() > 0) {
            foundStock.setCurrentPrice(stock.getCurrentPrice());
        }
        foundStock.setLastUpdated(Instant.now().getEpochSecond());
        // update entry
        return stockRepository.save(foundStock);
    }
}
