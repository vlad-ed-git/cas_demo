package com.dev_vlad.cleanairspaces.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.maps2d.MapView
import com.dev_vlad.cleanairspaces.R
import com.dev_vlad.cleanairspaces.databinding.FragmentMapBinding
import com.dev_vlad.cleanairspaces.ui.adapters.home.MapActionsAdapter
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
    private var snackbar: Snackbar? = null
    private val viewModel: MapViewModel by viewModels()
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val mapActionsAdapter by lazy {
        MapActionsAdapter(this)
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

            val mapView = map as MapView
            mapView.onCreate(savedInstanceState)
            val aMap = mapView.map
        }

    }

    override fun onClickAction(actionChoice: MapActionChoices) {
        showSnackBar(
            msgRes = actionChoice.strRes
        )
    }

    private fun showSnackBar(msgRes: Int, isError: Boolean = false){
        dismissPopUps()
        binding.apply {
            snackbar = viewsContainer.showSnackBar(
                msgResId = msgRes,
                isErrorMsg = isError
            )
        }
    }

    private fun showHelpDialog() {
        dismissPopUps()
        popUp = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.map_menu_help_desc_txt)
            .setPositiveButton(
                R.string.got_it
            ) { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton(
                R.string.dismiss
            ) { dialog, _ ->
                dialog.dismiss()
            }.create()

        popUp?.show()
    }

    /************* MENU **************/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_view_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_help -> {
                showHelpDialog()
                true
            }
            else -> item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(
                item
            )
        }
    }

    /****** other life cycle methods ********/
    private fun dismissPopUps() {
        popUp?.let {
            if (it.isShowing) it.dismiss()
            popUp = null
        }
        snackbar?.let {
            if (it.isShown) it.dismiss()
            snackbar = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissPopUps()
        _binding = null
    }



}