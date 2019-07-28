package ir.zahrasadeghi.worldaround.repo

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ir.zahrasadeghi.worldaround.api.*
import ir.zahrasadeghi.worldaround.data.model.*
import ir.zahrasadeghi.worldaround.data.room.Venue
import ir.zahrasadeghi.worldaround.data.room.VenueDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VenueExploreRepoImpl(private val venueDao: VenueDao) : VenueExploreRepo {

    companion object {
        const val SORT_BY_DISTANCE = 1
    }

    private val venueCallService by lazy {
        APIService.retrofit.create(VenueCallService::class.java)
    }

    //region public functions
    override suspend fun loadVenues(targetLatLng: String, limit: Int, offset: Int): ApiResult<List<RecommendedItem>> {

        return withContext(Dispatchers.IO) {

            var result: List<RecommendedItem> = emptyList()

            val response = venueCallService.getRecommendations(targetLatLng, SORT_BY_DISTANCE, limit, offset)

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

    override suspend fun getVenues(targetLatLng: String, limit: Int, offset: Int): List<RecommendedItem> {

        val venus = venueDao.getAllVenues(limit, offset)

        if (venus.isNotEmpty()) {
            return venueDaoListToRecommendedItemList(venus)
        } else {
            try {
                val result = loadVenues(targetLatLng, limit, offset)
                if (result is ApiResult.Success) {
                    insertVenues(result.data)
                    return result.data
                }
            } catch (e: Exception) {
                return emptyList()
            }
        }
        return emptyList()
    }

    override suspend fun clearCache() {
        venueDao.clearTable()
    }

    override suspend fun updateVenues(targetLatLng: String, initLoadSize: Int): ApiResult<Any> {
        try {
            val result = loadVenues(targetLatLng, initLoadSize, 0)

            if (result is ApiResult.Success) {

                GlobalScope.launch(Dispatchers.IO) {
                    clearCache()
                    insertVenues(result.data)
                }
                return ApiResult.Success(Any())
            }
        } catch (e: Exception) {
            return ApiResult.Error(e)
        }
        return ApiResult.Error(Exception())
    }

    override suspend fun getVenueDetail(venueId: String): ApiResult<VenueDetail> {
        try {
            var result = VenueDetail()
            val response = venueCallService.getVenueDetail(venueId)

            val gsonBuilder = GsonBuilder()
            val listType = object : TypeToken<VenueDetail>() {}.type

            gsonBuilder.registerTypeAdapter(listType, VenueDetailDeserializer)

            val customGson = gsonBuilder.create()
            response.body()?.let {
                result = customGson.fromJson(it.string(), listType) as VenueDetail
            }

            return if (response.isSuccessful) {
                ApiResult.Success(result)
            } else {
                ApiResult.Error(Exception(response.errorBody()?.string()))
            }

        } catch (e: Exception) {
            return ApiResult.Error(e)
        }
    }
    //endregion

    //region private functions
    private fun insertVenues(items: List<RecommendedItem>) {
        GlobalScope.launch {
            items.forEach {
                val venue = it.venue
                venueDao.insert(
                    Venue(
                        venue.id,
                        venue.name.toString(),
                        venue.location.address.toString(),
                        venue.categories[0].icon.prefix,
                        venue.categories[0].icon.suffix,
                        venue.location.distance
                    )
                )
            }
        }
    }

    private fun venueDaoListToRecommendedItemList(venues: List<Venue>): List<RecommendedItem> {
        val recommendedItemList = ArrayList<RecommendedItem>()
        for (v in venues) {
            recommendedItemList.add(venueDaoToRecommendedItem(v))
        }
        return recommendedItemList
    }

    private fun venueDaoToRecommendedItem(venue: Venue): RecommendedItem {

        return RecommendedItem(
            Venue(
                venue.id,
                venue.name,
                Location(venue.address, venue.distance),
                listOf(Category(Icon(venue.catIconPrefix, venue.catIconSuffix))),
                Media()
            )
        )
    }
    //endregion
}