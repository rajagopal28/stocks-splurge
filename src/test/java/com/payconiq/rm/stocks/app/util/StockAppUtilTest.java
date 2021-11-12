package com.payconiq.rm.stocks.app.util;

import com.payconiq.rm.stocks.app.model.Stock;
import org.junit.Assert;
import org.junit.Test;


public class StockAppUtilTest {

    @Test
    public void testValidateStock() {
        double currentPrice = Math.random() * 999;
        String sname = "Stock1";
        Stock s1 = new Stock();
        Stock s2 = new Stock();
        Stock s3 = new Stock();
        s1.setCurrentPrice(currentPrice);
        s2.setCurrentPrice(currentPrice);
        s1.setName(sname);
        s3.setName(sname);

        Assert.assertTrue(StockAppUtil.validateStock(s1));
        Assert.assertFalse(StockAppUtil.validateStock(s2));
        Assert.assertFalse(StockAppUtil.validateStock(s3));
    }
}