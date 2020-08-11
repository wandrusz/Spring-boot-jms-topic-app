package com.example.instrumentprices.service;

import com.example.instrumentprices.domain.Vendor;

public interface VendorService {
    Vendor getVendor(int vendorId);

    void store(Vendor vendor);
}
