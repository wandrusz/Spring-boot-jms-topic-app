package com.example.instrumentprices.repo;

import com.example.instrumentprices.domain.Vendor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class VendorRepoImpl implements VendorRepo {

    /**
     * Store method can be access by multiple threads that's why counter has to be thread safe.
     */
    AtomicInteger counter = new AtomicInteger(1);

    /**
     * ConcurrentHashMap is used to not to block store in multithreading environment
     */
    Map<Integer, Vendor> vendors = new ConcurrentHashMap<>();

    /**
     * Stores vendor
     * @param vendor Vendor object
     */
    @Override
    public void store(Vendor vendor) {
        vendor.setId(counter.getAndIncrement());
        vendors.put(vendor.getId(), vendor);
    }

    /**
     * Gets vendor by id
     * @param id vendor id
     * @return vendor object
     */
    @Override
    public Vendor getById(int id) {
        return vendors.get(id);
    }
}
