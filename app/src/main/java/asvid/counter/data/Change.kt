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

}
