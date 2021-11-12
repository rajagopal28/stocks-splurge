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

import java.time.Instant;
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

    @Test
    public void testDeleteStockByIdFoundStockNotInLockWindow() {
        long id = 12;
        long timePast10Mins = Instant.now()
                .minusSeconds(StockAppUtil.LOCK_WINDOW_IN_SECONDS*2)
                .getEpochSecond();
        Stock expected = new Stock();
        expected.setLastUpdated(timePast10Mins);
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(expected));
        Stock actual = service.deleteStock(id);
        Assert.assertEquals(expected, actual);
        Mockito.verify(mockRepository).delete(expected);
    }

    @Test
    public void testDeleteFailStockByIdNotFoundStockId() {
        long id = 34;
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.empty());
        try {
            service.deleteStock(id);
            Assert.fail("Should not come here!");
        } catch (Exception e) {
            // Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals(StockAppUtil.FN_ERROR_STOCK_WITH_ID_NOT_PRESENT.apply(id), e.getMessage());
            Mockito.verify(mockRepository).findById(id);
            Mockito.verify(mockRepository, Mockito.never()).delete(Mockito.any(Stock.class));
        }
    }

    @Test
    public void testDeleteFailStockByIdFoundStockWithingLockWindow() {
        long id = 65;
        long timePast2Mins = Instant.now()
                .minusSeconds(StockAppUtil.LOCK_WINDOW_IN_SECONDS/2)
                .getEpochSecond();
        Stock expected = new Stock();
        expected.setLastUpdated(timePast2Mins);
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(expected));
        try {
            service.deleteStock(id);
            Assert.fail("Should not come here!");
        } catch (Exception e) {
            // Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals(StockAppUtil.ERROR_LOCK_WINDOW_ENABLED, e.getMessage());
            Mockito.verify(mockRepository).findById(id);
            Mockito.verify(mockRepository, Mockito.never()).delete(expected);
        }
    }

    @Test
    public void testUpdateStockByIdFoundStockNotInLockWindowNameUpdate() {
        long id = 12;
        long timePast10Mins = Instant.now()
                .minusSeconds(StockAppUtil.LOCK_WINDOW_IN_SECONDS*2)
                .getEpochSecond();

        Stock expected = Mockito.mock(Stock.class);
        Mockito.when(expected.getLastUpdated()).thenReturn(timePast10Mins);
        Stock updated = new Stock();
        updated.setName("Stock2");
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(expected));
        Mockito.when(mockRepository.save(expected)).thenReturn(expected);
        Stock actual = service.updateStock(id, updated);
        Assert.assertEquals(actual, expected);
        Mockito.verify(mockRepository).save(expected);
        Mockito.verify(mockRepository).findById(id);
        Mockito.verify(expected).setName("Stock2");
    }


    @Test
    public void testUpdateStockByIdFoundStockNotInLockWindowPriceUpdate() {
        long id = 12;
        long timePast10Mins = Instant.now()
                .minusSeconds(StockAppUtil.LOCK_WINDOW_IN_SECONDS*2)
                .getEpochSecond();

        Stock expected = Mockito.mock(Stock.class);
        Mockito.when(expected.getLastUpdated()).thenReturn(timePast10Mins);
        Stock updated = new Stock();
        updated.setCurrentPrice(23.45);
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(expected));
        Mockito.when(mockRepository.save(expected)).thenReturn(expected);
        Stock actual = service.updateStock(id, updated);
        Assert.assertEquals(actual, expected);
        Mockito.verify(mockRepository).save(expected);
        Mockito.verify(mockRepository).findById(id);
        Mockito.verify(expected).setCurrentPrice(23.45);
    }

    @Test
    public void testUpdateStockByIdShouldFailForNotFoundStockId() {
        long id = 34;
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.empty());
        try {
            Stock expected = Mockito.mock(Stock.class);
            Mockito.when(expected.getName()).thenReturn("Stock1");
            service.updateStock(id, expected);
            Assert.fail("Should not come here!");
        } catch (Exception e) {
            // Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals(StockAppUtil.FN_ERROR_STOCK_WITH_ID_NOT_PRESENT.apply(id), e.getMessage());
            Mockito.verify(mockRepository).findById(id);
            Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any(Stock.class));
        }
    }

    @Test
    public void testUpdateStockByIdShouldFailForInvalidStockRequest() {
        long id = 34;
        try {
            Stock expected = Mockito.mock(Stock.class);
            service.updateStock(id, expected);
            Mockito.when(expected.getName()).thenReturn("Stock1");
            Assert.fail("Should not come here!");
        } catch (Exception e) {
            // Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals(StockAppUtil.ERROR_INVALID_UPDATE_REQUEST, e.getMessage());
            Mockito.verify(mockRepository, Mockito.never()).findById(id);
            Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any(Stock.class));
        }
    }

    @Test
    public void testUpdateStockByIdShouldFailForValidStockRequestWithinLockWindow() {
        long id = 34;
        try {
            long timePast2Mins = Instant.now()
                    .minusSeconds(StockAppUtil.LOCK_WINDOW_IN_SECONDS/2)
                    .getEpochSecond();

            Stock expected = Mockito.mock(Stock.class);
            Mockito.when(expected.getName()).thenReturn("Stock1");
            Mockito.when(expected.getLastUpdated()).thenReturn(timePast2Mins);

            Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(expected));
            service.updateStock(id, expected);
            Assert.fail("Should not come here!");
        } catch (Exception e) {
            // Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals(StockAppUtil.ERROR_LOCK_WINDOW_ENABLED, e.getMessage());
            Mockito.verify(mockRepository).findById(id);
            Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any(Stock.class));
        }
    }
}