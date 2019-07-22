package ir.zahrasadeghi.worldaround.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = SupervisorJob()

    val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

}