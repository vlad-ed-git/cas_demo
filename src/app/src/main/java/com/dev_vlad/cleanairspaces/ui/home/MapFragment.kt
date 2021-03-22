package com.dev_vlad.cleanairspaces.ui.home

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.*
import com.dev_vlad.cleanairspaces.R
import com.dev_vlad.cleanairspaces.databinding.FragmentMapBinding
import com.dev_vlad.cleanairspaces.models.entities.LocationInfo
import com.dev_vlad.cleanairspaces.models.entities.LocationStatus
import com.dev_vlad.cleanairspaces.ui.adapters.home.MapActionsAdapter
import com.dev_vlad.cleanairspaces.ui.adapters.home.MapsMarkersInfoWindowAdapter
import com.dev_vlad.cleanairspaces.ui.dialogs.LocationInfoDialog
import com.dev_vlad.cleanairspaces.utils.showSnackBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment(), MapActionsAdapter.ClickListener {

    companion object {
        private val TAG = MapFragment::class.java.simpleName
    }

    private var popUp: AlertDialog? = null
    private var locationInfoDialog: LocationInfoDialog? = null
    private var snackbar: Snackbar? = null
    private val viewModel: MapViewModel by viewModels()
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!


    private val CIRCLE_RADIUS = 500.toDouble()
    private val STROKE_WIDTH = 5f

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    private val mapActionsAdapter by lazy {
        MapActionsAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showUserLocation()
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            mapActionsRv.layoutManager = LinearLayoutManager(
                    requireContext(),
                    RecyclerView.HORIZONTAL,
                    false
            )
            mapActionsAdapter.setMapActionsList(viewModel.mapActions)
            mapActionsRv.adapter = mapActionsAdapter
        }

        initializeMap(savedInstanceState)

    }

    private var mapView: MapView? = null
    private var aMap: AMap? = null
    private var mapsMarkersInfoWindowAdapter: MapsMarkersInfoWindowAdapter? = null
    private fun initializeMap(savedInstanceState: Bundle?) {
        binding.apply {
            mapView = map as MapView
            mapView?.let { mMapView ->
                mMapView.onCreate(savedInstanceState)
                aMap = mMapView.map
                aMap?.apply {
                    setMapLanguage(AMap.ENGLISH)
                    uiSettings.isZoomControlsEnabled = false
                    setOnMapLoadedListener { requestPermissionsToShowUserLocation() }
                    viewModel.getLocations().observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            setupMarkers(locations = it)

                            initInfoWindowsAdapter()
                        }
                    })
                }
            }
        }

    }

    private fun initInfoWindowsAdapter() {
        if (mapsMarkersInfoWindowAdapter != null) return
        mapsMarkersInfoWindowAdapter = MapsMarkersInfoWindowAdapter(requireContext())
        aMap?.apply {
            setInfoWindowAdapter(mapsMarkersInfoWindowAdapter)
            setOnInfoWindowClickListener(AMap.OnInfoWindowClickListener() { clickedMarker ->
                clickedMarker?.let {
                    displayLocationInfo(it.title)
                }
            })
        }
    }


    /****************** USER LOCATION ******************/

    private fun requestPermissionsToShowUserLocation() {
        when {
            ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                showUserLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showDialog(msgRes = R.string.location_permission_rationale) { this@MapFragment.requestPermission() }
            }
            else -> {
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showUserLocation() {
        val myLocationStyle: MyLocationStyle = MyLocationStyle()
        myLocationStyle.apply {
            myLocationIcon(
                    BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory
                                    .decodeResource(resources, R.drawable.my_location_icon)
                    )
            )
            interval(2000)
            myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        }
        aMap?.apply {
            setMyLocationStyle(myLocationStyle)
            uiSettings?.isMyLocationButtonEnabled = true
            isMyLocationEnabled = true
            promptMyLocationSettings()
        }
    }

    private fun promptMyLocationSettings() {
        if (viewModel.hasPromptedForLocationSettings)
            return
        viewModel.hasPromptedForLocationSettings = true
        val manager = context?.getSystemService(LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //showTurnOnGPSDialog
            showDialog(msgRes = R.string.turn_on_gps_prompt, positiveAction = { startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
        }
    }

    /***************** DIALOGS ****************/

    private fun displayLocationInfo(title: String) {
        val locationInfo = viewModel.getLocationInfo(locationName = title)
        locationInfo?.let {
            dismissPopUps()
            locationInfoDialog = LocationInfoDialog(it)
            locationInfoDialog?.show(parentFragmentManager, "com.dev_vlad.cleanairspaces.ui.dialogs")
        }
    }

    override fun onClickAction(actionChoice: MapActionChoices) {
        showSnackBar(
                msgRes = actionChoice.strRes
        )
    }


    private fun showDialog(msgRes: Int, positiveAction: () -> Unit) {
        dismissPopUps()
        popUp = MaterialAlertDialogBuilder(requireContext())
                .setTitle(msgRes)
                .setPositiveButton(
                        R.string.got_it
                ) { dialog, _ ->
                    positiveAction.invoke()
                    dialog.dismiss()
                }
                .setNeutralButton(
                        R.string.dismiss
                ) { dialog, _ ->
                    dialog.dismiss()
                }.create()

        popUp?.show()
    }

    private fun showSnackBar(
            msgRes: Int,
            isError: Boolean = false,
            actionRes: Int? = null,
            msg: String? = null
    ) {
        dismissPopUps()
        binding.apply {
            snackbar = viewsContainer.showSnackBar(
                    msgResId = msgRes,
                    isErrorMsg = isError,
                    actionMessage = actionRes
            )
        }
    }


    /************* MENU **************/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_view_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_help -> {
                showDialog(msgRes = R.string.map_menu_help_desc_txt, positiveAction = {})
                true
            }
            else -> item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(
                    item
            )
        }
    }


    /**************** MARKERS & CIRCLES **************/
    private fun setupMarkers(locations: List<LocationInfo>) {
        for (location in locations) {
            val markerOptions = MarkerOptions()
            markerOptions.apply {
                position(location.location)
                title(location.location_name)
                snippet(location.location_area)
                draggable(false)
                icon(
                        BitmapDescriptorFactory.fromBitmap(
                                BitmapFactory
                                        .decodeResource(resources, location.indoor_status.drawableRes)
                        )
                )
            }
            aMap?.addMarker(markerOptions)
            aMap?.addCircle(getCircle(locationStatus = location.outdoor_status, pos = location.location))
        }
        aMap?.setOnMarkerClickListener {
            Log.d(TAG, it.title)
            false
        }

    }

    private fun getCircle(pos: LatLng, locationStatus: LocationStatus): CircleOptions? {
        return CircleOptions().center(pos).radius(CIRCLE_RADIUS)
                .fillColor(ContextCompat.getColor(requireContext(), locationStatus.colorRes))
                .strokeColor(ContextCompat.getColor(requireContext(), R.color.blue))
                .strokeWidth(STROKE_WIDTH)
    }


    /************* forwarding life cycle methods & clearing *********/
    private fun dismissPopUps() {
        locationInfoDialog?.let {
            if (it.isVisible) it.dismiss()
        }
        popUp?.let {
            if (it.isShowing) it.dismiss()
        }
        snackbar?.let {
            if (it.isShown) it.dismiss()
        }
        locationInfoDialog = null
        popUp = null
        snackbar = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView?.onDestroy()
        mapsMarkersInfoWindowAdapter = null
        dismissPopUps()
        _binding = null
    }

}