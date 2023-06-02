package com.planet.assesment.utility;

import com.planet.assesment.repository.enums.Country;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Utility class for all utility methods commonly used throught the application
 */
public class OrderUtils {

    /**
     * this method determines the Country name based on phone number
     *
     * @param phoneNumber
     * @return
     */
    public static Country determineCountryByPhone(String phoneNumber) {
        phoneNumber = "(" + phoneNumber.substring(0, 3) + ")" + phoneNumber.substring(3);
        String regexCameroon = "\\(237\\)\\ ?[2368]\\d{7,8}$";
        String regexEthiopia = "\\(251\\)\\ ?[1-59]\\d{8}$";
        String regexMorocco = "\\(212\\)\\ ?[5-9]\\d{8}$";
        String regexMozambique = "\\(258\\)\\ ?[28]\\d{7,8}$";
        String regexUganda = "\\(256\\)\\ ?\\d{9}$";

        if (phoneNumber.matches(regexCameroon)) {
            return Country.CAMEROON;
        } else if (phoneNumber.matches(regexEthiopia)) {
            return Country.ETHIOPIA;
        } else if (phoneNumber.matches(regexMorocco)) {
            return Country.MOROCCO;
        } else if (phoneNumber.matches(regexMozambique)) {
            return Country.MOZAMBIQUE;
        } else if (phoneNumber.matches(regexUganda)) {
            return Country.UGANDA;
        } else {
            return Country.UNKNOWN;
        }
    }
}
