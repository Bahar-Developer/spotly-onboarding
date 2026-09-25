package com.smart.kahkashan.spotlyonboardinglibrary.sample

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.spotly.onboarding.model.GuideStep
import com.spotly.onboarding.model.SpotlightShape
import com.spotly.onboarding.model.TargetShape

object OnboardingSample {
    fun getSampleSteps(screenWidthDp: Float): List<GuideStep> {
        val centerX = (screenWidthDp / 2).dp

        return listOf(
            // 1. Triangle Highlight
            GuideStep(
                title = "Triangle Spotlight",
                description = "Ideal for media play icons, video controls, and directional indicators.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 480.dp),
                        shape = TargetShape.Triangle(
                            widthDp = 100.dp,
                            heightDp = 90.dp
                        )
                    )
                )
            ),
            // 2. Polygon / Hexagon Highlight
            GuideStep(
                title = "Polygon Spotlight",
                description = "Perfect for badges, achievements, reward icons, and custom avatars.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 340.dp),
                        shape = TargetShape.Polygon(
                            radiusDp = 60.dp,
                            sides = 6
                        )
                    )
                )
            ),
            // 3. Star Highlight
            GuideStep(
                title = "Star Spotlight",
                description = "Great for ratings, gamification elements, featured items, and favorites.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 200.dp),
                        shape = TargetShape.Star(
                            radiusDp = 60.dp,
                            points = 5,
                            innerRadiusRatio = 0.45f
                        )
                    )
                )
            ),
            // 4. Circle Highlight
            GuideStep(
                title = "Circle Spotlight",
                description = "Suitable for profile avatars, Floating Action Buttons (FAB), and round icons.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 180.dp),
                        shape = TargetShape.Circle(radiusDp = 60.dp)
                    )
                )
            ),

            // 5. Rounded Rectangle Highlight
            GuideStep(
                title = "Rounded Rect Spotlight",
                description = "Best for cards, banners, and message containers with customizable corner radiuses.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 300.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = (screenWidthDp - 48).dp,
                            heightDp = 110.dp,
                            cornerRadiusDp = 16.dp
                        )
                    )
                )
            ),

            // 6. Oval Highlight
            GuideStep(
                title = "Oval Spotlight",
                description = "Designed for elliptical components, wider logos, and horizontal/vertical buttons.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 420.dp),
                        shape = TargetShape.Oval(
                            widthDp = 180.dp,
                            heightDp = 90.dp
                        )
                    )
                )
            ),

            // 7. Capsule / Pill Highlight
            GuideStep(
                title = "Capsule Spotlight",
                description = "Ideal for UI chips, fully rounded buttons, tags, and status badges.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 520.dp),
                        shape = TargetShape.Capsule(
                            widthDp = 160.dp,
                            heightDp = 48.dp
                        )
                    )
                )
            ),

            // 8. Cut Corner Rectangle Highlight
            GuideStep(
                title = "Cut Corner Rect Spotlight",
                description = "Perfect for modern cyberpunk designs, chamfered cards, and distinct UI actions.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 620.dp),
                        shape = TargetShape.CutCornerRect(
                            widthDp = (screenWidthDp - 64).dp,
                            heightDp = 100.dp,
                            cutSizeDp = 16.dp
                        )
                    )
                )
            ),

            // 9. Multi-Spotlight Combination
            GuideStep(
                title = "Multi-Spotlight Support",
                description = "Highlight multiple distinct UI elements simultaneously in a single step.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = 60.dp, y = 100.dp),
                        shape = TargetShape.Circle(radiusDp = 28.dp)
                    ),
                    SpotlightShape(
                        offsetDp = DpOffset(x = screenWidthDp.dp - 60.dp, y = 100.dp),
                        shape = TargetShape.Capsule(widthDp = 80.dp, heightDp = 36.dp)
                    )
                )
            )
        )
    }
}