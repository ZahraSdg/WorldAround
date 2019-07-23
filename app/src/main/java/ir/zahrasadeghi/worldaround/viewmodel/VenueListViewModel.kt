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
import ir.zahrasadeghi.worldaround.model.LiveLocation
import ir.zahrasadeghi.worldaround.repo.LocationRepo
import ir.zahrasadeghi.worldaround.util.AppConstants

class VenueListViewModel(locationRepoImpl: LocationRepo, application: Application) :
    BaseAndroidViewModel(application) {

    //region Public parameters
    private val _location: MutableLiveData<Location> = locationRepoImpl.currentLocation
    val location: LiveData<Location> = _location
    private var _locationPermissionGranted = MutableLiveData<Boolean>()
    var locationPermissionGranted: LiveData<Boolean> = _locationPermissionGranted
    private var _locationSettingSatisfied = MutableLiveData<Boolean>()
    var locationSettingSatisfied: LiveData<Boolean> = _locationSettingSatisfied
    //endregion

    init {
        checkLocationPermission()
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
            client.checkLocationSettings((location as LiveLocation).locationSettingRequest)

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
    //endregion
}