package com.payconiq.rm.stocks.app.service;

import com.payconiq.rm.stocks.app.model.Stock;
import com.payconiq.rm.stocks.app.repository.StockRepository;
import com.payconiq.rm.stocks.app.util.StockAppUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
public class StockServiceTest {

    @Mock
    private StockRepository mockRepository;

    @InjectMocks
    private StockService service;


    @Test
    public void testAddStockWithProperData() {
        double currentPrice = 23.45;
        String sname = "Stock1";
        Stock s1 = new Stock();
        s1.setCurrentPrice(currentPrice);
        s1.setName(sname);

        Mockito.when(mockRepository.findByName(sname)).thenReturn(Optional.empty());
        Mockito.when(mockRepository.save(s1)).thenReturn(s1);

        Stock actual = service.addNewStock(s1);
        Assert.assertEquals(s1, actual);
        Mockito.verify(mockRepository).findByName(sname);
        Mockito.verify(mockRepository).save(s1);
    }


    @Test
    public void testAddStockWithInvalidDataGetException() {
         String sname = "Stock1";
        Stock s1 = new Stock();
        s1.setName(sname);
        try {
           service.addNewStock(s1);
           Assert.fail("Should not come here!");
        } catch (Exception e) {
            // Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals(StockAppUtil.ERROR_INVALID_REQUEST, e.getMessage());
            Mockito.verify(mockRepository, Mockito.never()).findByName(sname);
            Mockito.verify(mockRepository, Mockito.never()).save(s1);
        }
    }

    @Test
    public void testAddStockWithValidDataButExistingStockGetException() {
        double currentPrice = 23.45;
        String sname = "Stock1";
        Stock s1 = new Stock();
        s1.setCurrentPrice(currentPrice);
        s1.setName(sname);

        Mockito.when(mockRepository.findByName(sname)).thenReturn(Optional.of(s1));

        try {
            service.addNewStock(s1);
            Assert.fail("Should not come here!");
        } catch (Exception e) {
            // Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals(StockAppUtil.FN_ERROR_STOCK_WITH_NAME_PRESENT.apply(sname), e.getMessage());
            Mockito.verify(mockRepository).findByName(sname);
            Mockito.verify(mockRepository, Mockito.never()).save(s1);
        }
    }

    @Test
    public void testGetAllStocks() {
        double currentPrice = 23.45;
        String sname = "Stock";
        Stock s1 = new Stock();
        s1.setCurrentPrice(currentPrice);
        s1.setName(sname+1);
        Stock s2 = new Stock();
        s2.setCurrentPrice(currentPrice);
        s2.setName(sname+2);

        List<Stock> expected = Arrays.asList(s1, s2);
        Mockito.when(mockRepository.findAll()).thenReturn(expected);

        Iterable<Stock> actual = service.getAllStocks();

        Assert.assertEquals(expected, actual);
        Mockito.verify(mockRepository).findAll();
    }


    @Test
    public void testGetStockByIdFoundStock() {
        long id = 12;
        Stock expected = new Stock();
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(expected));
        Stock actual = service.getStockById(id);
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testGetStockByIdNotFoundStockId() {
        long id = 44;
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.empty());
        try {
            service.getStockById(id);
            Assert.fail("Should not come here!");
        } catch (Exception e) {
            // Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals(StockAppUtil.FN_ERROR_STOCK_WITH_ID_NOT_PRESENT.apply(id), e.getMessage());
            Mockito.verify(mockRepository).findById(id);
        }
    }

}