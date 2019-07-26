package ir.zahrasadeghi.worldaround.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import ir.zahrasadeghi.worldaround.model.ApiResult
import ir.zahrasadeghi.worldaround.model.NetworkState
import ir.zahrasadeghi.worldaround.model.NetworkState.*
import ir.zahrasadeghi.worldaround.model.RecommendedItem
import ir.zahrasadeghi.worldaround.repo.VenueExploreRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VenueDataSource(
    private val latLng: String,
    private val venueExploreRepo: VenueExploreRepo
) :
    PositionalDataSource<RecommendedItem>() {


    var state = MutableLiveData<NetworkState>()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<RecommendedItem>) {

        var result: ApiResult<List<RecommendedItem>>

        GlobalScope.launch(Dispatchers.Main) {
            result = venueExploreRepo.loadVenues(
                latLng,
                params.loadSize,
                params.startPosition
            )

            if (result is ApiResult.Success) {
                val resultData = (result as ApiResult.Success<List<RecommendedItem>>).data
                callback.onResult(resultData)
            } else {
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<RecommendedItem>) {

        var result: ApiResult<List<RecommendedItem>>
        state.postValue(LOADING)

        GlobalScope.launch(Dispatchers.Main) {
            result = venueExploreRepo.loadVenues(
                latLng,
                params.requestedLoadSize,
                params.requestedStartPosition
            )

            if (result is ApiResult.Success) {
                val resultData = (result as ApiResult.Success<List<RecommendedItem>>).data
                callback.onResult(resultData, params.requestedStartPosition)
                state.postValue(SUCCESSFUL)
            } else {
                state.postValue(ERROR)
            }
            state.postValue(COMPLETE)
        }
    }

}