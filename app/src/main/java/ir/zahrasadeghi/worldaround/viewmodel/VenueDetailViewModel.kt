package ir.zahrasadeghi.worldaround.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ir.zahrasadeghi.worldaround.api.ApiResult
import ir.zahrasadeghi.worldaround.data.model.VenueDetail
import ir.zahrasadeghi.worldaround.repo.VenueExploreRepo
import kotlinx.coroutines.launch

class VenueDetailViewModel(
    private val venueExploreRepo: VenueExploreRepo, application: Application
) : BaseAndroidViewModel(application) {

    private val _venueDetail = MutableLiveData<VenueDetail>().apply { value = VenueDetail() }
    val venueDetail: LiveData<VenueDetail> = _venueDetail

    fun loadVenueDetail(venueId: String) {
        viewModelScope.launch {
            val result = venueExploreRepo.getVenueDetail(venueId)

            if (result is ApiResult.Success){
                _venueDetail.postValue(result.data)
            }
        }
    }
}