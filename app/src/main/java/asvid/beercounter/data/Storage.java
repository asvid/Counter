package asvid.beercounter.data;

import android.content.Context;
import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import widget.CounterWidget;

/**
 * Created by adam on 15.01.17.
 */

public class Storage {

    public static final String ID = "id";
    private Realm realm;

    public Storage(Context context) {
        Log.d("Storage", "starting storage");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(
                context).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfiguration);
    }

    public void saveItem(CounterItem item) {
        realm.beginTransaction();
        item.setId(getId(CounterItem.class));
        realm.copyToRealmOrUpdate(item);
        realm.commitTransaction();
    }

    public void saveWidget(CounterWidget widget) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(widget);
        realm.commitTransaction();
    }

    public CounterItem getItem(int id) {
        return realm.where(CounterItem.class).equalTo("id", id).findFirst();
    }

    public List<CounterItem> getAllItems() {
        return realm.where(CounterItem.class).findAll();
    }

    public <C extends RealmObject> int getId(Class<C> objectClass) {
        Number currentIdNum = realm.where(objectClass).max(ID);
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }

        return nextId;
    }
}
