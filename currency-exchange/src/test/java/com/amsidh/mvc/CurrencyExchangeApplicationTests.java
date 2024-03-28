
package com.amsidh.mvc;

import com.amsidh.mvc.controller.ExchangeController;
import com.amsidh.mvc.service.InstanceInformationService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
class CurrencyExchangeApplicationTests {

    private final ExchangeController exchangeController;
    private final InstanceInformationService instanceInformationService;

    @Test
    void contextLoads() {
        CurrencyExchangeApplication.main(new String[]{});
        assertNotNull(exchangeController);
        Assertions.assertNotNull(instanceInformationService);
    }

}

