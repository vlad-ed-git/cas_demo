package com.dev_vlad.cleanairspaces.ui.home

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.amap.api.maps2d.model.LatLng
import com.dev_vlad.cleanairspaces.R
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

    val locations = arrayListOf<LocationMarkers>(
        LocationMarkers(title = "Saint Gobain" , subtitle = "Demo location 1", location = LatLng(31.2271589,121.4396738), status = LocationStatus.DANGER),
        LocationMarkers(title = "Fudan University" , subtitle = "Demo location 2", location = LatLng(31.2182884,121.4191865), status = LocationStatus.SAFE),
        LocationMarkers(title = "Changning District" , subtitle = "Demo location 3", location = LatLng(31.2109503,121.3506141), status = LocationStatus.MODERATE),
    )



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

@Parcelize
class LocationMarkers(
    val title : String,
    val subtitle : String,
    val location : LatLng,
    var status : LocationStatus
): Parcelable

enum class LocationStatus(val colorRes : Int, val drawableRes : Int){
    DANGER(colorRes = R.color.orange, drawableRes = R.drawable.danger_location_marker),
    SAFE(colorRes = R.color.green, drawableRes = R.drawable.safe_location_marker),
    MODERATE(colorRes = R.color.yellow, drawableRes = R.drawable.moderate_location_marker)
}
