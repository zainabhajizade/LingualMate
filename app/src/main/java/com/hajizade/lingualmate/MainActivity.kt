package com.hajizade.lingualmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.hajizade.lingualmate.presentation.navigation.AppNavigation
import com.hajizade.lingualmate.presentation.onBoarding.OnBoardingViewModel
import com.hajizade.lingualmate.ui.theme.LingualMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // ۱. این آنوتیشن برای Hilt ضروری است
class MainActivity : ComponentActivity() {

    // ۲. دریافت ViewModel با استفاده از Hilt
    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LingualMateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LingualMateTheme {
                        AppNavigation() // <--- فراخوانی گراف مسیریابی اینجا انجام می‌شود
                    }


                }
            }
        }
    }
}