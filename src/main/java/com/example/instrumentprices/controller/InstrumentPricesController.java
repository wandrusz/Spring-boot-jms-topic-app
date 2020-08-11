package com.example.instrumentprices.controller;

import com.example.instrumentprices.domain.InstrumentPrice;
import com.example.instrumentprices.service.InstrumentPricesService;
import com.example.instrumentprices.service.InstrumentService;
import com.example.instrumentprices.service.VendorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Instrument publish controller which contains endpoints to publish and retrieve instrument prices. Using MVC design pattern.
 */
@RestController
public class InstrumentPricesController {

    private InstrumentPricesService instrumentPricesService;
    private VendorService vendorService;
    private InstrumentService instrumentService;

    public InstrumentPricesController(InstrumentPricesService instrumentPricesService, VendorService vendorService, InstrumentService instrumentService) {
        this.instrumentPricesService = instrumentPricesService;
        this.vendorService = vendorService;
        this.instrumentService = instrumentService;
    }

    @PostMapping("/vendors/{vendorId}/instruments/{instrumentId}/prices")
    public void publishInstrumentPrice(@RequestBody InstrumentPrice instrumentPrice, @PathVariable int vendorId, @PathVariable int instrumentId) {
        instrumentPrice.setInstrument(instrumentService.getInstrument(instrumentId));
        instrumentPrice.setVendor(vendorService.getVendor(vendorId));
        instrumentPricesService.publishPrice(instrumentPrice);
    }

    @GetMapping("/instruments/{instrumentId}/prices")
    public List<InstrumentPrice> getInstrumentPrices(InstrumentPrice instrumentPrice,
                                    @PathVariable int instrumentId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return instrumentPricesService.getInstrumentPrices(instrumentId, dateTime);
    }

    @GetMapping("/vendors/{vendorId}/prices")
    public List<InstrumentPrice> getAllPricesByVendor(InstrumentPrice instrumentPrice,
                                                 @PathVariable int vendorId,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return instrumentPricesService.getPricesByVendor(vendorId, dateTime);
    }
}
