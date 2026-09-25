package com.smart.kahkashan.spotlyonboardinglibrary.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.smart.kahkashan.spotlyonboardinglibrary.ui.theme.SpotlyOnboardingLibraryTheme
import com.spotly.onboarding.theme.SpotlyColors
import com.spotly.onboarding.ui.SpotlyGuideManager

@Composable
fun SpotlyDemoScreen() {
    val configuration = LocalConfiguration.current
    val steps = remember(configuration) {
        OnboardingSample.getSampleSteps(configuration.screenWidthDp.toFloat())
    }

    var currentIndex by remember { mutableIntStateOf(0) }

    SpotlyGuideManager(
        steps = steps,
        currentStepIndex = currentIndex,
        onNextStep = { if (currentIndex < steps.lastIndex) currentIndex++ },
        onPreviousStep = { if (currentIndex > 0) currentIndex-- },
        onSkipOrFinish = {
        },
        colors = SpotlyColors(
            containerBackground = Color(0xFF3200EE),
            buttonBackgroundColor = Color(0xFFFFEB3B),
            buttonTextColor = Color(0xFF3200EE)
        ),
        skipText = "Skip Guide",
        finishText = "Finish",
        nextText = "Next",
        previousText = "Previous",
            
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpotlyOnboardingLibraryTheme {
        SpotlyDemoScreen()
    }
}