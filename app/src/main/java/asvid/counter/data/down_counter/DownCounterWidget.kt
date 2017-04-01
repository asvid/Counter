package asvid.counter.data.down_counter

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date

open class DownCounterWidget : RealmObject() {

    @PrimaryKey var id: Long? = null
    var name: String? = null
    var date: Date? = null

    override fun toString(): String {
        return "DownCounterWidget(id=$id, name=$name, date=$date)"
    }

}