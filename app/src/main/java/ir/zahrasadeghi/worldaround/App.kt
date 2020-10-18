package ir.zahrasadeghi.worldaround

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ir.zahrasadeghi.worldaround.util.preferences.Preferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
class App : Application() {

    companion object {
        private const val PREFERENCES_KEY = "world_around_preferences"
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(viewModelModules, repositoryModules))
        }

        Preferences.init(applicationContext.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE))

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}