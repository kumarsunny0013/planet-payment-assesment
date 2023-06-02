package com.planet.assesment.repository;

/**
 * ENUM class for Country and its code
 */
public enum Country {
    CAMEROON("+237"),

    ETHIOPIA("+251"),

    MOROCCO("+212"),

    MOZAMBIQUE("+258"),

    UGANDA("+256"),

    UNKNOWN("UNKNOWN");

    private final String countryCode;

    Country(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public static Country getByCountryCode(String countryCode) {
        for (Country country : Country.values()) {
            if (country.getCountryCode().equals(countryCode)) {
                return country;
            }
        }
        return null;
    }
}
