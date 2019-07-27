package ir.zahrasadeghi.worldaround.api

import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import ir.zahrasadeghi.worldaround.data.model.RecommendedItem


val VenueDeserializer =
    JsonDeserializer { json, _, _ ->
        val bodyObject = json.asJsonObject
        val responseObj = bodyObject.getAsJsonObject("response")
        val groups = responseObj.getAsJsonArray("groups")
        var result: List<RecommendedItem> = ArrayList()
        groups.forEach { group ->
            val groupItem = group.asJsonObject
            val items = groupItem.getAsJsonArray("items")

            val listType = object : TypeToken<List<RecommendedItem>>() {

            }.type
            result = Gson().fromJson(items, listType) as List<RecommendedItem>
        }

        result
    }