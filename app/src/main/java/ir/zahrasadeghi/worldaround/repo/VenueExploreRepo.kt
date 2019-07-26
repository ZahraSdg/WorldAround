package ir.zahrasadeghi.worldaround.repo

import ir.zahrasadeghi.worldaround.model.ApiResult
import ir.zahrasadeghi.worldaround.model.RecommendedItem

interface VenueExploreRepo {

    suspend fun loadVenues(targetLatLng: String, radius: Long): ApiResult<List<RecommendedItem>>
}