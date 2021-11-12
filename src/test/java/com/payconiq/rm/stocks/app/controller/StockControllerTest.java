package com.payconiq.rm.stocks.app.controller;


import com.payconiq.rm.stocks.app.model.Stock;
import com.payconiq.rm.stocks.app.service.StockService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StockControllerTest {

    @Mock
    private StockService mockService;

    @InjectMocks
    private StockController controller;

    @Test
    public void testShouldGetAllStocks() {
        List<Stock> expected = Arrays.asList(Mockito.mock(Stock.class), Mockito.mock(Stock.class));
        Mockito.when(mockService.getAllStocks()).thenReturn(expected);

        ResponseEntity<Iterable<Stock>> allStocksResponse = controller.getAllStocks();

        Assert.assertNotNull(allStocksResponse);
        Assert.assertEquals(HttpStatus.OK, allStocksResponse.getStatusCode());
        Assert.assertNotNull(allStocksResponse.getBody());
        Assert.assertEquals(expected, allStocksResponse.getBody());

        Mockito.verify(mockService).getAllStocks();
    }

}