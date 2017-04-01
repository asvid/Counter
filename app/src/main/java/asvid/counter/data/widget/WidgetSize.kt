package asvid.counter.data.widget

import io.realm.RealmObject

open class WidgetSize : RealmObject() {

    var heightFactor: Int? = null
    var widthFactor: Int? = null

    fun getStringSize(): String = "${heightFactor}x$widthFactor"

    override fun toString(): String {
        return "WidgetSize(heightFactor=$heightFactor, widthFactor=$widthFactor)"
    }

}