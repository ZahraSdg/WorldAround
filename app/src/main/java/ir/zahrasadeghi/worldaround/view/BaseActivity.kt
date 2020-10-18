package ir.zahrasadeghi.worldaround.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel


abstract class BaseActivity<T : ViewModel> : AppCompatActivity() {

    protected lateinit var bindingView: ViewDataBinding

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected abstract val viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingView = DataBindingUtil.setContentView(this, layoutId)
        bindingView.lifecycleOwner = this

        setupObservers()
    }

    protected open fun setupObservers() {}
}
