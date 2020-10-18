package ir.zahrasadeghi.worldaround.api

import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import ir.zahrasadeghi.worldaround.data.model.VenueDetail


val VenueDetailDeserializer =
    JsonDeserializer { json, _, _ ->
        val bodyObject = json.asJsonObject
        val responseObj = bodyObject.getAsJsonObject("response")
        val venue = responseObj.getAsJsonObject("venue")

        val venueDetailType = object : TypeToken<VenueDetail>() {

        }.type

        Gson().fromJson(venue, venueDetailType) as VenueDetail
    }