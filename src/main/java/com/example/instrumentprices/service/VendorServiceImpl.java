package com.example.instrumentprices.service;

import com.example.instrumentprices.domain.Vendor;
import com.example.instrumentprices.repo.VendorRepo;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceImpl implements VendorService {

    VendorRepo vendorRepo;

    public VendorServiceImpl(VendorRepo vendorRepo) {
        this.vendorRepo = vendorRepo;
    }

    @Override
    public Vendor getVendor(int vendorId) {
        return vendorRepo.getById(vendorId);
    }

    @Override
    public void store(Vendor vendor) {
        vendorRepo.store(vendor);
    }
}
