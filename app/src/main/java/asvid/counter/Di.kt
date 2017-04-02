package asvid.counter

import android.content.Context
import android.os.Build
import asvid.counter.analytics.AnalyticsHelper
import asvid.counter.data.Storage
import java.util.Locale
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

    val locale: Locale by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }
}