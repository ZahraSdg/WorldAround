package ir.zahrasadeghi.worldaround.viewmodel

import android.app.Application
import ir.zahrasadeghi.worldaround.repo.VenueExploreRepo
import kotlinx.coroutines.launch

class VenueDetailViewModel(
    private val venueExploreRepo: VenueExploreRepo, application: Application
) : BaseAndroidViewModel(application) {

    fun loadVenueDetail(venueId: String) {
        viewModelScope.launch {
            venueExploreRepo.getVenueDetail(venueId)
        }
    }
}