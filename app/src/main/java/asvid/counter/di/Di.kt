package asvid.counter.di

import android.content.Context
import asvid.counter.Config
import asvid.counter.analytics.AnalyticsHelper
import kotlin.properties.Delegates

object Di {

  var context: Context by Delegates.notNull()

  fun set(context: Context) {
    Di.context = context
  }

  val analyticsHelper: AnalyticsHelper by lazy {
    AnalyticsHelper.Builder(context).setDebug(Config.isDevelop).build()
  }
}