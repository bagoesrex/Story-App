package com.bagoesrex.storyapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bagoesrex.storyapp.R
import com.bagoesrex.storyapp.databinding.ActivityDetailBinding
import com.bagoesrex.storyapp.ui.viewmodel.DetailViewModel
import com.bagoesrex.storyapp.ui.viewmodel.factory.DetailViewModelFactory
import com.bagoesrex.storyapp.utils.showToast
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbarDetail.toolbar)
        supportActionBar?.title = getString(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        binding.toolbarDetail.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val storyId = intent.getStringExtra("STORY_ID")

        if (storyId != null) {
            detailViewModel.getStory(storyId)
        } else {
            showToast(this, getString(R.string.invalid_story_id))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeViewModel() {

        detailViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })

        detailViewModel.story.observe(this, Observer { storyDetail ->
            if (storyDetail != null) {
                binding.apply {
                    Picasso.get()
                        .load(storyDetail.story.photoUrl)
                        .into(binding.previewImageView)
                    binding.nameTextView.text = storyDetail.story.name
                    binding.descriptionTextView.text = storyDetail.story.description
                }
            }
        })

        detailViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                showToast(this, it)
            }
        })
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.detailLayout.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.detailLayout.visibility = View.VISIBLE
    }
}