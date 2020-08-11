package com.example.instrumentprices.repo;

import com.example.instrumentprices.domain.Instrument;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InstrumentRepoImpl implements InstrumentRepo {

    /**
     * Store method can be access by multiple threads that's why counter has to be thread safe.
     */
    AtomicInteger counter = new AtomicInteger(1);

    /**
     * ConcurrentHashMap is used to not to block store in multithreading environment
     */
    Map<Integer, Instrument> instruments = new ConcurrentHashMap<>();

    @Override
    public void store(Instrument instrument) {
        instrument.setId(counter.getAndIncrement());
        instruments.put(instrument.getId(), instrument);
    }

    @Override
    public Instrument getById(int id) {
        return instruments.get(id);
    }
}
