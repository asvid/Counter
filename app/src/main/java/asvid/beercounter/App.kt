package asvid.beercounter

import android.app.Application

import asvid.beercounter.data.Storage

/**
 * Created by adam on 15.01.17.
 */

class App : Application() {

    var storage: Storage? = null
        private set

    override fun onCreate() {
        super.onCreate()
        storage = Storage(this)
    }
}
