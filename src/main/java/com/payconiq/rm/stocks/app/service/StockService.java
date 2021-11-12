package com.payconiq.rm.stocks.app.service;

import com.payconiq.rm.stocks.app.model.Stock;
import com.payconiq.rm.stocks.app.repository.StockRepository;
import com.payconiq.rm.stocks.app.util.StockAppUtil;
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
            // validate if the name is not there
            Optional<Stock> stockByName = stockRepository.findByName(stock.getName());
            if(stockByName.isPresent()) {
                //TODO:: create custom exception
                throw new RuntimeException(StockAppUtil.FN_ERROR_STOCK_WITH_NAME_PRESENT.apply(stock.getName()));
            }
            // set timestamp values
            long now = Instant.now().getEpochSecond();
            stock.setLastUpdated(now);
            stock.setTimeCreated(now);
            // persist the stock object
            return stockRepository.save(stock);
        } else {
            //TODO:: create custom exception
            throw new RuntimeException(StockAppUtil.ERROR_INVALID_REQUEST);
        }
    }

    public Iterable<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(long id) {
        Optional<Stock> foundStock = stockRepository.findById(id);
        // if not present throw error
        if (foundStock.isEmpty()) {
            // TODO: custom exception
            throw new RuntimeException(StockAppUtil.FN_ERROR_STOCK_WITH_ID_NOT_PRESENT.apply(id));
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
            // TODO: custom exception
            throw new RuntimeException(StockAppUtil.ERROR_LOCK_WINDOW_ENABLED);
        }
    }
}
