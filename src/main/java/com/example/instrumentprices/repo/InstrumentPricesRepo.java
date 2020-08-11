package com.example.instrumentprices.repo;

import com.example.instrumentprices.domain.InstrumentPrice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public interface InstrumentPricesRepo {
    void store(InstrumentPrice instrumentPrice);

    InstrumentPrice getCurrentPrice(int vendorId, int instrumentId);

    List<InstrumentPrice> getInstrumentPrices(int instrumentId, LocalDateTime dateTime);

    List<InstrumentPrice> getVendorPrices(int vendorId, LocalDateTime dateTime);

    Map<Integer, Map<Integer, Queue<InstrumentPrice>>> getAllInstruments();
}
