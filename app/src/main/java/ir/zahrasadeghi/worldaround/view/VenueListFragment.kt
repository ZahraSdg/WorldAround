package ir.zahrasadeghi.worldaround.view

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import ir.zahrasadeghi.worldaround.R
import ir.zahrasadeghi.worldaround.databinding.FragmentVenueListBinding
import ir.zahrasadeghi.worldaround.model.NetworkState
import ir.zahrasadeghi.worldaround.util.AppConstants
import ir.zahrasadeghi.worldaround.view.adapter.VenueListAdapter
import ir.zahrasadeghi.worldaround.viewmodel.VenueListViewModel
import kotlinx.android.synthetic.main.fragment_venue_list.*
import org.koin.android.ext.android.inject


class VenueListFragment : BaseFragment<VenueListViewModel>() {

    companion object {
        fun newInstance() = VenueListFragment()

        private const val REQUEST_CHECK_SETTINGS = 300
    }

    override val layoutId: Int
        get() = R.layout.fragment_venue_list

    override val viewModel: VenueListViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (bindingView as FragmentVenueListBinding).viewmodel = viewModel
        venueListRv.adapter = VenueListAdapter()

        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkLocationSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.venue_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_refresh -> {
                swipeRefresh.isRefreshing = true
                viewModel.refresh()

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.handlePermissionResult(requestCode, grantResults)
    }

    override fun setupObservers() {
        super.setupObservers()

        val locationObserver = Observer<Location> { viewModel.checkLocation() }

        viewModel.locationSettingSatisfied.observe(this, Observer { satisfied ->
            satisfied?.let {
                if (!it) {
                    showLocationSettingDialog()
                }
            }
        })

        viewModel.locationPermissionGranted.observe(this, Observer { permissionGranted ->
            permissionGranted?.let {
                if (!it) {
                    requestPermission()
                } else {
                    viewModel.location.observe(this, locationObserver)
                }
            }
        })

        viewModel.needRefresh.observe(this, Observer {
            it?.let {
                if (it) {
                    viewModel.refresh()
                }
            }
        })

        viewModel.getState().observe(this, Observer {
            swipeRefresh.isRefreshing = it == NetworkState.LOADING
        })
    }

    private fun showLocationSettingDialog() {
        context?.let { it1 ->
            AlertDialog.Builder(it1)
                .setMessage(getString(R.string.location_setting_message))
                .setPositiveButton(getString(R.string.go_to_setting)) { _, _ ->
                    showLocationSetting()
                }
                .setNegativeButton(getString(R.string.dismiss)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .show()
        }
    }

    private fun showLocationSetting() {
        val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        this.startActivityForResult(i, REQUEST_CHECK_SETTINGS)
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            AppConstants.LOCATION_PERMISSIONS_REQUEST_CODE
        )
    }
}