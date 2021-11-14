package com.payconiq.rm.stocks.app;

import com.payconiq.rm.stocks.app.model.Stock;
import com.payconiq.rm.stocks.app.repository.StockRepository;
import com.payconiq.rm.stocks.app.util.StockAppUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StocksApplication.class)
@WebAppConfiguration
public class IntegrationTestSuite {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private GenericWebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void getContext() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Assert.assertNotNull(mockMvc);
    }

    @Test
    public void testCreateSimpleNewStock() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(StockAppUtil.ENDPOINT_STOCKS).
                    content("{\"name\":\"A hiker has got lost\",\"currentPrice\":12.45}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json("{\"name\":\"A hiker has got lost\",\"currentPrice\":12.45}"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testCreateSimpleNewStockFailForExistingName() {
        try {
            // insert one record
            String name = "Stock1";
            Stock s1 = new Stock(name, 10.25);
            Stock saved = stockRepository.save(s1);
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.post(StockAppUtil.ENDPOINT_STOCKS).
                    content("{\"name\":\""+name+"\",\"currentPrice\":34.45}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isAlreadyReported())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\": \"Stock with Name("+name+") already found!\"}"));
            // delete record
            stockRepository.delete(saved);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testCreateSimpleNewStockFailForMissingName() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(StockAppUtil.ENDPOINT_STOCKS).
                    content("{\"name\":\"\",\"currentPrice\":34.45}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\": \"Invalid request data passed!\"}"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testCreateSimpleNewStockFailForMissingPrice() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(StockAppUtil.ENDPOINT_STOCKS).
                    content("{\"name\":\"Stock23\"}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\": \"Invalid request data passed!\"}"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }


    @Test
    public void testCreateSimpleNewStockFailForMissingEverything() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(StockAppUtil.ENDPOINT_STOCKS).
                    content("{}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\": \"Invalid request data passed!\"}"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testGetAllStocksWithSomeDataExisting() {
        try {
            // insert records
            stockRepository.deleteAll();
            Iterable<Stock> stocksCreated = getStocksCreated(3);
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.get(StockAppUtil.ENDPOINT_STOCKS)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(getJsonStructureFromStocks(stocksCreated)));
            // delete records
            stockRepository.deleteAll(stocksCreated);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testUpdateExistingStockOutsideLockWindow() {
        try {
            // insert one record
            String name = "Stock1";
            String newName = "Stock23";
            Stock s1 = new Stock(name, 10.25);
            s1.setLastUpdated(Instant.now().minusSeconds(StockAppUtil.LOCK_WINDOW_IN_SECONDS+60).getEpochSecond());
            Stock saved = stockRepository.save(s1);
            saved.setName(newName);
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.put(StockAppUtil.ENDPOINT_STOCKS+"/"+ saved.getId())
                    .content("{\"name\":\""+newName+"\"}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(getJsonStructureFromStocks(Collections.singletonList(saved))));
            // delete record
            stockRepository.delete(saved);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testShouldNotUpdateExistingStockWithBlankRequest() {
        try {
            // insert one record
            String name = "Stock1";
            Stock s1 = new Stock(name, 10.25);
            s1.setLastUpdated(Instant.now().minusSeconds(StockAppUtil.LOCK_WINDOW_IN_SECONDS+60).getEpochSecond());
            Stock saved = stockRepository.save(s1);
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.put(StockAppUtil.ENDPOINT_STOCKS+"/"+ saved.getId())
                    .content("{}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\": \"Blank request cannot be updated!\"}"));
            // delete record
            stockRepository.delete(saved);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testFailToUpdateExistingStockWithinLockWindow() {
        try {
            // insert one record
            String name = "Stock1";
            String newName = "Stock23";
            Stock s1 = new Stock(name, 10.25);
            s1.setLastUpdated(Instant.now().minusSeconds(60).getEpochSecond());
            Stock saved = stockRepository.save(s1);
            saved.setName(newName);
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.put(StockAppUtil.ENDPOINT_STOCKS+"/"+ saved.getId())
                    .content("{\"name\":\""+newName+"\"}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\": \"Cannot manipulate stock within Lock window!\"}"));
            // delete record
            stockRepository.delete(saved);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testDeleteExistingStockOutsideLockWindow() {
        try {
            // insert one record
            String name = "Stock1";
            Stock s1 = new Stock(name, 10.25);
            s1.setLastUpdated(Instant.now().minusSeconds(StockAppUtil.LOCK_WINDOW_IN_SECONDS+60).getEpochSecond());
            Stock saved = stockRepository.save(s1);
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.delete(StockAppUtil.ENDPOINT_STOCKS+"/"+ saved.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(getJsonStructureFromStocks(Collections.singletonList(saved))));
            // delete record
            stockRepository.delete(saved);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testFailToDeleteExistingStockWithinLockWindow() {
        try {
            // insert one record
            String name = "Stock1";
            Stock s1 = new Stock(name, 10.25);
            s1.setLastUpdated(Instant.now().minusSeconds(60).getEpochSecond());
            Stock saved = stockRepository.save(s1);
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.delete(StockAppUtil.ENDPOINT_STOCKS+"/"+ saved.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\": \"Cannot manipulate stock within Lock window!\"}"));
            // delete record
            stockRepository.delete(saved);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testGetAvailableStockWithId() {
        try {
            // insert one record
            String name = "Stock1";
            Stock s1 = new Stock(name, 10.25);
            Stock saved = stockRepository.save(s1);
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.get(StockAppUtil.ENDPOINT_STOCKS+"/"+ saved.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(getJsonStructureFromStocks(Collections.singletonList(saved))));
            // delete record
            stockRepository.delete(saved);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testGetNotFoundStockWithId() {
        try {
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.get(StockAppUtil.ENDPOINT_STOCKS+"/99")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    @Test
    public void testGetIndexPageHTML() {
        try {
            // test the API
            mockMvc.perform(MockMvcRequestBuilders.get(StockAppUtil.ENDPOINT_HOME_INDEX)
                    .contentType(MediaType.TEXT_HTML))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not come here!!");
        }
    }

    private Iterable<Stock> getStocksCreated(int count) {
        List<Stock> stocks = IntStream.range(0, count)
                .mapToObj(i -> new Stock("Stock" + i, Math.random() * 9))
                .collect(Collectors.toList());
        return stockRepository.saveAll(stocks);
    }

    private String getJsonStructureFromStocks(Iterable<Stock> stocks) {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for(Stock s : stocks) {
            if(count > 0) {
                sb.append(",");
            }
            sb.append("{\"name\":\"");
            sb.append(s.getName());
            sb.append("\",\"currentPrice\":");
            sb.append(s.getCurrentPrice());
            sb.append("}");
            count++;
        }

        if(count > 1) {
            sb.insert(0, "[");
            sb.append("]");
        }
        return sb.toString();
    }
}