package com.spotly.onboarding.model

import com.spotly.onboarding.theme.SpotlyColors

data class SpotlyBottomContentData(
    val title: String,
    val description: String,
    val step: GuideStep,
    val progress: Float,
    val isFirstStep: Boolean,
    val isLastStep: Boolean,
    val onNext: () -> Unit,
    val onSkipOrFinish: () -> Unit,
    val onPrevious: () -> Unit,
    val colors: SpotlyColors,
    val showTooltipTail: Boolean = step.showTooltipTail,
    val tooltipPosition: TooltipPosition = step.tooltipPosition
)
