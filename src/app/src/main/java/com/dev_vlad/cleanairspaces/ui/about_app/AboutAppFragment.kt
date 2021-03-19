package com.dev_vlad.cleanairspaces.ui.about_app

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.dev_vlad.cleanairspaces.databinding.FragmentAboutAppBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutAppFragment : Fragment() {

    companion object {
        private val TAG = AboutAppFragment::class.java.simpleName
    }

    private var _binding: FragmentAboutAppBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    /****** other life cycle methods ********/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}