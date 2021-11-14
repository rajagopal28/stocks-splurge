package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

/**
 * LockWindowEnabledException to indicate the stock update/delete is performed withing LockWindow.
 *
 * @author Rajagopal
 */
public class LockWindowEnabledException extends RuntimeException {
    /**
     * Constructor to create the LockWindowEnabledException appropriate message.
     */
    public LockWindowEnabledException() {
        super(StockAppUtil.ERROR_LOCK_WINDOW_ENABLED);
    }
}
