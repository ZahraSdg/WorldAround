package ir.zahrasadeghi.worldaround.view

import android.os.Bundle
import ir.zahrasadeghi.worldaround.R
import ir.zahrasadeghi.worldaround.viewmodel.MainViewModel
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity<MainViewModel>() {

    override val viewModel: MainViewModel by inject()

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, VenueListFragment.newInstance()).commit()
    }
}
