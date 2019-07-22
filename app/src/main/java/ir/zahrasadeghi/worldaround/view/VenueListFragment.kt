package ir.zahrasadeghi.worldaround.view

import ir.zahrasadeghi.worldaround.R
import ir.zahrasadeghi.worldaround.viewmodel.VenueListViewModel
import org.koin.android.ext.android.inject

class VenueListFragment : BaseFragment<VenueListViewModel>() {

    companion object {
        fun newInstance() = VenueListFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_venue_list

    override val viewModel: VenueListViewModel by inject()
}