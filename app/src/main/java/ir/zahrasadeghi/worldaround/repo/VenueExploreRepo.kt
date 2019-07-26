package ir.zahrasadeghi.worldaround.repo

import ir.zahrasadeghi.worldaround.model.ApiResult
import ir.zahrasadeghi.worldaround.model.RecommendedItem

interface VenueExploreRepo {

    suspend fun loadVenues(
        targetLatLng: String,
        limit: Int,
        offset: Int
    ): ApiResult<List<RecommendedItem>>
}