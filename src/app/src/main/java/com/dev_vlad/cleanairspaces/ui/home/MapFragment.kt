package com.dev_vlad.cleanairspaces.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.system.Os.remove
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.dev_vlad.cleanairspaces.R
import com.dev_vlad.cleanairspaces.databinding.FragmentMapBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable.cancel

@AndroidEntryPoint
class MapFragment : Fragment() {

        companion object {
            private val TAG = MapFragment::class.java.simpleName
        }

        private var popUp: AlertDialog? = null
        private var snackbar: Snackbar? = null
        private val viewModel: MapViewModel by viewModels()
        private var _binding: FragmentMapBinding? = null
        private val binding get() = _binding!!
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentMapBinding.inflate(inflater, container, false)
            setHasOptionsMenu(true)
            return binding.root
        }

    private fun showHelpDialog(){
        dismissPopUps()
        popUp = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.map_menu_help_desc_txt)
            .setPositiveButton(R.string.got_it
            ) { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton(R.string.dismiss
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
        return when(item.itemId) {
            R.id.action_help -> {
                showHelpDialog()
                true
            }
         else ->  item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(
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