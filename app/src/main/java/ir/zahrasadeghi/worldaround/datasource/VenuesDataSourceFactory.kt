package ir.zahrasadeghi.worldaround.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import ir.zahrasadeghi.worldaround.model.RecommendedItem
import ir.zahrasadeghi.worldaround.repo.VenueExploreRepo

class VenuesDataSourceFactory(private val latLng: String, private val venueExploreRepo: VenueExploreRepo) :
    DataSource.Factory<Int, RecommendedItem>() {

    val venuesSourceLiveData = MutableLiveData<VenueDataSource>()

    override fun create(): DataSource<Int, RecommendedItem> {
        val venueDataSource = VenueDataSource(latLng, venueExploreRepo)
        venuesSourceLiveData.postValue(venueDataSource)
        return venueDataSource
    }
}