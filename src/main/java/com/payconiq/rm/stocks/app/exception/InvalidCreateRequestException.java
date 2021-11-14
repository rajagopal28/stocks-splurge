package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

/**
 * InvalidCreateRequestException to indicate the request sent to create new stock in invalid.
 *
 * @author Rajagopal
 */
public class InvalidCreateRequestException extends RuntimeException {
    /**
     * Constructor to create the InvalidCreateRequestException appropriate message.
     */
    public InvalidCreateRequestException() {
        super(StockAppUtil.ERROR_INVALID_REQUEST);
    }
}
