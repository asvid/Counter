package asvid.beercounter.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by adam on 15.01.17.
 */

open class CounterItem : RealmObject() {


    @PrimaryKey var id: Long? = null
    var name: String? = null
    var value: Int? = null
    var color: String? = null
}
