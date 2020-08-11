package com.example.instrumentprices;

import com.example.instrumentprices.domain.Instrument;
import com.example.instrumentprices.domain.InstrumentPrice;
import com.example.instrumentprices.domain.Vendor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TestData {

    Vendor vendorA = new Vendor(1, "Vendor A");
    Vendor vendorB = new Vendor(1, "Vendor B");
    Instrument instrumentA = new Instrument(1, "Instrument A");
    Instrument instrumentB = new Instrument(2, "Instrument A");

    public static List<Vendor> getVendors() {
        return List.of(
                vendorA,
                vendorB
        );
    }

    public static List<Instrument> getInstruments() {
        return List.of(
                instrumentA,
                instrumentB
        );
    }

    public static List<InstrumentPrice> getInstrumentPrices() {
        LocalDateTime now = LocalDateTime.now();
        return List.of(
                new InstrumentPrice(vendorA, instrumentA, now.minusDays(7), BigDecimal.valueOf(1)),
                new InstrumentPrice(vendorA, instrumentA, now.minusDays(6), BigDecimal.valueOf(2)),
                new InstrumentPrice(vendorA, instrumentB, now.minusDays(3), BigDecimal.valueOf(3)),
                new InstrumentPrice(vendorA, instrumentB, now.minusDays(2), BigDecimal.valueOf(4)),
                new InstrumentPrice(vendorB, instrumentA, now.minusDays(7), BigDecimal.valueOf(5)),
                new InstrumentPrice(vendorB, instrumentA, now.minusDays(6), BigDecimal.valueOf(6)),
                new InstrumentPrice(vendorB, instrumentB, now.minusDays(3), BigDecimal.valueOf(7)),
                new InstrumentPrice(vendorB, instrumentB, now.minusDays(2), BigDecimal.valueOf(8))
        );
    }
}
