package ir.zahrasadeghi.worldaround

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ir.zahrasadeghi.worldaround.modules.repositoryModules
import ir.zahrasadeghi.worldaround.modules.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(viewModelModules, repositoryModules))
        }

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}