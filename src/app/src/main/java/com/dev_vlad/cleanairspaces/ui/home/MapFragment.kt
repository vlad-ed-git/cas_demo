package com.dev_vlad.cleanairspaces.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev_vlad.cleanairspaces.R
import com.dev_vlad.cleanairspaces.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {

        companion object {
            private val TAG = MapFragment::class.java.simpleName
        }

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

        /************* MENU **************/
        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.map_view_menu, menu)
        }


        /****** other life cycle methods ********/
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    }