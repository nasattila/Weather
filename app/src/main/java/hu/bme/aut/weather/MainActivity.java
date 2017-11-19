package hu.bme.aut.weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import io.realm.Realm;

import hu.bme.aut.weather.data.WeatherResult;
import hu.bme.aut.weather.data.City;
import hu.bme.aut.weather.network.AsyncTaskHttpGet;
import hu.bme.aut.weather.touch.ListItemTouchHelperCallback;
import hu.bme.aut.weather.adapter.ListRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, hu.bme.aut.weather.network.WeatherResultListener
{
    public static final String KEY_ITEM_ID = "KEY_ITEM_ID";
    public static final int REQUEST_CODE_VIEW = 101;

    private static ListRecyclerAdapter listRecyclerAdapter;
    private RecyclerView recyclerList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();

        setupUI();
    }

    private void setupUI()
    {
        setUpToolBar();
        setUpAddItemUI();
        setupRecyclerView();
    }

    private void setupRecyclerView()
    {
        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        recyclerList.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerList.setLayoutManager(layoutManager);

        listRecyclerAdapter = new ListRecyclerAdapter(this, ((MainApplication)getApplication()).getRealm());
        recyclerList.setAdapter(listRecyclerAdapter);

        // adding touch support
        ItemTouchHelper.Callback callback = new ListItemTouchHelperCallback(listRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerList);
    }

    private void setUpAddItemUI()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAddCityDialog();
            }
        });
    }

    private void setUpToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showAddCityDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ADD CITY");

        final EditText etCityName = new EditText(this);
        builder.setView(etCityName);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                new AsyncTaskHttpGet(MainActivity.this).execute("http://api.openweathermap.org/data/2.5/weather?units=metric&appid=f3d694bc3e1d44c1ed5a97bd1120e8fe&q=" + etCityName.getText());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void openViewActivity(int index, String itemID)
    {
        Intent startView = new Intent(this, ViewWeatherActivity.class);

        startView.putExtra(KEY_ITEM_ID, itemID);

        startActivityForResult(startView, REQUEST_CODE_VIEW);
    }

    private Realm getRealm()
    {
        return ((MainApplication)getApplication()).getRealm();
    }

    private void addCity(WeatherResult weatherResult)
    {
        getRealm().beginTransaction();
        City addCity = getRealm().createObject(City.class, weatherResult.getId().toString());
        addCity.setName(weatherResult.getName());
        getRealm().commitTransaction();

        // City addCity = new City(weatherResult.getId().toString());

        listRecyclerAdapter.add(addCity);
        listRecyclerAdapter.notifyItemInserted(0);
    }

    @Override
    public void weatherResultArrived(String rawJson)
    {
        try
        {
            Gson gson = new Gson();
            WeatherResult weatherResult = gson.fromJson(rawJson, WeatherResult.class);

            addCity(weatherResult);

            Snackbar.make(recyclerList, "The city has been added!", Snackbar.LENGTH_LONG).setAction("added", null).show();
        }
        catch (Exception exception)
        {
            Snackbar.make(recyclerList, "The city was not found and therefore, could not be added!", Snackbar.LENGTH_LONG).setAction("failed", null).show();

            exception.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        ((MainApplication)getApplication()).closeRealm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // inflate the menu: this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_add_new_item)
        {
            findViewById(R.id.fab).performClick();

            return true;
        }
        if (id == R.id.action_delete_all_items)
        {
            listRecyclerAdapter.dismissAllItems();

            Snackbar.make(recyclerList, "All of the cities have been removed!", Snackbar.LENGTH_LONG)
                    .setAction("delete_all", null).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add)
        {
            showAddCityDialog();
        }
        else if (id == R.id.nav_author)
        {
            Toast toast = Toast.makeText(MainActivity.this, getString(R.string.author), Toast.LENGTH_LONG);
            toast.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
