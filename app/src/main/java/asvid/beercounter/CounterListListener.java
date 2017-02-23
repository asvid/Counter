package asvid.beercounter;

import asvid.beercounter.data.CounterItem;

public interface CounterListListener {
    void onItemDelete(CounterItem item);

    void onItemClicked(CounterItem item);

    void onItemIncrement(CounterItem item);

    void onItemDecrement(CounterItem item);
}
