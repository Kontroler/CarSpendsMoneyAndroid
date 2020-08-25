package pl.kontroler.carspendsmoney

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class CarSpendsMoneyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@CarSpendsMoneyApplication)
            modules(AppModule().appModule)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
                Timber.plant(Timber.asTree())
        }
        AndroidThreeTen.init(this);
    }

}