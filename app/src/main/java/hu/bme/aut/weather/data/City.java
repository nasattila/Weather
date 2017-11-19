package hu.bme.aut.weather.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class City extends RealmObject
{
    private String name;
    @PrimaryKey
    private String cityID;

    public City()
    {
    }

    public City(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCityID()
    {
        return cityID;
    }
}
