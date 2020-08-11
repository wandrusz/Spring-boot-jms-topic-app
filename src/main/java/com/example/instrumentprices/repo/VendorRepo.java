package com.example.instrumentprices.repo;

import com.example.instrumentprices.domain.Vendor;

public interface VendorRepo {
    void store(Vendor vendor);

    Vendor getById(int id);
}
