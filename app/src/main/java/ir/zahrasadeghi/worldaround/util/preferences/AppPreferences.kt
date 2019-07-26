package ir.zahrasadeghi.worldaround.util.preferences

import android.location.Location


object AppPreferences {

    private const val LAST_LOCATION_LAT = "location_lat_"
    private const val LAST_LOCATION_LNG = "location_lng_"

    var lastLocation: Location?
        set(value) {
            value?.let {
                Preferences.put(LAST_LOCATION_LAT, value.latitude.toString())
                Preferences.put(LAST_LOCATION_LNG, value.longitude.toString())
            }
        }
        get() {
            val location = Location("")
            location.latitude = Preferences.getString(LAST_LOCATION_LAT, "0").toDouble()
            location.longitude = Preferences.getString(LAST_LOCATION_LNG, "0").toDouble()

            return if (location.latitude.equals(0.0) || location.longitude.equals(0.0)) {
                null
            } else {
                location
            }
        }
}