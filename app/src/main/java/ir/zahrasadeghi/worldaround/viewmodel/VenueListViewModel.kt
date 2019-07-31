package ir.zahrasadeghi.worldaround.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import ir.zahrasadeghi.worldaround.api.ApiResult
import ir.zahrasadeghi.worldaround.data.model.LiveLocation
import ir.zahrasadeghi.worldaround.data.model.NetworkState
import ir.zahrasadeghi.worldaround.data.model.RecommendedItem
import ir.zahrasadeghi.worldaround.datasource.VenuesDataSourceFactory
import ir.zahrasadeghi.worldaround.repo.LocationRepo
import ir.zahrasadeghi.worldaround.repo.VenueExploreRepo
import ir.zahrasadeghi.worldaround.util.AppConstants
import kotlinx.coroutines.launch


class VenueListViewModel(
    private val locationRepo: LocationRepo,
    private val venueExploreRepo: VenueExploreRepo,
    application: Application
) :
    BaseAndroidViewModel(application) {

    companion object {
        private const val MIN_PLACEMENT = 100
        private const val INITIAL_LOAD_SIZE = 20
        private const val PAGE_SIZE = 10
    }

    //region Private Parameters
    private val lastLocation: Location?
        get() = locationRepo.lastLocation

    private var venuesDataSourceFactory: VenuesDataSourceFactory? = null
    private val pagingNotInitialized: Boolean
        get() = venuesDataSourceFactory == null

    private var venueItems: LiveData<PagedList<RecommendedItem>> = MutableLiveData()
    private var state: LiveData<NetworkState> = MutableLiveData()
    private val firstTry: Boolean
        get() = lastLocation == null
    //endregion

    //region Public parameters
    private val _location: MutableLiveData<Location> = locationRepo.currentLocation
    val location: LiveData<Location> = _location

    private var _locationPermissionGranted = MutableLiveData<Boolean>()
    var locationPermissionGranted: LiveData<Boolean> = _locationPermissionGranted

    private var _locationSettingSatisfied = MutableLiveData<Boolean>()
    var locationSettingSatisfied: LiveData<Boolean> = _locationSettingSatisfied

    private val _updateAvailable = MutableLiveData<Boolean>()
    val updateAvailable: LiveData<Boolean> = _updateAvailable
    //endregion

    init {
        checkLocationPermission()
        initPaging()
    }

    //region Public functions
    fun getVenueItems(): LiveData<PagedList<RecommendedItem>> = venueItems

    fun getState(): LiveData<NetworkState> = state

    fun checkLocationSettings() {

        val client: SettingsClient = LocationServices.getSettingsClient(getApplication<Application>())
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings((_location as LiveLocation).locationSettingRequest)

        task.addOnSuccessListener {
            _locationSettingSatisfied.postValue(true)
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                _locationSettingSatisfied.postValue(false)
            }
        }
    }

    fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            AppConstants.LOCATION_PERMISSIONS_REQUEST_CODE -> {
                _locationPermissionGranted.value =
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    fun checkLocation() {

        _location.value?.let {
            if (firstTry) {
                updateLocation()
                refresh(true)
            } else if (lastLocation!!.distanceTo(it) > MIN_PLACEMENT) {
                refresh(false)
            }
        }
    }

    fun refresh(immediate: Boolean) {
        if (pagingNotInitialized) {
            initPaging()
        }
        updateVenues(immediate)
    }

    fun resetPaging() {
        venuesDataSourceFactory?.venuesSourceLiveData?.value?.invalidate()
        updateLocation()
    }
    //endregion

    //region Private functions
    private fun checkLocationPermission() {

        _locationPermissionGranted.value = ContextCompat.checkSelfPermission(
            getApplication<Application>().applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initPaging() {
        lastLocation?.let {

            val latLngStr = it.latitude.toString() + "," + it.longitude.toString()

            venuesDataSourceFactory =
                VenuesDataSourceFactory(latLngStr, venueExploreRepo)

            val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(INITIAL_LOAD_SIZE)
                .setPageSize(PAGE_SIZE)
                .build()

            venuesDataSourceFactory?.let { factory ->
                venueItems = LivePagedListBuilder(factory, pagedListConfig)
                    .build()

                state = Transformations.switchMap(factory.venuesSourceLiveData) { venueDataSource ->
                    venueDataSource.state
                }
            }
        }
    }

    private fun updateVenues(immediate: Boolean) {
        viewModelScope.launch {

            lastLocation?.let {

                val latLngStr = it.latitude.toString() + "," + it.longitude.toString()
                val result = venueExploreRepo.updateVenues(latLngStr, INITIAL_LOAD_SIZE)

                if (immediate && result is ApiResult.Success) {
                    resetPaging()
                }
                _updateAvailable.postValue(!immediate and (result is ApiResult.Success))
            }
        }
    }

    private fun updateLocation() {
        _location.value?.let {
            locationRepo.lastLocation = it
        }
    }
    //endregion
}