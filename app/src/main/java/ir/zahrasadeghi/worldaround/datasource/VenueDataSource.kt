package ir.zahrasadeghi.worldaround.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import ir.zahrasadeghi.worldaround.data.model.NetworkState
import ir.zahrasadeghi.worldaround.data.model.NetworkState.COMPLETE
import ir.zahrasadeghi.worldaround.data.model.NetworkState.LOADING
import ir.zahrasadeghi.worldaround.data.model.RecommendedItem
import ir.zahrasadeghi.worldaround.repo.VenueExploreRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VenueDataSource(
    private val latLng: String,
    private val venueExploreRepo: VenueExploreRepo
) :
    PositionalDataSource<RecommendedItem>() {

    var state = MutableLiveData<NetworkState>()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<RecommendedItem>) {

        var result: List<RecommendedItem>

        GlobalScope.launch {
            result = venueExploreRepo.getVenues(
                latLng,
                params.loadSize,
                params.startPosition
            )

            if (result.isNotEmpty()) {
                callback.onResult(result)
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<RecommendedItem>) {

        var result: List<RecommendedItem>
        state.postValue(LOADING)

        val startPosition = if (params.requestedStartPosition == -1) 0 else params.requestedStartPosition

        GlobalScope.launch {
            result = venueExploreRepo.getVenues(
                latLng,
                params.requestedLoadSize,
                startPosition
            )

            if (result.isNotEmpty()) {
                callback.onResult(result, startPosition)
            }
        }

        state.postValue(COMPLETE)
    }

}