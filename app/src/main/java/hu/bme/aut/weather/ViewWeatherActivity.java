package hu.bme.aut.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import io.realm.Realm;

import hu.bme.aut.weather.data.City;
import hu.bme.aut.weather.data.WeatherResult;
import hu.bme.aut.weather.network.AsyncTaskHttpGet;


public class ViewWeatherActivity extends AppCompatActivity implements hu.bme.aut.weather.network.WeatherResultListener, OnMapReadyCallback
{
    private City viewCity;
    private TextView nameText;
    private TextView tempText;
    private TextView pressureText;
    private TextView humidityText;
    private TextView tempMinText;
    private TextView tempMaxText;
    private TextView descriptionText;
    private ImageView image;
    private GoogleMap map;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_weather);

        if (getIntent().hasExtra(MainActivity.KEY_ITEM_ID))
        {
            String itemID = getIntent().getStringExtra(MainActivity.KEY_ITEM_ID);
            viewCity = getRealm().where(City.class)
                    .equalTo("cityID", itemID)
                    .findFirst();
        }

        nameText = (TextView) findViewById(R.id.tvName);
        tempText = (TextView) findViewById(R.id.tvTemp);
        pressureText = (TextView) findViewById(R.id.tvPressure);
        humidityText = (TextView) findViewById(R.id.tvHumidity);
        tempMinText = (TextView) findViewById(R.id.tvTempMin);
        tempMaxText = (TextView) findViewById(R.id.tvTempMax);
        descriptionText = (TextView) findViewById(R.id.tvDescription);
        image = (ImageView) findViewById(R.id.image);

        updateInfo();

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public Realm getRealm()
    {
        return ((MainApplication)getApplication()).getRealm();
    }

    private void updateInfo()
    {
        new AsyncTaskHttpGet(ViewWeatherActivity.this).execute("http://api.openweathermap.org/data/2.5/weather?units=metric&appid=f3d694bc3e1d44c1ed5a97bd1120e8fe&id=" + viewCity.getCityID());
    }

    @Override
    public void weatherResultArrived(String rawJson)
    {
        try
        {
            Gson gson = new Gson();
            WeatherResult weatherResult = gson.fromJson(rawJson, WeatherResult.class);

            nameText.setText(weatherResult.getName());
            tempText.setText(weatherResult.getTemp().toString() + "\u00b0 C");
            pressureText.setText(weatherResult.getPressure().toString() + " hPa");
            humidityText.setText(weatherResult.getHumidity().toString() + "%");
            tempMinText.setText(weatherResult.getTempMin().toString() + "\u00b0 C");
            tempMaxText.setText(weatherResult.getTempMax().toString() + "\u00b0 C");
            descriptionText.setText(weatherResult.getDescription() + " ");

            latitude = weatherResult.getLat();
            longitude = weatherResult.getLon();

            onMapReady(map);

            Glide.with(this).load("http://openweathermap.org/img/w/" + weatherResult.getIcon() + ".png").into(image);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;

        googleMap.clear();

        LatLng location = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions().position(location).title("marker"));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
