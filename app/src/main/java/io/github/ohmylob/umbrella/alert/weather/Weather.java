package io.github.ohmylob.umbrella.alert.weather;

public class Weather {

    private final String conditions;
    private final String currentTemperature;
    private final String minTemperature;
    private final String maxTemperature;

    public Weather(String conditions, String currentTemperature, String minTemperature, String maxTemperature) {
        this.conditions = conditions;
        this.currentTemperature = currentTemperature;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public String getConditions() {
        return conditions;
    }

    public String getCurrentTemperature() {
        return currentTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }
}
