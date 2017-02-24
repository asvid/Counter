package widget

import asvid.counter.data.CounterItem
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by adam on 15.01.17.
 */

open class CounterWidget : RealmObject() {

    @PrimaryKey
    open var id: Long? = null

    open var counterItem: CounterItem? = null

    override fun toString(): String {
        return "CounterWidget(id=$id, counterItem=$counterItem)"
    }

}
