package hu.bme.aut.weather.touch;


public interface ListTouchHelperAdapter
{
    void onItemDismiss(int position);

    void onItemMove(int fromPosition, int toPosition);
}
