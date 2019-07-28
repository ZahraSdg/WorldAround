package ir.zahrasadeghi.worldaround.data.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class VenueDetail(

    val id: String,
    val name: String,
    val contact: JsonObject,
    val location: Location,
    val canonicalUrl: String,
    val categories: List<Category>,
    val verified: Boolean,
    val stats: JsonObject,
    val price: Price,
    val likes: Likes,
    val dislike: Boolean,
    val ok: Boolean,
    val delivery: JsonObject,
    val allowMenuUrlEdit: Boolean,
    val beenHere: JsonObject,
    val specials: JsonObject,
    val photos: Photos,
    val reasons: JsonObject,
    val hereNow: JsonObject,
    val createdAt: Int,
    val tips: JsonObject,
    val shortUrl: String,
    val timeZone: String,
    val listed: JsonObject,
    val pageUpdates: JsonObject,
    val inbox: JsonObject,
    val attributes: JsonObject,
    val bestPhoto: JsonObject,
    val colors: JsonObject
)

data class Price(

    val tier: Int,
    val message: String,
    val currency: String
)

data class Likes(

    val count: Int,
    val groups: JsonArray
)

data class Photos(

    val count: Int,
    val groups: List<Groups>,
    val summary: String
)

data class Groups(

    val type: String,
    val name: String,
    val summary: String,
    val count: Int,
    val items: List<Items>
)

data class Items(

    val displayName: String,
    val displayValue: String,
    val priceTier: Int
)