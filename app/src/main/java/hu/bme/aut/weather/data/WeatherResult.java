package hu.bme.aut.weather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WeatherResult
{
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather;
    @SerializedName("coord")
    @Expose
    private Coord coord;

    public WeatherResult(String name, Integer id, Main main, List<Weather> weather, Coord coord)
    {
        this.name = name;
        this.id = id;
        this.main = main;
        this.weather = weather;
        this.coord = coord;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Double getTemp()
    {
        return main.getTemp();
    }

    public void setTemp(Double temp)
    {
        main.setTemp(temp);
    }

    public Double getPressure()
    {
        return main.getPressure();
    }

    public void setPressure(Double pressure)
    {
        main.setPressure(pressure);
    }

    public Double getHumidity()
    {
        return main.getHumidity();
    }

    public void setHumidity(Double humidity)
    {
        main.setHumidity(humidity);
    }

    public Double getTempMin()
    {
        return main.getTempMin();
    }

    public void setTempMin(Double temp_min)
    {
        main.setTempMin(temp_min);
    }

    public Double getTempMax()
    {
        return main.getTempMax();
    }

    public void setTempMax(Double temp_max)
    {
        main.setTempMax(temp_max);
    }

    public String getDescription()
    {
        return weather.get(0).getDescription();
    }

    public void setDescription(String description)
    {
        weather.get(0).setDescription(description);
    }

    public String getIcon()
    {
        return weather.get(0).getIcon();
    }

    public void setIcon(String icon)
    {
        weather.get(0).setIcon(icon);
    }

    public Double getLon()
    {
        return coord.getLon();
    }

    public void setLon(Double lon)
    {
        coord.setLon(lon);
    }

    public Double getLat()
    {
        return coord.getLat();
    }

    public void setLat(Double lat)
    {
        coord.setLat(lat);
    }
}
