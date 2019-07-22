package ir.zahrasadeghi.worldaround.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel


abstract class BaseFragment<T : ViewModel> : Fragment() {

    protected lateinit var bindingView: ViewDataBinding

    @get:LayoutRes
    abstract val layoutId: Int

    protected abstract val viewModel: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        bindingView = DataBindingUtil.inflate(inflater, layoutId, container, false)
        bindingView.lifecycleOwner = this

        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    protected open fun setupObservers() {
    }
}