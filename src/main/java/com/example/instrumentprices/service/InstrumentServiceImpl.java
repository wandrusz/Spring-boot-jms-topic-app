package com.example.instrumentprices.service;

import com.example.instrumentprices.domain.Instrument;
import com.example.instrumentprices.repo.InstrumentRepo;
import org.springframework.stereotype.Service;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    InstrumentRepo instrumentRepo;

    public InstrumentServiceImpl(InstrumentRepo instrumentRepo) {
        this.instrumentRepo = instrumentRepo;
    }

    @Override
    public Instrument getInstrument(int instrumentId) {
        return instrumentRepo.getById(instrumentId);
    }

    @Override
    public void store(Instrument instrument) {
        instrumentRepo.store(instrument);
    }
}
