package com.planet.assesment.utility;

import com.planet.assesment.repository.enums.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderUtilsTest {

    @Test
    void test_determineCountryByPhone() {
        String phoneNumber1 = "237 209993809";
        String phoneNumber2 = "258 852828436";
        String phoneNumber3 = "256 217813782";
        String phoneNumber4 = "251 543636241";
        String phoneNumber5 = "212 905608793";
        String phoneNumber6 = "200 209993809";
        Assertions.assertEquals(Country.CAMEROON, OrderUtils.determineCountryByPhone(phoneNumber1));
        Assertions.assertEquals(Country.MOZAMBIQUE, OrderUtils.determineCountryByPhone(phoneNumber2));
        Assertions.assertEquals(Country.UGANDA, OrderUtils.determineCountryByPhone(phoneNumber3));
        Assertions.assertEquals(Country.ETHIOPIA, OrderUtils.determineCountryByPhone(phoneNumber4));
        Assertions.assertEquals(Country.MOROCCO, OrderUtils.determineCountryByPhone(phoneNumber5));
        Assertions.assertEquals(Country.UNKNOWN, OrderUtils.determineCountryByPhone(phoneNumber6));

    }
}