package com.spotly.onboarding.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp



data class GuideStep(
    val title: String,
    val description: String,
    val trackingTag: String = "",
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

    val isLooping: Boolean = false,

    /**
     * Padding around the spotlight cutout to give breathing room. (Optional, default is 0.dp)
     */
    val spotlightPadding: Dp = 0.dp,

    /**
     * Whether to show a pulsing animation ring around the spotlight. (Optional, default is false)
     */
    val isPulseAnimationEnabled: Boolean = false,

    /**
     * Whether taps inside the spotlight area can pass through to underlying UI. (Optional, default is false)
     */
    val allowTargetClickThrough: Boolean = false,

    /**
     * Whether to automatically scroll container to bring target into view. (Optional, default is true)
     */
    val isAutoScrollEnabled: Boolean = true,

    /**
     * Whether to show a directional triangular pointer (tail) on the tooltip card pointing towards the spotlight. (Optional, default is false)
     */
    val showTooltipTail: Boolean = false,

    /**
     * Position of the tooltip card (TOP, BOTTOM, or AUTO based on spotlight position).
     */
    val tooltipPosition: TooltipPosition = TooltipPosition.AUTO
)

enum class ProgressMode {
    INDIVIDUAL,
    GROUP_INCREMENTAL,
    GROUP_STATIC
}

enum class TooltipPosition {
    AUTO,
    TOP,
    BOTTOM
}
