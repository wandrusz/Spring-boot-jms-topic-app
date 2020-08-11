package com.example.instrumentprices.repo;

import com.example.instrumentprices.domain.Instrument;

public interface InstrumentRepo {
    void store(Instrument instrument);

    Instrument getById(int id);
}
