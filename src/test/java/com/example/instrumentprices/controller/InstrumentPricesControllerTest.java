package com.example.instrumentprices.controller;

import com.example.instrumentprices.domain.InstrumentPrice;
import com.example.instrumentprices.service.InstrumentPricesService;
import com.example.instrumentprices.service.InstrumentService;
import com.example.instrumentprices.service.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.instrumentprices.TestData.*;
import static com.example.instrumentprices.TestUtils.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InstrumentPricesControllerTest {

    private MockMvc mvc;

    @InjectMocks
    InstrumentPricesController instrumentPricesController;

    @Mock
    private InstrumentPricesService instrumentPricesService;

    @Mock
    private VendorService vendorService;

    @Mock
    private InstrumentService instrumentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(instrumentPricesController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void publishInstrumentPrice() throws Exception {
        // Given
        var price = getInstrumentPrices().get(0);
        when(vendorService.getVendor(anyInt())).thenReturn(vendorA);
        when(instrumentService.getInstrument(anyInt())).thenReturn(instrumentA);

        // When
        mvc.perform(MockMvcRequestBuilders
                .post("/vendors/{vendorId}/instruments/{instrumentId}/prices", vendorA.getId(), instrumentA.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(price)))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("")));

        // Then
        verify(instrumentPricesService, times(1)).publishPrice(any(InstrumentPrice.class));
        verify(vendorService, times(1)).getVendor(vendorA.getId());
        verify(instrumentService, times(1)).getInstrument(instrumentA.getId());
    }

    // TODO: Add other endpoints tests
}
