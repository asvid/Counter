package widget;

import asvid.beercounter.App;
import asvid.beercounter.data.CounterItem;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by adam on 15.01.17.
 */

public class CounterWidget extends RealmObject {

    @PrimaryKey
    private long id;
    private CounterItem counterItem;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CounterItem getCounterItem() {
        return counterItem;
    }

    public void setCounterItem(CounterItem counterItem) {
        this.counterItem = counterItem;
    }
}
