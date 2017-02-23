package asvid.beercounter

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Created by adam on 15.01.17.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        init()
    }

    private fun init() {
        initLeakCanary()
        initTimber()
        Di.set(this)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }
}
