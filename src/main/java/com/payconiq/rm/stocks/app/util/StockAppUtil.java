package com.payconiq.rm.stocks.app.util;

import com.payconiq.rm.stocks.app.model.Stock;
import org.apache.commons.lang3.StringUtils;

public interface StockAppUtil {

    static boolean validateStock(Stock stock) {
        return StringUtils.isNotBlank(stock.getName())
                && stock.getCurrentPrice() > 0;
    }
}
