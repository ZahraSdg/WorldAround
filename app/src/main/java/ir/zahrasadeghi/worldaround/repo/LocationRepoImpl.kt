package ir.zahrasadeghi.worldaround.repo

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import ir.zahrasadeghi.worldaround.data.model.LiveLocation
import ir.zahrasadeghi.worldaround.util.preferences.AppPreferences

class LocationRepoImpl(private val fusedLocationClient: FusedLocationProviderClient) : LocationRepo {

    override val currentLocation: LiveLocation by lazy { LiveLocation(fusedLocationClient) }

    override var lastLocation: Location?
        get() = AppPreferences.lastLocation
        set(value) {
            AppPreferences.lastLocation = value
        }
}