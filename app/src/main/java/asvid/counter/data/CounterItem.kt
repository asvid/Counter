package asvid.counter.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by adam on 15.01.17.
 */

open class CounterItem : RealmObject() {


    @PrimaryKey var id: Long? = null
    var name: String? = null
    var value: Int = 0
    var color: String? = null

    fun incrementValue() {
        value += 1
    }

    fun decrementValue() {
        value -= 1
    }

    override fun toString(): String {
        return "CounterItem(id=$id, name=$name, value=$value, color=$color)"
    }

}
