package com.example.instrumentprices.controller;

import com.example.instrumentprices.domain.InstrumentPrice;
import com.example.instrumentprices.repo.InstrumentRepo;
import com.example.instrumentprices.repo.VendorRepo;
import com.example.instrumentprices.service.InstrumentPricesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.instrumentprices.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InstrumentPricesControllerTestIT {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private InstrumentRepo instrumentRepo;

    @Autowired
    InstrumentPricesService instrumentPricesService;


    @BeforeEach
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void testGetInstrumentPrices() throws Exception {
        // Given
        getVendors().forEach(vendor -> vendorRepo.store(vendor));
        getInstruments().forEach(instrument -> instrumentRepo.store(instrument));
        var instrumentPrices = getInstrumentPrices();
        instrumentPrices.forEach(price -> instrumentPricesService.publishPrice(price));
        var exPrices = List.of(
                instrumentPrices.get(2),
                instrumentPrices.get(6)
        );

        // When
        ResponseEntity<InstrumentPrice[]> response = template.getForEntity(base.toString() + "/instruments/{instrumentId}/prices?dateTime={dateTime}"
                , InstrumentPrice[].class
                , exPrices.get(0).getInstrument().getId()
                , exPrices.get(0).getDateFrom().format(DateTimeFormatter.ISO_DATE_TIME
        ));

        // Then
        InstrumentPrice[] prices = response.getBody();
        assertThat(prices).containsAll(exPrices);
    }
}
