package com.example.instrumentprices.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InstrumentPrice {

    private Vendor vendor;

    private Instrument instrument;

    /**
     * By using two date field dateFrom and dateTo it is easier to find price of for specified date and time.
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateFrom;

    /**
     * Optional property. If dateTo is null it means it is current price.
     */
    @JsonIgnore
    private LocalDateTime dateTo;

    @JsonIgnore
    private LocalDateTime created = LocalDateTime.now();

    private BigDecimal value;

    public InstrumentPrice() {
    }

    public InstrumentPrice(Vendor vendor, Instrument instrument, LocalDateTime dateFrom, BigDecimal value) {
        this.vendor = vendor;
        this.instrument = instrument;
        this.dateFrom = dateFrom;
        this.value = value;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentPrice that = (InstrumentPrice) o;
        return Objects.equal(vendor, that.vendor) &&
                Objects.equal(instrument, that.instrument) &&
                Objects.equal(dateFrom, that.dateFrom) &&
                Objects.equal(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(vendor, instrument, dateFrom, value);
    }
}
