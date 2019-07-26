package ir.zahrasadeghi.worldaround.repo

import android.location.Location
import ir.zahrasadeghi.worldaround.model.LiveLocation

interface LocationRepo {

    val currentLocation: LiveLocation
    var lastLocation: Location?
}