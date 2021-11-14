package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

/**
 * StockNotFoundException to indicate the given stock id is not found in the database.
 *
 * @author Rajagopal
 */
public class StockNotFoundException extends RuntimeException {
    /**
     * Constructor to create the StockNotFoundException appropriate message.
     *
     * @param id which was not found in DB.
     */
    public StockNotFoundException(long id) {
        super(StockAppUtil.FN_ERROR_STOCK_WITH_ID_NOT_PRESENT.apply(id));
    }
}
