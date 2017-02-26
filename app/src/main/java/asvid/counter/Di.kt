package asvid.counter

import android.content.Context
import asvid.counter.data.Storage
import kotlin.properties.Delegates

object Di {

    var context: Context by Delegates.notNull()

    fun set(context: Context) {
        this.context = context
    }

    val storage: Storage by lazy {
        Storage(context)
    }

    val utils: Utils by lazy {
        Utils(context)
    }
}