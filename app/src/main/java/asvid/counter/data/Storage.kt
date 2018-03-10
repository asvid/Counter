package asvid.counter.data

import android.content.Context
import asvid.counter.BuildConfig
import asvid.counter.data.counter.CounterItem
import asvid.counter.data.data_migration.Migration
import asvid.counter.data.down_counter.DownCounterWidget
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject

val ID = "id"

class Storage(context: Context) {
    private val realm: Realm

    init {
        Realm.init(context)
        realm = Realm.getInstance(
            RealmConfiguration.Builder()
                .schemaVersion(BuildConfig.REALM_SCHEMA_VERSION)
                .migration(Migration())
                .build())
    }

    fun saveItem(item: CounterItem) {
        realm.beginTransaction()
        if (item.id == null) item.id = getId(CounterItem::class.java).toLong()
        realm.copyToRealmOrUpdate(item)
        realm.commitTransaction()
    }

    fun saveDownCounter(widget: DownCounterWidget) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(widget)
        realm.commitTransaction()
    }

    fun getDownCounterWidget(id: Int): DownCounterWidget {
        return realm.copyFromRealm(
            realm.where(DownCounterWidget::class.java).equalTo("id", id).findFirst())
    }

    fun getAllDownCounterWidget(): MutableList<DownCounterWidget> {
        return realm.copyFromRealm(
            realm.where(DownCounterWidget::class.java).findAll())
    }

    fun allItems(): MutableList<CounterItem> {
        val findAll = realm.where(CounterItem::class.java).findAll()
        return realm.copyFromRealm(findAll)
    }

    fun <C : RealmObject> getId(objectClass: Class<C>): Int {
        val currentIdNum = realm.where(objectClass).max(ID)
        val nextId: Int
        if (currentIdNum == null) {
            nextId = 1
        } else {
            nextId = currentIdNum.toInt() + 1
        }

        return nextId
    }

    fun getCounterItem(id: Long): CounterItem {
        return realm.copyFromRealm(
            realm.where(CounterItem::class.java).equalTo("id", id).findFirst())
    }

    fun deleteCounter(counter: CounterItem) {
        deleteObject(
            realm.where(CounterItem::class.java).equalTo("id", counter.id?.toInt()!!).findFirst())
    }

    fun deleteObject(realmObject: RealmObject) {
        if (realmObject.isManaged) {
            realm.beginTransaction()
            realmObject.deleteFromRealm()
            realm.commitTransaction()
        }
    }
}
