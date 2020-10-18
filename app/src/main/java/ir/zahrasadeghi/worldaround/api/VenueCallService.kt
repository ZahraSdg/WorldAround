package ir.zahrasadeghi.worldaround.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VenueCallService {

    @GET("venues/explore")
    suspend fun getRecommendations(
        @Query("ll", encoded = true) targetLatLng: String,
        @Query("sortByDistance") sortByDistance: Int = 1,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<ResponseBody>

    @GET("venues/{id}")
    suspend fun getVenueDetail(@Path("id") id: String): Response<ResponseBody>
}