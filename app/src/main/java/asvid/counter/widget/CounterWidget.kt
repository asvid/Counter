package asvid.counter.widget

import asvid.counter.data.CounterItem
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date

/**
 * Created by adam on 15.01.17.
 */

open class CounterWidget : RealmObject() {

    @PrimaryKey
    open var id: Long? = null

    var color: Int? = null
    var counterItem: CounterItem? = null
    var createDate: Date = Date()

    override fun toString(): String {
        return "CounterWidget(id=$id, color=$color, counterItem=$counterItem, createDate=$createDate')"
    }

}
