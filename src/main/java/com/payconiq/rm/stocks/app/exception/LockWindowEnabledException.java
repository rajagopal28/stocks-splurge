package com.payconiq.rm.stocks.app.exception;

import com.payconiq.rm.stocks.app.util.StockAppUtil;

public class LockWindowEnabledException extends RuntimeException {
    public LockWindowEnabledException() {
        super(StockAppUtil.ERROR_LOCK_WINDOW_ENABLED);
    }
}
