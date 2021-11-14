package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

/**
 * InvalidUpdateRequestException to indicate the request to update stock is invalid.
 *
 * @author Rajagopal
 */
public class InvalidUpdateRequestException extends RuntimeException {
    /**
     * Constructor to create the InvalidUpdateRequestException appropriate message.
     */
    public InvalidUpdateRequestException() {
        super(StockAppUtil.ERROR_INVALID_UPDATE_REQUEST);
    }
}
