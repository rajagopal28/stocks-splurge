package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

public class InvalidUpdateRequestException extends RuntimeException {
    public InvalidUpdateRequestException() {
        super(StockAppUtil.ERROR_INVALID_UPDATE_REQUEST);
    }
}
