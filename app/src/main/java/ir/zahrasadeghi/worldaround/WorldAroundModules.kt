package ir.zahrasadeghi.worldaround

import com.google.android.gms.location.LocationServices
import ir.zahrasadeghi.worldaround.data.room.WorldAroundDataBase
import ir.zahrasadeghi.worldaround.repo.LocationRepo
import ir.zahrasadeghi.worldaround.repo.LocationRepoImpl
import ir.zahrasadeghi.worldaround.repo.VenueExploreRepo
import ir.zahrasadeghi.worldaround.repo.VenueExploreRepoImpl
import ir.zahrasadeghi.worldaround.viewmodel.MainViewModel
import ir.zahrasadeghi.worldaround.viewmodel.VenueDetailViewModel
import ir.zahrasadeghi.worldaround.viewmodel.VenueListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModules: Module = module {

    single<LocationRepo> { LocationRepoImpl(LocationServices.getFusedLocationProviderClient(androidContext())) }
    single<VenueExploreRepo> { VenueExploreRepoImpl(WorldAroundDataBase.getDatabase(androidContext()).venueDao()) }
}

val viewModelModules: Module = module {

    viewModel { MainViewModel(androidApplication()) }
    viewModel { VenueListViewModel(get(), get(), androidApplication()) }
    viewModel { VenueDetailViewModel(get(), androidApplication()) }
}