package ir.zahrasadeghi.worldaround.view

import android.os.Bundle
import android.view.View
import ir.zahrasadeghi.worldaround.R
import ir.zahrasadeghi.worldaround.databinding.FragmentVenueDetailBinding
import ir.zahrasadeghi.worldaround.util.AppConstants
import ir.zahrasadeghi.worldaround.viewmodel.VenueDetailViewModel
import org.koin.android.ext.android.inject

class VenueDetailFragment : BaseFragment<VenueDetailViewModel>() {

    companion object {
        fun newInstance(args: Bundle) = VenueDetailFragment().apply { arguments = args }
    }

    override val layoutId: Int
        get() = R.layout.fragment_venue_detail

    override val viewModel: VenueDetailViewModel by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (bindingView as FragmentVenueDetailBinding).viewmodel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(AppConstants.VENUE_ID_BUNDLE)?.let {
            viewModel.loadVenueDetail(it)
        }
    }
}