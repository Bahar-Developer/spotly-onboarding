package com.spotly.onboarding.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.geometry.Rect



data class GuideStep(
    val title: String,
    val description: String,

    @DrawableRes
    val imageRes: Int = 0,

    val targetRect: Rect? = null,

    val spotlights: List<SpotlightShape> = emptyList(),

    val imageScrollYRatio: Float = 0f,

    /**
     * If true, this step automatically moves
     * to the next step after [autoAdvanceDurationMillis].
     *
     * If false, navigation buttons are shown.
     */
    val isAutoAdvance: Boolean = false,
    val showDescription: Boolean = true,
    /**
     * Duration before automatically moving
     * to the next step.
     */
    val autoAdvanceDurationMillis: Long = 3000L,

    /**
     * Optional ID to group steps logically together.
     * Steps with the same [groupId] will be treated as one logical step.
     */
    val groupId: String? = null,

    val progressMode: ProgressMode = ProgressMode.INDIVIDUAL,

    val isLooping: Boolean = false
)

enum class ProgressMode {
    INDIVIDUAL,
    GROUP_INCREMENTAL,
    GROUP_STATIC
}