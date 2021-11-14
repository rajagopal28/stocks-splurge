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

/**
 * StockService majorly handles the business logic of the Stock Splurge Application. <br/>
 * It relies on the StockRepository to fetch the related data. <br/>
 * The service is filled with validations of multiple sorts and business logics,
 * like lock window validation, throwing multiple custom exceptions.
 *
 * @author  Rajagopal
 */
@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    /**
     * AddNewStock method created new Stock with given data. <br/>
     * The method performs the following validations: <br/>
     * @throws InvalidCreateRequestException for Validation on request data.<br/>
     * @throws  DuplicateStockException for Validation on the stock name to be unique and non existing in the db.<br/>
     *
     * @param stock the Stock data with which the new data entry is to be creation.<br/>
     * @return Stock data with created data/reference information.
     */
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

    /**
     * ValidateIfAnyStockWithGivenName checks if there are any stocks with given name. <br/>
     * @throws DuplicateStockException if there is another stock with given name.
     *
     * @param name of the stock to be verified.
     */
    private void validateIfAnyStockWithGivenName(String name) {
        // validate if the name is not there
        Optional<Stock> stockByName = stockRepository.findByName(name);
        if(stockByName.isPresent()) {
            throw new DuplicateStockException(name);
        }
    }

    /**
     * GetAllStocks method fetches the list of all stocks persisted from repository.
     *
     * @return List of all stocks from the system.
     */
    public Iterable<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    /**
     * GetStockById fetches the stock based on given id.
     *
     * @throws StockNotFoundException if the stock is not found.
     *
     * @param id of the stock to be fetched.
     * @return Stock fetched with given data.
     */
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

    /**
     * ValidateIfStockIsInLockPeriod checks if the stock entry has data update in the LockWindow time period.
     *
     * @param stock the stock which is to be validated.
     * @throws LockWindowEnabledException if data found in time range.
     */
    private void validateIfStockIsInLockPeriod(Stock stock) {
        // check if the stock is not updated in past 5minutes
        if(StockAppUtil.isWithinLockTime(stock)) {
            throw new LockWindowEnabledException();
        }
    }

    /**
     * UpdateStock method is used to update the stock data if it is not updated in the lock window for valid request.
     *
     * @throws InvalidUpdateRequestException if none of the fields are provided.
     * @throws LockWindowEnabledException if the stock is updated within lock window.
     * @throws DuplicateStockException if the new name is already found for other stock.
     *
     * @param id of the stock to which the data is to be updated.
     * @param stock details of the stock to be updated.
     *
     * @return Stock with the updated information.
     */
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
