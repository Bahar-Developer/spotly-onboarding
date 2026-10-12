package com.spotly.onboarding.model

import com.spotly.onboarding.theme.SpotlyColors

/**
 * Holds the state and actions required to render a custom bottom content
 * in the Spotly guide.
 */
data class SpotlyBottomContentData(
    val step: GuideStep,
    val progress: Float,
    val isFirstStep: Boolean,
    val isLastStep: Boolean,
    val onNext: () -> Unit,
    val onPrevious: () -> Unit,
    val onSkipOrFinish: () -> Unit,
    val colors: SpotlyColors
)
