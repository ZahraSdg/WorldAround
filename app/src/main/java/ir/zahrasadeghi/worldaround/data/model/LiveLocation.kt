package ir.zahrasadeghi.worldaround.data.model

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import timber.log.Timber

class LiveLocation(private val fusedLocationClient: FusedLocationProviderClient) : MutableLiveData<Location>() {

    companion object {

        private const val TIME_INTERVAL = 2000L
        private const val DISPLACEMENT = 5F
    }

    private val locationRequest = LocationRequest.create()?.apply {
        interval = TIME_INTERVAL
        fastestInterval = TIME_INTERVAL / 2
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        smallestDisplacement = DISPLACEMENT
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val location = locationResult?.locations?.firstOrNull()
            Timber.d("onLocationResult$location")
            location?.let { value = it }
        }
    }

    val locationSettingRequest = locationRequest?.let {
        LocationSettingsRequest.Builder()
            .addLocationRequest(it)
    }?.build()

    @SuppressLint("MissingPermission")
    override fun onActive() {
        // Request updates if there’s someone observing
        if (hasActiveObservers()) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onInactive() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}