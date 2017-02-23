package asvid.beercounter

import android.content.Context
import asvid.beercounter.data.Storage
import kotlin.properties.Delegates

object Di {

    var context: Context by  Delegates.notNull()

    fun set(context: Context) {
        this.context = context
    }

    val storage: Storage by lazy {
        Storage(context)
    }
}