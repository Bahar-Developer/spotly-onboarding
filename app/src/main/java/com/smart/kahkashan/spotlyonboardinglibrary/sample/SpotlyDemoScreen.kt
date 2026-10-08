package com.smart.kahkashan.spotlyonboardinglibrary.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.spotly.onboarding.ui.SpotlyGuideManager
import com.smart.kahkashan.spotlyonboardinglibrary.ui.theme.SpotlyOnboardingLibraryTheme
import com.spotly.onboarding.theme.SpotlyColors

@Composable
fun SpotlyDemoScreen() {

    val configuration = LocalConfiguration.current

    val steps = remember(configuration) {
        OnboardingSample.getSampleSteps(
            screenWidthDp = configuration.screenWidthDp.toFloat()
        )
    }

    var currentIndex by remember {
        mutableIntStateOf(0)
    }

    var isGuideVisible by remember {
        mutableStateOf(true)
    }

    if (isGuideVisible) {

        SpotlyGuideManager(
            steps = steps,
            currentStepIndex = currentIndex,

            onNextStep = {
                if (currentIndex < steps.lastIndex) {
                    currentIndex++
                }
            },

            onPreviousStep = {
                if (currentIndex > 0) {
                    currentIndex--
                }
            },

            onSkipOrFinish = {
                isGuideVisible = false
            },

            colors = SpotlyColors(
                overlayColor = Color.Black.copy(
                    alpha = 0.65f
                ),
                containerBackground = Color(0xFF3200EE),
                titleColor = Color.White,
                descriptionColor = Color(0xFFE0E0E0),
                progressColor = Color.White,
                buttonBackgroundColor = Color(0xFFFFEB3B),
                buttonTextColor = Color(0xFF3200EE)
            ),


        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GreetingPreview() {
    SpotlyOnboardingLibraryTheme {
        SpotlyDemoScreen()
    }
}