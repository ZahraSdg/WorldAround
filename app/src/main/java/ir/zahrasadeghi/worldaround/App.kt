package ir.zahrasadeghi.worldaround

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ir.zahrasadeghi.worldaround.viewmodel.MainViewModel
import ir.zahrasadeghi.worldaround.viewmodel.VenueListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Suppress("unused")
class App : Application() {


    private val appModule = module {

        viewModel { MainViewModel(androidApplication()) }
        viewModel { VenueListViewModel(androidApplication()) }
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}