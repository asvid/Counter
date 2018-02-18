package asvid.counter.analytics

import android.app.Activity
import android.content.Context
import android.os.Bundle
import asvid.counter.analytics.enums.Action
import asvid.counter.analytics.enums.Category
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsHelper private constructor(builder: Builder) {
  private var analytics = FirebaseAnalytics.getInstance(builder.context)!!
  private var isDebug = builder.isDebug

  init {
    analytics.setAnalyticsCollectionEnabled(!isDebug)
  }

  fun sendEvent(category: Category, action: Action, label: String) {
    if (!isDebug) {
      val params = Bundle()
      params.putString(FirebaseAnalytics.Param.CONTENT, "$action")
      params.putString(FirebaseAnalytics.Param.VALUE, label)
      analytics.logEvent(category.toString(), params)
    }
  }

  fun sendScreenName(activity: Activity, screen: String) {
    if (!isDebug) {
      analytics.setCurrentScreen(activity, screen, null)
    }
  }

  class Builder(val context: Context) {
    var isDebug = true

    fun setDebug(isDevelop: Boolean): Builder {
      this.isDebug = isDevelop
      return this
    }

    fun build(): AnalyticsHelper {
      return AnalyticsHelper(this)
    }
  }
}