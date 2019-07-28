package ir.zahrasadeghi.worldaround.data.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class VenueDetail(

    val id: String,
    val name: String,
    val contact: Contact,
    val location: Location,
    val canonicalUrl: String,
    val categories: List<Category>,
    val verified: Boolean,
    val stats: JsonObject,
    val price: Price,
    val likes: Likes,
    val dislike: Boolean,
    val ok: Boolean,
    val rating: Float,
    val delivery: JsonObject,
    val allowMenuUrlEdit: Boolean,
    val beenHere: JsonObject,
    val specials: JsonObject,
    val photos: JsonObject,
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
    val bestPhoto: BestPhoto,
    val colors: JsonObject
) {
    constructor() : this(
        "", "", Contact(), Location(), "", emptyList(), false, JsonObject(), Price(),
        Likes(), false, false, 0f, JsonObject
            (), false, JsonObject(), JsonObject(), JsonObject(), JsonObject(), JsonObject(), 0, JsonObject(),
        "", "", JsonObject(), JsonObject(), JsonObject(), JsonObject(), BestPhoto(), JsonObject()
    )
}

data class Price(

    val tier: Int,
    val message: String,
    val currency: String
) {
    constructor() : this(0, "", "")
}

data class Likes(

    val count: Int,
    val groups: JsonArray
) {
    constructor() : this(0, JsonArray())
}

data class BestPhoto(

    val id: String,
    val createdAt: Int,
    val source: JsonObject,
    val prefix: String,
    val suffix: String,
    val width: Int,
    val height: Int,
    val visibility: String
) {
    constructor() : this("", 0, JsonObject(), "", "", 0, 0, "")

    fun getFormattedPhotoUrl(): String {
        return prefix + "original" + suffix
    }
}

data class Contact(

    val phone: String,
    val formattedPhone: String
) {
    constructor() : this("", "")
}