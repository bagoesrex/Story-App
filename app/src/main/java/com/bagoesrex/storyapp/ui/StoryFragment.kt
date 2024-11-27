package com.bagoesrex.storyapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bagoesrex.storyapp.databinding.FragmentStoryBinding
import com.bagoesrex.storyapp.ui.adapter.StoryAdapter
import com.bagoesrex.storyapp.ui.viewmodel.factory.StoryViewModelFactory
import com.bagoesrex.storyapp.ui.viewmodel.StoryViewModel

class StoryFragment : Fragment() {

    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var storyAdapter: StoryAdapter
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        storyViewModel.getStories()

    }

    private fun setupRecyclerView() {
        binding.storyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        storyAdapter = StoryAdapter()
        binding.storyRecyclerView.adapter = storyAdapter
    }

    private fun observeViewModel() {
        storyViewModel.storyList.observe(viewLifecycleOwner) { storyList ->
            storyAdapter.submitList(storyList)
        }

        storyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        storyViewModel.getStories()
    }
}
