package asvid.counter.data

import android.content.Context
import asvid.counter.widget.CounterWidget
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject

/**
 * Created by adam on 15.01.17.
 */
val ID = "id"

class Storage(context: Context) {
    private val realm: Realm

    init {
        Realm.init(context)
        realm = Realm.getInstance(
            RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build())
    }

    fun saveItem(item: CounterItem) {
        realm.beginTransaction()
        if (item.id == null) item.id = getId(CounterItem::class.java).toLong()
        realm.copyToRealmOrUpdate(item)
        realm.commitTransaction()
    }

    fun saveWidget(widget: CounterWidget) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(widget)
        realm.commitTransaction()
    }

    fun getWidget(id: Int): CounterWidget {
        return realm.copyFromRealm(
            realm.where(CounterWidget::class.java).equalTo("id", id).findFirst())
    }

    fun allItems(): MutableList<CounterItem> {
        return realm.copyFromRealm(realm.where(CounterItem::class.java).findAll())
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

    fun deleteWidget(widget: CounterWidget) {
        deleteObject(
            realm.where(CounterWidget::class.java).equalTo("id", widget.id?.toInt()!!).findFirst())
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

    fun getWidgetsOfCounter(counter: CounterItem): List<CounterWidget> {
        return realm.where(CounterWidget::class.java)
            .equalTo("counterItem.id", counter.id!!)
            .findAll()
    }

    fun getAllWidgets(): MutableList<CounterWidget> {
        return realm.where(CounterWidget::class.java).findAll()
    }
}
