package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

public class DuplicateStockException extends RuntimeException {
    public DuplicateStockException(String stockName) {
        super(StockAppUtil.FN_ERROR_STOCK_WITH_NAME_PRESENT.apply(stockName));
    }
}
