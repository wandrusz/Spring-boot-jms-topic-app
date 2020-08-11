package com.example.instrumentprices.service;

import com.example.instrumentprices.domain.InstrumentPrice;

import java.time.LocalDateTime;
import java.util.List;

public interface InstrumentPricesService {
    void publishPrice(InstrumentPrice instrumentPrice);

    List<InstrumentPrice> getInstrumentPrices(int instrumentId, LocalDateTime dateTime);

    List<InstrumentPrice> getPricesByVendor(int vendorId, LocalDateTime dateTime);
}
