package hu.bme.aut.weather.network;


public interface WeatherResultListener
{
    public void weatherResultArrived(String rawJson);
}
