package com.payconiq.rm.stocks.app.util;

import com.payconiq.rm.stocks.app.model.Stock;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.function.Function;

/**
 * StockAppUtil interface containing constants and validation methods that are re-used.
 * @author Rajagopal
 */
public interface StockAppUtil {
    long LOCK_WINDOW_IN_SECONDS = 60*5; // 5 minutes

    String VIEW_NAME_HOME = "home.html";

    String ENDPOINT_HOME_INDEX = "/";
    String ENDPOINT_STOCKS = "/api/stocks";
    String ENDPOINT_STOCKS_WITH_ID = "/api/stocks/{id}";

    String ERROR_REQUEST_VALIDATION_FAILED = "Validation failed";
    String ERROR_INVALID_REQUEST = "Invalid request data passed!";
    String ERROR_LOCK_WINDOW_ENABLED = "Cannot manipulate stock within Lock window!";
    String ERROR_INVALID_UPDATE_REQUEST = "Blank request cannot be updated!";

    Function<String, String> FN_ERROR_STOCK_WITH_NAME_PRESENT = (s) -> "Stock with Name("+s+") already found!";
    Function<Long, String> FN_ERROR_STOCK_WITH_ID_NOT_PRESENT = (i) -> "Stock with ID("+i+") not found!";


    /**
     * ValidateNewStock method to validate a create new stock request.
     *
     * @param stock request which is to be validation for creation.
     * @return boolean indicating the validity of the stock create request.
     */
    static boolean validateNewStock(Stock stock) {
        return StringUtils.isNotBlank(stock.getName())
                && stock.getCurrentPrice() > 0;
    }

    /**
     * ValidateUpdateStock method to validate an update existing stock request.
     *
     * @param stock request which is to be validation for update.
     * @return boolean indicating the validity of the stock update request.
     */
    static boolean validateUpdateStock(Stock stock) {
        return StringUtils.isNotBlank(stock.getName())
                || stock.getCurrentPrice() > 0;
    }

    /**
     * isWithinLockTime method to validate a given stock from DB has been changed within LockWindow.
     *
     * @param stock request which is to be validation for updating within LockWindow.
     * @return boolean indicating the validity of the stock to be updated.
     */
    static boolean isWithinLockTime(Stock stock) {
        long now = Instant.now().getEpochSecond();
        return (now - stock.getLastUpdated()) <= LOCK_WINDOW_IN_SECONDS;
    }
}
