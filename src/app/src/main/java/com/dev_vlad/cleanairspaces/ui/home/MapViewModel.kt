package com.dev_vlad.cleanairspaces.ui.home

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dev_vlad.cleanairspaces.R
import com.dev_vlad.cleanairspaces.models.entities.LocationInfo
import com.dev_vlad.cleanairspaces.models.repository.LocationsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
        private val locationsRepo: LocationsRepo
) : ViewModel() {

    var hasPromptedForLocationSettings = false

    val mapActions = arrayListOf(
        MapActions(action = MapActionChoices.SMART_QR),
        MapActions(action = MapActionChoices.MAP_VIEW),
        MapActions(action = MapActionChoices.ADD),
    )

    fun getLocations() : LiveData<List<LocationInfo>> = locationsRepo.getLocations()
    fun getLocationInfo(locationName: String) = locationsRepo.getLocationByName(locationName)
}

@Parcelize
class MapActions(
    val action: MapActionChoices
): Parcelable

enum class MapActionChoices(val strRes: Int) {
    SMART_QR(strRes = R.string.smart_qr_txt),
    MAP_VIEW(strRes = R.string.map_view_txt),
    ADD(strRes = R.string.add_txt)
}
