package com.example.instrumentprices.service;

import com.example.instrumentprices.domain.InstrumentPrice;
import com.example.instrumentprices.repo.InstrumentPricesRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.instrumentprices.TestData.getInstrumentPrices;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstrumentPricesServiceImplTest {

    @Mock
    private InstrumentPricesRepo instrumentPricesRepo;

    @Mock
    private PricesPublisher pricesPublisher;

    @InjectMocks
    private InstrumentPricesServiceImpl instrumentPricesService;

    @Test
    public void publishPriceWithoutInstrument() {
        // Given
        var price = getInstrumentPrices().get(0);
        price.setInstrument(null);

        // When
        assertThrows(NullPointerException.class, () -> instrumentPricesService.publishPrice(price));
    }

    @Test
    public void publishPriceWithoutVendor() {
        // Given
        var price = getInstrumentPrices().get(0);
        price.setVendor(null);

        // When
        assertThrows(NullPointerException.class, () -> instrumentPricesService.publishPrice(price));
    }

    @Test
    public void publishPrice() {
        // Given
        List<InstrumentPrice> prices = getInstrumentPrices();
        var price = prices.get(1);
        when(instrumentPricesRepo.getCurrentPrice(anyInt(), anyInt())).thenReturn(prices.get(0));

        // When
        instrumentPricesService.publishPrice(price);

        // Then
        verify(instrumentPricesRepo, times(1)).store(price);
        verify(pricesPublisher, times(1)).publish(price);
    }

    @Test
    public void publishFirstPriceForSpecifiedInstrumentAndVendor() {
        // Given
        List<InstrumentPrice> prices = getInstrumentPrices();
        var price = prices.get(1);
        when(instrumentPricesRepo.getCurrentPrice(anyInt(), anyInt())).thenReturn(null);

        // When
        instrumentPricesService.publishPrice(price);

        // Then
        verify(instrumentPricesRepo, times(1)).store(price);
        verify(pricesPublisher, times(1)).publish(price);
    }

    @Test
    public void publishPriceWithIllegalDate() {
        // Given
        List<InstrumentPrice> prices = getInstrumentPrices();
        var price = prices.get(0);
        when(instrumentPricesRepo.getCurrentPrice(anyInt(), anyInt())).thenReturn(prices.get(1));

        // When
        assertThrows(IllegalArgumentException.class, () -> instrumentPricesService.publishPrice(price));
    }
}
