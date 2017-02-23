package widget

import asvid.beercounter.data.CounterItem
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by adam on 15.01.17.
 */

open class CounterWidget : RealmObject() {

    @PrimaryKey
    open var id: Long = 0
    open var counterItem: CounterItem? = null
}
