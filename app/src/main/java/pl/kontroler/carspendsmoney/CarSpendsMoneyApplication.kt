package pl.kontroler.carspendsmoney

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

class CarSpendsMoneyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CarSpendsMoneyApplication)
            modules(AppModule().appModule)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
                Timber.plant(Timber.asTree())
        }
    }

}