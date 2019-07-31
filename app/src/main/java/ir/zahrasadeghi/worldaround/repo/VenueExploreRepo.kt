package ir.zahrasadeghi.worldaround.repo

import ir.zahrasadeghi.worldaround.api.ApiResult
import ir.zahrasadeghi.worldaround.data.model.RecommendedItem
import ir.zahrasadeghi.worldaround.data.model.VenueDetail

interface VenueExploreRepo {

    suspend fun getVenues(
        targetLatLng: String,
        limit: Int,
        offset: Int
    ): List<RecommendedItem>

    suspend fun updateVenues(targetLatLng: String, initLoadSize: Int): ApiResult<Any>

    suspend fun getVenueDetail(venueId: String): ApiResult<VenueDetail>
}