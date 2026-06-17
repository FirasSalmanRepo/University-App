package com.fsalman.universityapp.feature.details.presentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fsalman.universityapp.feature.details.databinding.ActivityDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnRefresh.setOnClickListener {
            viewModel.handleIntent(DetailsIntent.Refresh)
        }

        observeState()
        observeEffects()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.university?.let { university ->
                        binding.tvUniversityName.text = university.name
                        binding.tvCountry.text = university.country
                        binding.tvAlphaCode.text = university.alphaTwoCode

                        if (university.stateProvince != null) {
                            binding.labelStateProvince.visibility = View.VISIBLE
                            binding.tvStateProvince.visibility = View.VISIBLE
                            binding.tvStateProvince.text = university.stateProvince
                        }

                        binding.tvWebPages.text = university.webPages.joinToString("\n")
                        binding.tvDomains.text = university.domains.joinToString("\n")
                    }
                }
            }
        }
    }

    private fun observeEffects() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        DetailsEffect.NavigateBackWithRefresh -> {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val RESULT_REFRESH = Activity.RESULT_OK
    }
}
