package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

public class InvalidCreateRequestException extends RuntimeException {
    public InvalidCreateRequestException() {
        super(StockAppUtil.ERROR_INVALID_REQUEST);
    }
}
