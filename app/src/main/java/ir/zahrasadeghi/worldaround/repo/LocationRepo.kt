package ir.zahrasadeghi.worldaround.repo

import ir.zahrasadeghi.worldaround.model.LiveLocation

interface LocationRepo {

    val currentLocation: LiveLocation
}