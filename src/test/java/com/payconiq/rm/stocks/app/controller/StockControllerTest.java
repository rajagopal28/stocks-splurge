package com.payconiq.rm.stocks.app.controller;


import com.payconiq.rm.stocks.app.model.Stock;
import com.payconiq.rm.stocks.app.service.StockService;
import com.payconiq.rm.stocks.app.util.StockAppUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

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

    @Test
    public void testShouldPostNewStocks() {
        Stock expected = Mockito.mock(Stock.class);
        Mockito.when(mockService.addNewStock(expected)).thenReturn(expected);

        ResponseEntity<Stock> addStockResponse = controller.addNewStock(expected);

        Assert.assertNotNull(addStockResponse);
        Assert.assertEquals(HttpStatus.CREATED, addStockResponse.getStatusCode());
        Assert.assertNotNull(addStockResponse.getBody());
        Assert.assertEquals(expected, addStockResponse.getBody());

        Mockito.verify(mockService).addNewStock(expected);
    }

    @Test
    public void testShouldGetOneStock() {
        Stock expected = Mockito.mock(Stock.class);
        long id = 23;
        Mockito.when(mockService.getStockById(id)).thenReturn(expected);

        ResponseEntity<Stock> stockResponse = controller.getStock(id);

        Assert.assertNotNull(stockResponse);
        Assert.assertEquals(HttpStatus.OK, stockResponse.getStatusCode());
        Assert.assertNotNull(stockResponse.getBody());
        Assert.assertEquals(expected, stockResponse.getBody());

        Mockito.verify(mockService).getStockById(id);
    }

    @Test
    public void testShouldUpdateOneStock() {
        Stock expected = Mockito.mock(Stock.class);
        long id = 45;
        Mockito.when(mockService.updateStock(id, expected)).thenReturn(expected);

        ResponseEntity<Stock> stockResponse = controller.updateStock(id, expected);

        Assert.assertNotNull(stockResponse);
        Assert.assertEquals(HttpStatus.OK, stockResponse.getStatusCode());
        Assert.assertNotNull(stockResponse.getBody());
        Assert.assertEquals(expected, stockResponse.getBody());

        Mockito.verify(mockService).updateStock(id, expected);
    }

    @Test
    public void testShouldDeleteOneStock() {
        Stock expected = Mockito.mock(Stock.class);
        long id = 45;
        Mockito.when(mockService.deleteStock(id)).thenReturn(expected);

        ResponseEntity<Stock> stockResponse = controller.deleteStock(id);

        Assert.assertNotNull(stockResponse);
        Assert.assertEquals(HttpStatus.OK, stockResponse.getStatusCode());
        Assert.assertNotNull(stockResponse.getBody());
        Assert.assertEquals(expected, stockResponse.getBody());

        Mockito.verify(mockService).deleteStock(id);
    }


    @Test
    public void testShouldGetIndexHomePageModel() {
        ModelAndView homeModel = controller.welcome();
        Assert.assertNotNull(homeModel);
        Assert.assertEquals(StockAppUtil.VIEW_NAME_HOME, homeModel.getViewName());
    }
}