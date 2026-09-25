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
            // ۳. شکل مثلث (Triangle)
            GuideStep(
                title = "هایلایت مثلثی (Triangle)",
                description = "مناسب برای آیکون‌های پخش ویدیو (Play) و نشانگرهای جهتی.",
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
            // ۲. شکل شش‌ضلعی (Polygon / Hexagon)
            GuideStep(
                title = "هایلایت شش‌ضلعی (Polygon)",
                description = "مناسب برای آیکون‌های مدال، پروفایل‌های خاص و نشان‌های افتخار.",
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
            // ۱. شکل ستاره (Star)
            GuideStep(
                title = "هایلایت ستاره‌ای (Star)",
                description = "مناسب برای امتیازدهی، المان‌های ویژه، گامیفیکیشن و آیکون‌های محبوب.",
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
            // ۱. شکل دایره‌ای (Circle)
            GuideStep(
                title = "هایلایت دایره‌ای (Circle)",
                description = "مناسب برای آیکون‌های پروفایل، دکمه‌های شناور (FAB) و آیکون‌های گرد.",
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 180.dp),
                        shape = TargetShape.Circle(radiusDp = 60.dp)
                    )
                )
            ),

            // ۲. شکل مستطیل با گوشه‌های گرد (RoundedRect)
            GuideStep(
                title = "هایلایت مستطیلی (RoundedRect)",
                description = "مناسب برای کارت‌ها، بنرها و باکس‌های توضیحات با میزان گردی دلخواه.",
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

            // ۳. شکل بیضی (Oval)
            GuideStep(
                title = "هایلایت بیضی (Oval)",
                description = "مناسب برای المان‌های بیضی شکل، لوگوها و دکمه‌های پهن افقی یا عمودی.",
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

            // ۴. شکل کپسولی (Capsule / Pill)
            GuideStep(
                title = "هایلایت کپسولی (Capsule)",
                description = "مناسب برای Chipها، دکمه‌های کاملاً گرد و نشان‌های وضعیت (Badges).",
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

            // ۵. شکل مستطیل با گوشه‌های برش‌خورده (CutCornerRect)
            GuideStep(
                title = "هایلایت گوشه برش‌خورده (CutCornerRect)",
                description = "مناسب برای سبک‌های طراحی خاص، مدرن و کارت‌های پَربری‌شده.",
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

            // ۶. نمونه ترکیب چند Spotlight همزمان (Multi-Spotlight)
            GuideStep(
                title = "چند هایلایت همزمان (Multi-Spotlight)",
                description = "می‌توانید به‌طور همزمان چند بخش مختلف صفحه را هایلایت کنید.",
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