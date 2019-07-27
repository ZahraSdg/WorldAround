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
import timber.log.Timber

class VenueDataSource(
    private val latLng: String,
    private val venueExploreRepo: VenueExploreRepo
) :
    PositionalDataSource<RecommendedItem>() {

    var state = MutableLiveData<NetworkState>()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<RecommendedItem>) {

        var result: ApiResult<List<RecommendedItem>>

        GlobalScope.launch(Dispatchers.Main) {
            try {
                result = venueExploreRepo.loadVenues(
                    latLng,
                    params.loadSize,
                    params.startPosition
                )

                if (result is ApiResult.Success) {
                    val resultData = (result as ApiResult.Success<List<RecommendedItem>>).data
                    callback.onResult(resultData)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<RecommendedItem>) {

        var result: ApiResult<List<RecommendedItem>>
        state.postValue(LOADING)

        val startPosition = if (params.requestedStartPosition == -1) 0 else params.requestedStartPosition

        GlobalScope.launch(Dispatchers.Main) {
            try {
                result = venueExploreRepo.loadVenues(
                    latLng,
                    params.requestedLoadSize,
                    startPosition
                )

                if (result is ApiResult.Success) {
                    val resultData = (result as ApiResult.Success<List<RecommendedItem>>).data
                    callback.onResult(resultData, startPosition)
                    state.postValue(SUCCESSFUL)
                } else {
                    state.postValue(ERROR)
                }
            } catch (e: Exception) {
                state.postValue(ERROR)
                Timber.e(e)
            } finally {
                state.postValue(COMPLETE)
            }
        }
    }

}