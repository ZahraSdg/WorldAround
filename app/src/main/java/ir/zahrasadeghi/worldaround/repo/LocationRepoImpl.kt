package ir.zahrasadeghi.worldaround.repo

import com.google.android.gms.location.FusedLocationProviderClient
import ir.zahrasadeghi.worldaround.model.LiveLocation

class LocationRepoImpl(private val fusedLocationClient: FusedLocationProviderClient) : LocationRepo {

    override val currentLocation: LiveLocation by lazy { LiveLocation(fusedLocationClient) }

}