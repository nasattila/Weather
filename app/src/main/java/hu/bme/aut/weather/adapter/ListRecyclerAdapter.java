package hu.bme.aut.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.RealmList;

import hu.bme.aut.weather.R;
import hu.bme.aut.weather.data.City;
import hu.bme.aut.weather.touch.ListTouchHelperAdapter;
import hu.bme.aut.weather.MainActivity;


public class ListRecyclerAdapter extends RecyclerView.Adapter<hu.bme.aut.weather.adapter.ListRecyclerAdapter.ViewHolder> implements ListTouchHelperAdapter
{
    private List<City> cities;
    private Context context;
    private Realm realmCities;

    public ListRecyclerAdapter(Context context, Realm realmCities)
    {
        this.context = context;
        this.realmCities = realmCities;

        RealmResults<City> itemResult = realmCities.where(City.class).findAll(); // .sort("name", Sort.ASCENDING);

        cities = new RealmList<City>();

        for (int i = 0; i < itemResult.size(); i = i + 1)
        {
            cities.add(itemResult.get(i));
        }
    }

    public List<City> getCities()
    {
        return cities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.tvName.setText(cities.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)context).openViewActivity(holder.getAdapterPosition(),
                        cities.get(holder.getAdapterPosition()).getCityID());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return cities.size();
    }

    public void add(City city)
    {
        cities.add(0, city);
    }

    @Override
    public void onItemDismiss(int position)
    {
        realmCities.beginTransaction();
        cities.get(position).deleteFromRealm();
        realmCities.commitTransaction();

        cities.remove(position);

        // refreshes the whole list
        // notifyDataSetChanged();
        // refreshes just the relevant part that has been deleted
        notifyItemRemoved(position);
    }

    public void dismissAllItems()
    {
        for (int i = 0; i < cities.size();)
        {
            realmCities.beginTransaction();
            cities.get(0).deleteFromRealm();
            realmCities.commitTransaction();

            cities.remove(0);

            notifyItemRemoved(0);
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition)
    {
        if (fromPosition < toPosition)
        {
            for (int i = fromPosition; i < toPosition; i = i + 1)
            {
                Collections.swap(cities, i, i + 1);
            }
        }
        else
        {
            for (int i = fromPosition; i > toPosition; i = i - 1)
            {
                Collections.swap(cities, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvName;

        public ViewHolder(View itemView)
        {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }
}