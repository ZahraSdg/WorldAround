package ir.zahrasadeghi.worldaround.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VenueCallService {

    @GET("venues/explore")
    suspend fun getRecommendations(
        @Query("ll", encoded = true) targetLatLng: String,
        @Query("radius") radius: Long
    ): Response<ResponseBody>
}