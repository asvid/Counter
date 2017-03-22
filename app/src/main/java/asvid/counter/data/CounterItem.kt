package asvid.counter.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date

/**
 * Created by adam on 15.01.17.
 */
open class CounterItem : RealmObject() {


    @PrimaryKey var id: Long? = null
    var name: String? = null
    var value: Int = 0

    var changes: RealmList<Change> = RealmList()

    fun incrementValue() {
        val change = Change()
        change.date = Date()
        change.preValue = value
        value += 1
        change.postValue = value
        changes.add(change)
    }

    fun decrementValue() {
        val change = Change()
        change.date = Date()
        change.preValue = value
        value -= 1
        change.postValue = value
        changes.add(change)
    }

    override fun toString(): String {
        return "CounterItem(id=$id, name=$name, value=$value, changes=$changes)"
    }

}
