package com.spotly.onboarding.model

import androidx.annotation.IntRange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class TargetShape {

    data class Circle(
        val radiusDp: Dp
    ) : TargetShape()

    data class RoundedRect(
        val widthDp: Dp,
        val heightDp: Dp,
        val cornerRadiusDp: Dp = 0.dp
    ) : TargetShape()

    /**
     * Rectangle با قابلیت corner radius
     */
    data class Rectangle(
        val widthDp: Dp,
        val heightDp: Dp,
        val cornerRadiusDp: Dp = 0.dp
    ) : TargetShape()

    data class Oval(
        val widthDp: Dp,
        val heightDp: Dp
    ) : TargetShape()

    data class Capsule(
        val widthDp: Dp,
        val heightDp: Dp
    ) : TargetShape()

    data class CutCornerRect(
        val widthDp: Dp,
        val heightDp: Dp,
        val cutSizeDp: Dp = 8.dp
    ) : TargetShape()

    /**
     * شکل ستاره
     *
     * @param radiusDp شعاع بیرونی ستاره
     * @param points تعداد پره‌ها
     * @param innerRadiusRatio نسبت شعاع داخلی به بیرونی
     */
    data class Star(
        val radiusDp: Dp,
        @IntRange(from = 3, to = 10)
        val points: Int = 5,
        val innerRadiusRatio: Float = 0.4f
    ) : TargetShape()

    /**
     * شکل مثلث
     */
    data class Triangle(
        val widthDp: Dp,
        val heightDp: Dp
    ) : TargetShape()

    /**
     * چندضلعی منظم
     */
    data class Polygon(
        val radiusDp: Dp,
        @IntRange(from = 3, to = 12)
        val sides: Int = 6
    ) : TargetShape()
}

