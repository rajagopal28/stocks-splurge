package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(long id) {
        super(StockAppUtil.FN_ERROR_STOCK_WITH_ID_NOT_PRESENT.apply(id));
    }
}
