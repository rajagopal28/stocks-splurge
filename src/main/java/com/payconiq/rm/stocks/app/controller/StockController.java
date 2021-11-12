package com.payconiq.rm.stocks.app.controller;

import com.payconiq.rm.stocks.app.model.Stock;
import com.payconiq.rm.stocks.app.service.StockService;
import com.payconiq.rm.stocks.app.util.StockAppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class StockController {

    @Autowired
    StockService service;

    @GetMapping(value = StockAppUtil.ENDPOINT_STOCKS)
    public ResponseEntity<Iterable<Stock>> getAllStocks() {
        return ResponseEntity.ok(service.getAllStocks());
    }

}
