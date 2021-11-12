package com.payconiq.rm.stocks.app.controller;

import com.payconiq.rm.stocks.app.model.Stock;
import com.payconiq.rm.stocks.app.service.StockService;
import com.payconiq.rm.stocks.app.util.StockAppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Slf4j
public class StockController {

    @Autowired
    StockService service;

    @GetMapping(value = StockAppUtil.ENDPOINT_STOCKS)
    public ResponseEntity<Iterable<Stock>> getAllStocks() {
        return ResponseEntity.ok(service.getAllStocks());
    }

    @PostMapping(value = StockAppUtil.ENDPOINT_STOCKS)
    public ResponseEntity<Stock> addNewStock(@RequestBody Stock stock) {
        Stock newStock = service.addNewStock(stock);
        return ResponseEntity.created(URI.create(StockAppUtil.ENDPOINT_STOCKS+newStock.getId())).body(newStock);
    }

    @GetMapping(value = StockAppUtil.ENDPOINT_STOCKS_WITH_ID)
    public ResponseEntity<Stock> getStock(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(service.getStockById(id));
    }

    @PutMapping(value = StockAppUtil.ENDPOINT_STOCKS_WITH_ID)
    public ResponseEntity<Stock> updateStock(@PathVariable(name = "id") Long id,@RequestBody Stock stock) {
        return ResponseEntity.ok(service.updateStock(id, stock));
    }

    @DeleteMapping(value = StockAppUtil.ENDPOINT_STOCKS_WITH_ID)
    public ResponseEntity<Stock> deleteStock(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(service.deleteStock(id));
    }

}
