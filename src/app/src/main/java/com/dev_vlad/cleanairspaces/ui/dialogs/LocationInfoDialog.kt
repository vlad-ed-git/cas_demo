package com.dev_vlad.cleanairspaces.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.amap.api.maps2d.model.LatLng
import com.bumptech.glide.Glide
import com.dev_vlad.cleanairspaces.R
import com.dev_vlad.cleanairspaces.databinding.MapPlaceDetailsPopupBinding
import com.dev_vlad.cleanairspaces.models.entities.LocationInfo
import com.dev_vlad.cleanairspaces.models.entities.LocationStatus

class LocationInfoDialog(private val location : LocationInfo) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val view: View = inflater.inflate(R.layout.map_place_details_popup, null)
            builder.setView(view)
            val binding =  MapPlaceDetailsPopupBinding.bind(view)

            binding.apply {
                locationNameTv.text = location.location_name
                locationAreaTv.text = location.location_area
                indoorPmTv.text = location.indoor_pm
                outdoorPmTv.text = location.outdoor_pm
                indoorPointsTv.text = location.indoor_points.toString()
                outdoorPointsTv.text = location.outdoor_points.toString()
                indoorStatusIndicatorTv.setText(location.indoor_status.strRes)
                indoorStatusIndicatorIv.setImageResource(location.indoor_status.statusIndicatorRes)
                outdoorStatusIndicatorTv.setText(location.outdoor_status.strRes)
                outdoorStatusIndicatorIv.setImageResource(location.outdoor_status.statusIndicatorRes)
                Glide.with(view.context)
                        .load(location.locationLogoRes)
                        .into(locationLogoIv)
                val updatedOn = getString(R.string.updated_on_prefix) + " " + location.getFormattedUpdateTime()
                updatedTv.text = updatedOn
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



}