package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

/**
 * DuplicateStockException to indicate the name unique constraint violation in the database.
 *
 * @author Rajagopal
 */
public class DuplicateStockException extends RuntimeException {
    /**
     * Constructor to create the DuplicateStockException appropriate message.
     *
     * @param stockName which was vialating the unique name constraint.
     */
    public DuplicateStockException(String stockName) {
        super(StockAppUtil.FN_ERROR_STOCK_WITH_NAME_PRESENT.apply(stockName));
    }
}
