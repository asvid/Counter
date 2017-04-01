package asvid.counter.data.widget

import asvid.counter.data.counter.CounterItem
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date

open class CounterWidget : RealmObject() {

    @PrimaryKey
    open var id: Long? = null

    var color: Int? = null
    var counterItem: CounterItem? = null
    var createDate: Date = Date()
    var size: String? = null

    override fun toString(): String {
        return "CounterWidget(id=$id, color=$color, counterItem=$counterItem, createDate=$createDate, size=$size)"
    }
}