package com.bagoesrex.storyapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bagoesrex.storyapp.databinding.FragmentStoryBinding
import com.bagoesrex.storyapp.ui.adapter.LoadingStateAdapter
import com.bagoesrex.storyapp.ui.adapter.StoryAdapterPaging
import com.bagoesrex.storyapp.ui.viewmodel.factory.StoryViewModelFactory
import com.bagoesrex.storyapp.ui.viewmodel.StoryViewModel
import com.bagoesrex.storyapp.utils.showToast

class StoryFragment : Fragment() {

    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var storyAdapter: StoryAdapterPaging
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private val afterAddStoryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            storyAdapter.refresh()
            storyAdapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.NotLoading) {
                    binding.storyRecyclerView.smoothScrollToPosition(0)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        observeViewModel()
        storyViewModel.getStories()
    }

    private fun setupUI() {
        binding.addFab.setOnClickListener {
            Intent(requireContext(), AddStoryActivity::class.java).also {
                afterAddStoryLauncher.launch(it)
            }
        }
    }


    private fun setupRecyclerView() {
        binding.storyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        storyAdapter = StoryAdapterPaging()
        binding.storyRecyclerView.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        storyAdapter.addLoadStateListener { loadState ->
            binding.progressBar.visibility =
                if (loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE

            binding.emptyImageView.visibility =
                if (loadState.source.refresh is LoadState.NotLoading && storyAdapter.itemCount == 0) View.GONE else View.VISIBLE

            val errorState = loadState.source.refresh as? LoadState.Error
                ?: loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error

            errorState?.let {
                it.error.localizedMessage?.let { it1 -> showToast(requireContext(), it1) }
            }
        }
    }

    private fun observeViewModel() {
        storyViewModel.getStories()
            .observe(viewLifecycleOwner) { pagingData ->
                storyAdapter.submitData(lifecycle, pagingData)
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
