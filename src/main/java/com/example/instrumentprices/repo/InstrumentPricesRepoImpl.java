package com.example.instrumentprices.repo;

import com.example.instrumentprices.domain.InstrumentPrice;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;

@Repository
@EnableScheduling
public class InstrumentPricesRepoImpl implements InstrumentPricesRepo {

    public static final int ONE_DAY = 24 * 3600 * 1000;
    /**
     * By using ConcurrentHashMap our store can be modified by multiple Threads in same time.
     * Queue allows for better manipulation of data. In situation where prices are changing very often it is better choice then ArrayList.
     * Data are grouped by Instruments and Vendors for faster selection.
     */
    private Map<Integer, Map<Integer, Queue<InstrumentPrice>>> pricesByInstrument = new ConcurrentHashMap<>();

    @Override
    public void store(InstrumentPrice instrumentPrice) {
        var instrumentPrices = pricesByInstrument.merge(instrumentPrice.getInstrument().getId(), new ConcurrentHashMap<>(), (oldVal, newVal) -> oldVal);
        var vendorInstrumentPrices = instrumentPrices.merge(instrumentPrice.getVendor().getId(), new ConcurrentLinkedQueue<InstrumentPrice>(), (oldVal, newVal) -> oldVal);
        var currentPrice = vendorInstrumentPrices.peek();
        if (currentPrice != null) {
            currentPrice.setDateTo(instrumentPrice.getDateFrom());
        }

        vendorInstrumentPrices.add(instrumentPrice);
    }

    @Override
    public InstrumentPrice getCurrentPrice(int vendorId, int instrumentId) {
        return pricesByInstrument.getOrDefault(instrumentId, emptyMap()).getOrDefault(vendorId, new LinkedList<>()).peek();
    }

    @Override
    public List<InstrumentPrice> getInstrumentPrices(int instrumentId, LocalDateTime dateTime) {
        return pricesByInstrument.getOrDefault(instrumentId, emptyMap()).entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .filter(getPriceByDateTime(dateTime))
                .collect(Collectors.toList());
    }

    private Predicate<InstrumentPrice> getPriceByDateTime(LocalDateTime dateTime) {
        return price -> price.getDateFrom().compareTo(dateTime) <= 0 && (price.getDateTo() == null || dateTime.isBefore(price.getDateTo()));
    }

    @Override
    public List<InstrumentPrice> getVendorPrices(int vendorId, LocalDateTime dateTime) {
        return pricesByInstrument.entrySet().stream()
                .flatMap(entry -> entry.getValue().get(vendorId).stream())
                .filter(getPriceByDateTime(dateTime))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Map<Integer, Queue<InstrumentPrice>>> getAllInstruments() {
        return pricesByInstrument;
    }

    /**
     * Clean cache from prices older then 30 days. Alternatively we can use cache like Redis and specify TTL to 30 days.
     */
    @Scheduled(fixedRate = ONE_DAY)
    public void cleanCache() {
        var thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        pricesByInstrument.entrySet().stream()
                .flatMap(instrumentPrices -> instrumentPrices.getValue().entrySet().stream())
                .forEach(vendorPrices -> vendorPrices.getValue().removeIf(price -> price.getCreated().isBefore(thirtyDaysAgo)));
    }
}
