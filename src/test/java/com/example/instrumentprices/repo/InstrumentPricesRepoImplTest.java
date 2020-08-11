package com.example.instrumentprices.repo;

import com.example.instrumentprices.TestData;
import com.example.instrumentprices.domain.InstrumentPrice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.instrumentprices.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentPricesRepoImplTest {

    private InstrumentPricesRepoImpl instrumentPricesRepo = new InstrumentPricesRepoImpl();

    @Test
    public void storeInstrumentPrice() {
        // Given
        var instrument = TestData.getInstruments().get(0);
        var vendor = TestData.getVendors().get(0);
        InstrumentPrice instrumentPrice = new InstrumentPrice(vendor, instrument, LocalDateTime.now(), BigDecimal.valueOf(1));

        // When
        instrumentPricesRepo.store(instrumentPrice);

        // Then
        InstrumentPrice actualPrice = instrumentPricesRepo.getCurrentPrice(vendor.getId(), instrument.getId());
        assertThat(actualPrice).isEqualTo(instrumentPrice);
    }

    @Test
    public void testGetInstrumentPrices() {
        // Given
        List<InstrumentPrice> instrumentPrices = getInstrumentPrices();
        instrumentPrices.forEach(price -> instrumentPricesRepo.store(price));
        var exPrices = List.of(
                instrumentPrices.get(2),
                instrumentPrices.get(6)
        );

        // When
        var actualPrices = instrumentPricesRepo.getInstrumentPrices(exPrices.get(0).getInstrument().getId(), exPrices.get(0).getDateFrom());

        // Then
        assertThat(actualPrices).containsAll(exPrices);
    }

    @Test
    public void testGetInstrumentPrices_forNotDefinePeriod() {
        // Given
        List<InstrumentPrice> instrumentPrices = getInstrumentPrices();
        instrumentPrices.forEach(price -> instrumentPricesRepo.store(price));

        // When
        var actualPrices = instrumentPricesRepo.getInstrumentPrices(instrumentA.getId(), LocalDateTime.now().minusDays(33));

        // Then
        assertThat(actualPrices).hasSize(0);
    }

    @Test
    public void testGetVendorPrices() {
        // Given
        List<InstrumentPrice> instrumentPrices = getInstrumentPrices();
        instrumentPrices.forEach(price -> instrumentPricesRepo.store(price));
        var exPrices = List.of(
                instrumentPrices.get(1),
                instrumentPrices.get(2)
        );

        // When
        var actualPrices = instrumentPricesRepo.getVendorPrices(exPrices.get(1).getVendor().getId(), exPrices.get(1).getDateFrom());

        // Then
        assertThat(actualPrices).containsAll(exPrices);
    }

    @Test
    public void testGetVendorPrices_forNotDefinePeriod() {
        // Given
        List<InstrumentPrice> instrumentPrices = getInstrumentPrices();
        instrumentPrices.forEach(price -> instrumentPricesRepo.store(price));

        // When
        var actualPrices = instrumentPricesRepo.getVendorPrices(vendorA.getId(), LocalDateTime.now().minusDays(33));

        // Then
        assertThat(actualPrices).hasSize(0);
    }

    @Test
    public void cleanCache() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<InstrumentPrice> prices = getInstrumentPrices();
        prices.forEach(price -> {
            price.setCreated(now.minusDays(33));
        });
        prices.get(0).setCreated(now);
        prices.get(1).setCreated(now);
        prices.forEach(price -> instrumentPricesRepo.store(price));

        // When
        instrumentPricesRepo.cleanCache();

        // Then
        var allPrices = instrumentPricesRepo.getAllInstruments();
        var allPricesAsList = allPrices.entrySet().stream()
                .flatMap(instrumentEntries -> instrumentEntries.getValue().entrySet().stream())
                .flatMap(vendorEntries -> vendorEntries.getValue().stream())
                .collect(Collectors.toList());
        assertThat(allPricesAsList).containsExactly(prices.get(0), prices.get(1));
    }
}
