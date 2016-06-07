package io.github.ohmylob.umbrella.alert.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Main {

    @SerializedName("temp")
    @Expose
    private Float temp;
    @SerializedName("pressure")
    @Expose
    private Float pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("temp_min")
    @Expose
    private Float tempMin;
    @SerializedName("temp_max")
    @Expose
    private Float tempMax;
    @SerializedName("sea_level")
    @Expose
    private Float seaLevel;
    @SerializedName("grnd_level")
    @Expose
    private Float grndLevel;

    /**
     * @return The temp
     */
    public Float getTemp() {
        return temp;
    }

    /**
     * @param temp The temp
     */
    public void setTemp(Float temp) {
        this.temp = temp;
    }

    /**
     * @return The pressure
     */
    public Float getPressure() {
        return pressure;
    }

    /**
     * @param pressure The pressure
     */
    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    /**
     * @return The humidity
     */
    public Integer getHumidity() {
        return humidity;
    }

    /**
     * @param humidity The humidity
     */
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    /**
     * @return The tempMin
     */
    public Float getTempMin() {
        return tempMin;
    }

    /**
     * @param tempMin The temp_min
     */
    public void setTempMin(Float tempMin) {
        this.tempMin = tempMin;
    }

    /**
     * @return The tempMax
     */
    public Float getTempMax() {
        return tempMax;
    }

    /**
     * @param tempMax The temp_max
     */
    public void setTempMax(Float tempMax) {
        this.tempMax = tempMax;
    }

    /**
     * @return The seaLevel
     */
    public Float getSeaLevel() {
        return seaLevel;
    }

    /**
     * @param seaLevel The sea_level
     */
    public void setSeaLevel(Float seaLevel) {
        this.seaLevel = seaLevel;
    }

    /**
     * @return The grndLevel
     */
    public Float getGrndLevel() {
        return grndLevel;
    }

    /**
     * @param grndLevel The grnd_level
     */
    public void setGrndLevel(Float grndLevel) {
        this.grndLevel = grndLevel;
    }

}
