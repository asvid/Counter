package asvid.counter.data

import io.realm.RealmObject
import java.util.Date

open class Change : RealmObject() {

    var preValue: Int? = null
    var postValue: Int? = null
    var date: Date? = null

    override fun toString(): String {
        return "Change(preValue=$preValue, postValue=$postValue, date=$date)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Change

        if (preValue != other.preValue) return false
        if (postValue != other.postValue) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = preValue ?: 0
        result = 31 * result + (postValue ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        return result
    }

}
