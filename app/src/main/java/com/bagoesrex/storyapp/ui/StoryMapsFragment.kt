package com.bagoesrex.storyapp.ui

import android.content.Intent
import android.content.res.Resources
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bagoesrex.storyapp.R
import com.bagoesrex.storyapp.databinding.FragmentStoryMapsBinding
import com.bagoesrex.storyapp.ui.viewmodel.StoryMapsViewModel
import com.bagoesrex.storyapp.ui.viewmodel.factory.StoryMapsViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.bagoesrex.storyapp.data.Result
import com.bagoesrex.storyapp.data.remote.response.ListStoryItem
import com.bagoesrex.storyapp.utils.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class StoryMapsFragment : Fragment(), OnMapReadyCallback {

    private val storyMapViewModel: StoryMapsViewModel by viewModels {
        StoryMapsViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var mMap: GoogleMap
    private var _binding: FragmentStoryMapsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.addFab.setOnClickListener {
            Intent(requireContext(), AddStoryActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun observeViewModel() {
        storyMapViewModel.stories.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val stories = result.data
                    if (stories.isNotEmpty()) {
                        addAllMarker(stories)
                    } else {
                        showToast(requireContext(), getString(R.string.no_stories))
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(requireContext(), result.error)
                }
            }
        }

        storyMapViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        storyMapViewModel.getAllStoriesWithMap()
    }

    private fun addAllMarker(stories: List<ListStoryItem>) {
        if (::mMap.isInitialized) {
            setMapStyle()
            val boundsBuilder = LatLngBounds.Builder()

            stories.forEach { story ->
                val lat = story.lat
                val lon = story.lon
                if (lat != null && lon != null) {
                    val latLng = LatLng(lat, lon)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(story.name)
                            .snippet(story.description)
                    )
                    boundsBuilder.include(latLng)
                }
            }

            val bounds = boundsBuilder.build()
            val padding = 100
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.style_map
                    )
                )
            if (!success) {
                showToast(requireContext(), "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            showToast(requireContext(), "Can't find style. Error: $exception")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}