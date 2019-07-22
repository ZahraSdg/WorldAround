package ir.zahrasadeghi.worldaround.view

import androidx.lifecycle.ViewModelProviders
import ir.zahrasadeghi.worldaround.R
import ir.zahrasadeghi.worldaround.viewmodel.MainViewModel


class MainActivity : BaseActivity<MainViewModel>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override val layoutId: Int
        get() = R.layout.activity_main

}
