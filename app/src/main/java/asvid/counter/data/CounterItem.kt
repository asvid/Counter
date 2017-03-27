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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as CounterItem

        if (id != other.id) return false
        if (name != other.name) return false
        if (value != other.value) return false
        if (changes != other.changes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + value
        result = 31 * result + changes.hashCode()
        return result
    }

}
