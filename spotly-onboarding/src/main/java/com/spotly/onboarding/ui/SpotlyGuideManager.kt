package com.spotly.onboarding.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.spotly.onboarding.model.GuideStep
import com.spotly.onboarding.theme.SpotlyColors

@Composable
fun SpotlyGuideManager(
    steps: List<GuideStep>,
    currentStepIndex: Int,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onSkipOrFinish: () -> Unit,
    modifier: Modifier = Modifier,
    colors: SpotlyColors = SpotlyColors(),
    nextText: String = "بعدی",
    previousText: String = "قبلی",
    skipText: String = "رد کردن راهنما",
    finishText: String = "پایان راهنما"
) {
    if (steps.isEmpty() || currentStepIndex !in steps.indices) return

    val currentStep = steps[currentStepIndex]
    val isLastStep = currentStepIndex == steps.lastIndex
    val isFirstStep = currentStepIndex == 0
    val progress = (currentStepIndex + 1f) / steps.size
    val scrollState = rememberScrollState()

    LaunchedEffect(currentStepIndex) {
        val targetScrollPx = (scrollState.maxValue * currentStep.imageScrollYRatio).toInt()
        scrollState.animateScrollTo(
            value = targetScrollPx,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState, enabled = false)
        ) {
            currentStep.imageRes?.let { resId ->
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = currentStep.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        MorphingMultiSpotlightOverlay(
            spotlights = currentStep.spotlights,
            overlayColor = colors.overlayColor,
            modifier = Modifier.fillMaxSize()
        )

        SpotlyBottomContent(
            title = currentStep.title,
            description = currentStep.description,
            progress = progress,
            isLastStep = isLastStep,
            isFirstStep = isFirstStep,
            colors = colors,
            nextText = nextText,
            previousText = previousText,
            skipText = skipText,
            finishText = finishText,
            onNext = onNextStep,
            onPreviousStep = onPreviousStep,
            onSkipOrFinish = onSkipOrFinish
        )
    }
}

