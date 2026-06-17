package com.fsalman.universityapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fsalman.universityapp.feature.details.presentation.DetailsActivity
import com.fsalman.universityapp.feature.details.presentation.DetailsViewModel
import com.fsalman.universityapp.feature.listing.presentation.ListingEffect
import com.fsalman.universityapp.feature.listing.presentation.ListingIntent
import com.fsalman.universityapp.feature.listing.presentation.ListingScreen
import com.fsalman.universityapp.feature.listing.presentation.ListingViewModel

import dagger.hilt.android.AndroidEntryPoint
import com.fsalman.universityapp.ui.theme.UniversityAppTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val listingViewModel: ListingViewModel by viewModels()

    private val detailsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == DetailsActivity.RESULT_REFRESH) {
            listingViewModel.handleIntent(ListingIntent.LoadUniversities)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            UniversityAppTheme {
                ListingScreen(viewModel = listingViewModel)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                listingViewModel.effect.collect { effect ->
                    when (effect) {
                        is ListingEffect.NavigateToDetails -> {
                            val intent = Intent(
                                this@MainActivity,
                                DetailsActivity::class.java
                            ).apply {
                                putExtra(
                                    DetailsViewModel.EXTRA_UNIVERSITY,
                                    effect.university
                                )
                            }
                            detailsLauncher.launch(intent)
                        }
                    }
                }
            }
        }
    }
}
