package asvid.beercounter.data

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import widget.CounterWidget

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
        item.id = getId(CounterItem::class.java).toLong()
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

    fun increaseAndSave(item: CounterItem) {
        realm.beginTransaction()
        item.value = item.value!! + 1
        realm.copyToRealmOrUpdate(item)
        realm.commitTransaction()
    }

    fun decreaseAndSave(item: CounterItem) {
        realm.beginTransaction()
        item.value = item.value!! - 1
        realm.copyToRealmOrUpdate(item)
        realm.commitTransaction()
    }
}
