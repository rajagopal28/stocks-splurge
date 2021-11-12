package com.payconiq.rm.stocks.app.util;

import com.payconiq.rm.stocks.app.model.Stock;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.function.Function;

public interface StockAppUtil {
    long LOCK_WINDOW_IN_SECONDS = 60*5; // 5 minutes

    String ENDPOINT_STOCKS = "/api/stocks";
    String ENDPOINT_STOCKS_WITH_ID = "/api/stocks/{id}";
    String ERROR_INVALID_REQUEST = "Invalid request data passed!";
    String ERROR_LOCK_WINDOW_ENABLED = "Cannot manipulate stock withing Lock window!";
    String ERROR_INVALID_UPDATE_REQUEST = "Blank request cannot be updated!";

    Function<String, String> FN_ERROR_STOCK_WITH_NAME_PRESENT = (s) -> "Stock with Name("+s+") already found!";
    Function<Long, String> FN_ERROR_STOCK_WITH_ID_NOT_PRESENT = (i) -> "Stock with ID("+i+") not found!";


    static boolean validateNewStock(Stock stock) {
        return StringUtils.isNotBlank(stock.getName())
                && stock.getCurrentPrice() > 0;
    }

    static boolean validateUpdateStock(Stock stock) {
        return StringUtils.isNotBlank(stock.getName())
                || stock.getCurrentPrice() > 0;
    }

    static boolean isWithinLockTime(Stock stock) {
        long now = Instant.now().getEpochSecond();
        return (now - stock.getLastUpdated()) <= LOCK_WINDOW_IN_SECONDS;
    }
}
