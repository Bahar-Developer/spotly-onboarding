package com.spotly.onboarding.model

import androidx.annotation.DrawableRes



data class GuideStep(
    val title: String,
    val description: String,

    @DrawableRes
    val imageRes: Int,

    val spotlights: List<SpotlightShape> = emptyList(),

    val imageScrollYRatio: Float = 0f,

    /**
     * If true, this step automatically moves
     * to the next step after [autoAdvanceDurationMillis].
     *
     * If false, navigation buttons are shown.
     */
    val isAutoAdvance: Boolean = false,

    /**
     * Duration before automatically moving
     * to the next step.
     */
    val autoAdvanceDurationMillis: Long = 3000L
)