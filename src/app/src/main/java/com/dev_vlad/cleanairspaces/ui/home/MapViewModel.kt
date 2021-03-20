package com.dev_vlad.cleanairspaces.ui.home

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.amap.api.maps2d.model.LatLng
import com.dev_vlad.cleanairspaces.R
import com.dev_vlad.cleanairspaces.models.LocationStatus
import com.dev_vlad.cleanairspaces.models.LocationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {


    val mapActions = arrayListOf(
        MapActions(action = MapActionChoices.SMART_QR),
        MapActions(action = MapActionChoices.MAP_VIEW),
        MapActions(action = MapActionChoices.ADD),
    )

   private val locationUpdates = arrayListOf<LocationInfo>(
            LocationInfo(
                            location_name = "Saint Gobain",
                            updated = System.currentTimeMillis(),
                            location_area = "Outdoor Shanghai",
                            indoor_pm = "PM2.5",
                            indoor_points = 6,
                            indoor_status = LocationStatus.DANGER,
                            outdoor_pm = "PM2.5",
                            outdoor_points = 18,
                            outdoor_status = LocationStatus.DANGER,
                            location = LatLng(31.2271589,121.4396738),
                            locationLogoRes = R.drawable.saint_gobain_logo
            ),
            LocationInfo(
                    location_name = "Fudan University",
                    updated = System.currentTimeMillis(),
                    location_area = "Outdoor Shanghai",
                    indoor_pm = "PM4.5",
                    indoor_points = 8,
                    indoor_status = LocationStatus.SAFE,
                    outdoor_pm = "PM4.5",
                    outdoor_points = 22,
                    outdoor_status = LocationStatus.MODERATE,
                    location = LatLng(31.2182884,121.4191865),
                    locationLogoRes = R.drawable.fudan_university_logo
            ),
            LocationInfo(
                    location_name = "Changning District",
                    updated = System.currentTimeMillis(),
                    location_area = "Outdoor Shanghai",
                    indoor_pm = "PM7.5",
                    indoor_points = 9,
                    indoor_status = LocationStatus.SAFE,
                    outdoor_pm = "PM9.5",
                    outdoor_points = 26,
                    outdoor_status = LocationStatus.SAFE,
                    location = LatLng(31.2109503,121.3506141)
            )
    )

    fun getLocations() = locationUpdates

    fun getLocationInfo(locationName: String): LocationInfo? {
        return locationUpdates.find { it.location_name == locationName }
    }

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
