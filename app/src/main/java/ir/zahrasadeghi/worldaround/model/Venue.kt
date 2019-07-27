package ir.zahrasadeghi.worldaround.model

import com.google.gson.JsonArray

data class Venue(
    var id: String,
    var name: String?,
    var location: Location,
    var categories: List<Category>,
    var photos: Media
)

//region LocationPoint
data class Location(
    val address: String?,
    val crossStreet: String,
    val lat: Double,
    val lng: Double,
    val labeledLatLngs: List<LabeledLatLngs>,
    val distance: Int,
    val postalCode: String,
    val cc: String,
    val city: String,
    val state: String,
    val country: String,
    val formattedAddress: List<String>
) {
    fun getFormattedDistance(): String {
        return String.format("%.2f", distance * 0.001)
    }
}

data class LabeledLatLngs(
    val label: String,
    val lat: Double,
    val lng: Double
)
//endregion

//region Category
data class Category(
    var id: String,
    var name: String,
    var pluralName: String,
    var shortName: String,
    var icon: Icon,
    var primary: Boolean
)

data class Icon(
    var prefix: String,
    var suffix: String
) {
    fun getFormattedIconUtl(): String {
        return prefix + "64" + suffix
    }
}
//endregion

data class Media(
    var count: Int,
    var groups: JsonArray
)