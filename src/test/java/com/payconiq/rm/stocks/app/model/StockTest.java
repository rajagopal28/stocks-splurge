package com.payconiq.rm.stocks.app.model;


import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

public class StockTest {

    @Test
    public void testShouldCreateModelInstance() {
        long now = Instant.now().getEpochSecond();
        double currentPrice = Math.random() * 999;
        String sname = "Stock1";
        Stock stock = new Stock();
        stock.setCurrentPrice(currentPrice);
        stock.setName(sname);
        stock.setTimeCreated(now);
        stock.setLastUpdated(now);

        Assert.assertNotNull(stock);
        Assert.assertEquals(now, stock.getTimeCreated());
        Assert.assertEquals(now, stock.getLastUpdated());
        Assert.assertEquals(sname, stock.getName());
    }
}