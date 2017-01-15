package asvid.beercounter;

import android.app.Application;

import asvid.beercounter.data.Storage;

/**
 * Created by adam on 15.01.17.
 */

public class App extends Application {

    private Storage storage;

    @Override
    public void onCreate() {
        super.onCreate();
        storage = new Storage(this);
    }

    public Storage getStorage() {
        return storage;
    }
}
