package hu.bme.aut.weather.network;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class AsyncTaskHttpGet extends AsyncTask<String, Void, String>
{
    private WeatherResultListener weatherResultListener;

    public AsyncTaskHttpGet(WeatherResultListener weatherResultListener)
    {
        this.weatherResultListener = weatherResultListener;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params)
    {
        String result = "";
        HttpURLConnection conn = null;
        InputStream is = null;

        try
        {
            URL url = new URL(params[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int ch;

                while ((ch = is.read()) != -1)
                {
                    bos.write((byte)ch);
                }

                result = new String(bos.toByteArray());
            }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException exception)
                {
                    exception.printStackTrace();
                }
            }

            if (conn != null)
            {
                conn.disconnect();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result)
    {
        weatherResultListener.weatherResultArrived(result);
    }
}
