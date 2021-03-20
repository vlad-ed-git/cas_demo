package com.dev_vlad.cleanairspaces.ui.adapters.home

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.Marker
import com.dev_vlad.cleanairspaces.R

class MapsMarkersInfoWindowAdapter(private val context : Context) : AMap.InfoWindowAdapter {
    private var infoWindow : View? = null
    override fun getInfoWindow(marker: Marker?): View {
            render(marker)
            return infoWindow!!
    }

    private fun render(marker: Marker?){
        if(infoWindow == null) {
            infoWindow = LayoutInflater.from(context).inflate(
                    R.layout.custom_info_window, null);
        }
        infoWindow?.apply {
            findViewById<TextView>(R.id.title).text = marker?.title
            findViewById<TextView>(R.id.snippet).text = marker?.snippet
        }
    }

    override fun getInfoContents(marker: Marker?): View {
        render(marker)
        return infoWindow!!
    }
}