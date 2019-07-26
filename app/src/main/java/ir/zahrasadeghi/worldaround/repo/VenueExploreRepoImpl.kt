package ir.zahrasadeghi.worldaround.repo

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ir.zahrasadeghi.worldaround.api.APIService
import ir.zahrasadeghi.worldaround.api.VenueCallService
import ir.zahrasadeghi.worldaround.api.VenueDeserializer
import ir.zahrasadeghi.worldaround.model.ApiResult
import ir.zahrasadeghi.worldaround.model.RecommendedItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VenueExploreRepoImpl : VenueExploreRepo {

    private val venueCallService by lazy {
        APIService.retrofit.create(VenueCallService::class.java)
    }

    override suspend fun loadVenues(
        targetLatLng: String,
        radius: Long
    ): ApiResult<List<RecommendedItem>> {

        return withContext(Dispatchers.IO) {

            var result: List<RecommendedItem> = emptyList()

            val response = venueCallService.getRecommendations(targetLatLng, radius)

            val gsonBuilder = GsonBuilder()
            val listType = object : TypeToken<List<RecommendedItem>>() {}.type

            gsonBuilder.registerTypeAdapter(listType, VenueDeserializer)

            val customGson = gsonBuilder.create()
            response.body()?.let {
                result = customGson.fromJson(it.string(), listType) as List<RecommendedItem>
            }

            return@withContext if (response.isSuccessful) {
                ApiResult.Success(result)
            } else {
                ApiResult.Error(Exception(response.errorBody()?.string()))
            }
        }
    }
}