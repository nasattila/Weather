package hu.bme.aut.weather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Main
{
    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("pressure")
    @Expose
    private Double pressure;
    @SerializedName("humidity")
    @Expose
    private Double humidity;
    @SerializedName("temp_min")
    @Expose
    private Double temp_min;
    @SerializedName("temp_max")
    @Expose
    private Double temp_max;

    public Main(Double temp, Double pressure, Double humidity, Double temp_min, Double temp_max)
    {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
    }

    public Double getTemp()
    {
        return temp;
    }

    public void setTemp(Double temp)
    {
        this.temp = temp;
    }

    public Double getPressure()
    {
        return pressure;
    }

    public void setPressure(Double pressure)
    {
        this.pressure = pressure;
    }

    public Double getHumidity()
    {
        return humidity;
    }

    public void setHumidity(Double humidity)
    {
        this.humidity = humidity;
    }

    public Double getTempMin()
    {
        return temp_min;
    }

    public void setTempMin(Double temp_min)
    {
        this.temp_min = temp_min;
    }

    public Double getTempMax()
    {
        return temp_max;
    }

    public void setTempMax(Double temp_max)
    {
        this.temp_max = temp_max;
    }
}
