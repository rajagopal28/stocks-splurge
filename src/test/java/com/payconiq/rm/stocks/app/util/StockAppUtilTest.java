package com.payconiq.rm.stocks.app.util;

import com.payconiq.rm.stocks.app.model.Stock;
import org.junit.Assert;
import org.junit.Test;


public class StockAppUtilTest {

    @Test
    public void testValidateNewStock() {
        double currentPrice = Math.random() * 999;
        String sname = "Stock1";
        Stock s1 = new Stock(sname, currentPrice);
        Stock s2 = new Stock("", currentPrice);
        Stock s3 = new Stock(sname, 0);

        Assert.assertTrue(StockAppUtil.validateNewStock(s1));
        Assert.assertFalse(StockAppUtil.validateNewStock(s2));
        Assert.assertFalse(StockAppUtil.validateNewStock(s3));
    }


    @Test
    public void testValidateUpdateStock() {
        double currentPrice = Math.random() * 999;
        String sname = "Stock1";
        Stock s1 = new Stock(sname, currentPrice);
        Stock s2 = new Stock("", currentPrice);
        Stock s3 = new Stock(sname, 0);
        Stock s4 = new Stock("", 0);

        Assert.assertTrue(StockAppUtil.validateUpdateStock(s1));
        Assert.assertTrue(StockAppUtil.validateUpdateStock(s2));
        Assert.assertTrue(StockAppUtil.validateUpdateStock(s3));
        Assert.assertFalse(StockAppUtil.validateUpdateStock(s4));
    }
}