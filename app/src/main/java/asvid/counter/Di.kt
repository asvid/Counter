package asvid.counter

import android.content.Context
import asvid.counter.data.Storage
import com.aswiderski.frigo.Config
import com.aswiderski.frigo.analytics.AnalyticsHelper
import kotlin.properties.Delegates

object Di {

    var context: Context by Delegates.notNull()

    fun set(context: Context) {
        this.context = context
    }

    val storage: Storage by lazy {
        Storage(context)
    }

    val analyticsHelper: AnalyticsHelper by lazy {
        AnalyticsHelper.Builder(context).setDebug(Config.isDevelop).build()
    }
}