package com.example.instrumentprices.service;

import com.example.instrumentprices.domain.Instrument;

public interface InstrumentService {
    Instrument getInstrument(int instrumentId);

    void store(Instrument instrument);
}
