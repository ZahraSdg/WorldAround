package ir.zahrasadeghi.worldaround.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import ir.zahrasadeghi.worldaround.model.ApiResult
import ir.zahrasadeghi.worldaround.model.LiveLocation
import ir.zahrasadeghi.worldaround.model.RecommendedItem
import ir.zahrasadeghi.worldaround.repo.LocationRepo
import ir.zahrasadeghi.worldaround.repo.VenueExploreRepo
import ir.zahrasadeghi.worldaround.util.AppConstants
import kotlinx.coroutines.launch
import timber.log.Timber

class VenueListViewModel(
    private val locationRepo: LocationRepo,
    private val venueExploreRepo: VenueExploreRepo,
    application: Application
) :
    BaseAndroidViewModel(application) {

    companion object {
        private const val MIN_PLACEMENT = 50
        private const val INIT_RADIUS = 500L
    }

    //region Private Parameters
    private val lastLocation: Location?
        get() = locationRepo.lastLocation
    //endregion

    //region Public parameters
    private val _location: MutableLiveData<Location> = locationRepo.currentLocation
    val location: LiveData<Location> = _location

    private var _locationPermissionGranted = MutableLiveData<Boolean>()
    var locationPermissionGranted: LiveData<Boolean> = _locationPermissionGranted

    private var _locationSettingSatisfied = MutableLiveData<Boolean>()
    var locationSettingSatisfied: LiveData<Boolean> = _locationSettingSatisfied

    private val _venueItems = MutableLiveData<List<RecommendedItem>>().apply { value = ArrayList() }
    val venueItems: LiveData<List<RecommendedItem>> = _venueItems

    private var _needRefresh = MutableLiveData<Boolean>().apply { value = false }
    val needRefresh: LiveData<Boolean> = _needRefresh

    private var _venueLoading = MutableLiveData<Boolean>().apply { value = true }
    val venueLoading: LiveData<Boolean> = _venueLoading
    //endregion

    init {
        checkLocationPermission()
        loadVenues()
    }

    //region Private functions
    private fun checkLocationPermission() {

        _locationPermissionGranted.value = ContextCompat.checkSelfPermission(
            getApplication<Application>().applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    //endregion

    //region Public functions
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
            if (lastLocation == null || lastLocation!!.distanceTo(it) > MIN_PLACEMENT) {
                locationRepo.lastLocation = it
                _needRefresh.value = true
            }
            _needRefresh.value = false
        }
    }

    fun loadVenues() = viewModelScope.launch {
        lastLocation?.let {

            _venueLoading.value = true

            val latLngStr = it.latitude.toString() + "," + it.longitude.toString()

            val result = venueExploreRepo.loadVenues(latLngStr, INIT_RADIUS)

            if (result is ApiResult.Success) {
                _venueItems.value = ArrayList(result.data)
            } else {
                Timber.d((result as ApiResult.Error).exception)
            }

            _venueLoading.value = false
        }
    }
    //endregion
}