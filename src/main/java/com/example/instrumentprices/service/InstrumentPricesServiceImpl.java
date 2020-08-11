package com.example.instrumentprices.service;

import com.example.instrumentprices.domain.InstrumentPrice;
import com.example.instrumentprices.repo.InstrumentPricesRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class InstrumentPricesServiceImpl implements InstrumentPricesService {

    private InstrumentPricesRepo instrumentPricesRepo;
    private PricesPublisher pricesPublisher;

    public InstrumentPricesServiceImpl(InstrumentPricesRepo instrumentPricesRepo, PricesPublisher pricesPublisher) {
        this.instrumentPricesRepo = instrumentPricesRepo;
        this.pricesPublisher = pricesPublisher;
    }

    /**
     * Publishes price in local cache and sends it to downstream systems.
     * @param instrumentPrice price object
     */
    @Override
    public void publishPrice(InstrumentPrice instrumentPrice) {
        checkNotNull(instrumentPrice.getVendor(), "Instrument price cannot be added to not existing Vendor");
        checkNotNull(instrumentPrice.getInstrument(), "Instrument price cannot be added to not existing Instrument");
        InstrumentPrice currentPrice = instrumentPricesRepo.getCurrentPrice(instrumentPrice.getVendor().getId(), instrumentPrice.getInstrument().getId());
        checkArgument(currentPrice == null || currentPrice.getDateFrom().isBefore(instrumentPrice.getDateFrom()),
                "Instrument: {} - price dateFrom: {} is incorrect. You must not modify historical prices",
                instrumentPrice.getInstrument().getId(), instrumentPrice.getDateFrom());
        instrumentPrice.setDateTo(null);
        instrumentPricesRepo.store(instrumentPrice);
        pricesPublisher.publish(instrumentPrice);
    }

    /**
     * Gets instrument prices from different vendors and specified date
     * @param instrumentId instrument id
     * @param dateTime date time object
     * @return list of instrument prices
     */
    @Override
    public List<InstrumentPrice> getInstrumentPrices(int instrumentId, LocalDateTime dateTime) {
        return instrumentPricesRepo.getInstrumentPrices(instrumentId, dateTime);
    }

    /**
     * Gets all instruments prices from specified vendor
     * @param vendorId vendor id
     * @param dateTime date time object
     * @return lost of instrument prices
     */
    @Override
    public List<InstrumentPrice> getPricesByVendor(int vendorId, LocalDateTime dateTime) {
        return instrumentPricesRepo.getVendorPrices(vendorId, dateTime);
    }
}
