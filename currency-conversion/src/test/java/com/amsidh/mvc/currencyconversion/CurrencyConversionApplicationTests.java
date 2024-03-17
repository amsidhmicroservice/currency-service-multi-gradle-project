

package com.amsidh.mvc.currencyconversion;

import com.amsidh.mvc.currencyconversion.controller.ConversionController;
import com.amsidh.mvc.service.InstanceInformationService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
class CurrencyConversionApplicationTests {

    private final ConversionController conversionController;
    private final InstanceInformationService instanceInformationService;


    @Test
    void contextLoads() {
        assertNotNull(conversionController);
        Assertions.assertNotNull(instanceInformationService);
    }

}


