package com.spotly.onboarding.model

import androidx.annotation.DrawableRes

data class GuideStep(
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int? = null,
    val spotlights: List<SpotlightShape> = emptyList(),
    val imageScrollYRatio: Float = 0f
)