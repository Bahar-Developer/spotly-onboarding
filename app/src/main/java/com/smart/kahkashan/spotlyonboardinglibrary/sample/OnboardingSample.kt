package com.smart.kahkashan.spotlyonboardinglibrary.sample

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.smart.kahkashan.spotlyonboardinglibrary.R
import com.spotly.onboarding.model.GuideStep
import com.spotly.onboarding.model.ProgressMode
import com.spotly.onboarding.model.SpotlightShape
import com.spotly.onboarding.model.TargetShape

object OnboardingSample {

    fun getSampleSteps(
        targetRects: Map<String, Rect> = emptyMap()
    ): List<GuideStep> {
        val centerX = 180.dp
        val fullWidth = 360.dp

        return listOf(
            // 1. Dashboard Live UI Step (Tab 1)
            GuideStep(
                title = "تب داشبورد - آمار و عملکرد",
                description = "در این بخش می‌توانید خلاصه وضعیت و آمار فعالیت‌های خود را مشاهده کنید.",
                targetRect = targetRects["dashboard_action_btn"],
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(0.dp, 0.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = 220.dp,
                            heightDp = 50.dp,
                            cornerRadiusDp = 14.dp
                        )
                    )
                )
            ),
            // 2. Shifts Live UI Step (Tab 2)
            GuideStep(
                title = "تب شیفت‌ها - فیلتر هوشمند",
                description = "در صفحه شیفت‌ها می‌توانید فیلترهای روز و ساعت را اعمال کنید.",
                targetRect = targetRects["shifts_filter_chip"],
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(0.dp, 0.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = 220.dp,
                            heightDp = 48.dp,
                            cornerRadiusDp = 12.dp
                        )
                    )
                )
            ),
            // 3. Shift Onboarding: انتخاب محدوده کاری
            GuideStep(
                title = "انتخاب محدودهٔ کاری",
                description = "ابتدا محدوده\u200Cای را که می\u200Cخواهید در آن فعالیت کنید، انتخاب کنید.",
                imageRes = R.drawable.img_shift_onboarding_step1,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = 150.dp, y = 460.dp),
                        shape = TargetShape.Circle(radiusDp = 190.dp)
                    )
                )
            ),
            // 4. Shift Onboarding: پیدا کردن شیفت مناسب
            GuideStep(
                title = "پیدا کردن شیفت مناسب",
                description = "روز، ساعت و فیلترهای دلخواه را مشخص کنید تا شیفت\u200Cهای مناسب همان روز نمایش داده شوند.",
                imageRes = R.drawable.img_shift_onboarding_step2,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 60.dp),
                        shape = TargetShape.Circle(radiusDp = 220.dp)
                    )
                )
            ),
            // Carousel Group (وضعیت‌های شیفت)
            GuideStep(
                title = "وضعیت های شیفت",
                description = "هر کارت، وضعیت و میزان ظرفیت شیفت را نمایش می\u200Cدهد تا بتوانید شرایط آن را پیش از انتخاب بررسی کنید.",
                imageRes = R.drawable.img_shift_onboarding_step345,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 298.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = fullWidth,
                            heightDp = 211.dp,
                            cornerRadiusDp = 0.dp
                        )
                    )
                ),
                groupId = "1",
                progressMode = ProgressMode.GROUP_STATIC,
                isAutoAdvance = true
            ),
            GuideStep(
                title = "وضعیت های شیفت",
                description = "هر کارت، وضعیت و میزان ظرفیت شیفت را نمایش می\u200Cدهد تا بتوانید شرایط آن را پیش از انتخاب بررسی کنید.",
                imageRes = R.drawable.img_shift_onboarding_step345,
                imageScrollYRatio = 0.1f,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 480.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = fullWidth,
                            heightDp = 205.dp,
                            cornerRadiusDp = 0.dp
                        )
                    )
                ),
                groupId = "1",
                progressMode = ProgressMode.GROUP_STATIC,
                isAutoAdvance = true
            ),
            GuideStep(
                title = "وضعیت های شیفت",
                description = "هر کارت، وضعیت و میزان ظرفیت شیفت را نمایش می\u200Cدهد تا بتوانید شرایط آن را پیش از انتخاب بررسی کنید.",
                imageRes = R.drawable.img_shift_onboarding_step345,
                imageScrollYRatio = 0.8f,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 630.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = fullWidth,
                            heightDp = 205.dp,
                            cornerRadiusDp = 0.dp
                        )
                    )
                ),
                groupId = "1",
                progressMode = ProgressMode.GROUP_STATIC,
                isAutoAdvance = true,
                autoAdvanceDurationMillis = 2000L
            ),
            GuideStep(
                title = "وضعیت های شیفت",
                description = "هر کارت، وضعیت و میزان ظرفیت شیفت را نمایش می\u200Cدهد تا بتوانید شرایط آن را پیش از انتخاب بررسی کنید.",
                imageRes = R.drawable.img_shift_onboarding_step345,
                imageScrollYRatio = 1.4f,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 780.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = fullWidth,
                            heightDp = 170.dp,
                            cornerRadiusDp = 0.dp
                        )
                    )
                ),
                groupId = "1",
                isAutoAdvance = true,
                isLooping = true,
                progressMode = ProgressMode.GROUP_STATIC
            ),
            GuideStep(
                title = "جزئیات و مزایای شیفت",
                description = "با انتخاب هر کارت، جزئیات شیفت را ببینید. نشان\u200Cهای «گارانتی» و «آب\u200Cوهوا» یعنی امکان درآمد بیشتر در آن شیفت",
                imageRes = R.drawable.img_shift_onboarding_step6,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 334.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = fullWidth,
                            heightDp = 520.dp,
                            cornerRadiusDp = 16.dp
                        )
                    )
                )
            ),
            GuideStep(
                title = "شیفت انتخاب شده",
                description = "پس از انتخاب، وضعیت کارت به «انتخاب\u200Cشده» تغییر می\u200Cکند و شیفت به برنامه کاری شما اضافه می\u200Cشود",
                imageRes = R.drawable.img_shift_onboarding_step78,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 325.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = fullWidth,
                            heightDp = 200.dp,
                            cornerRadiusDp = 0.dp
                        )
                    )
                ),
                groupId = "2",
                progressMode = ProgressMode.GROUP_STATIC,
                isAutoAdvance = true,
                autoAdvanceDurationMillis = 2000L
            ),
            GuideStep(
                title = "شیفت انتخاب شده",
                description = "پس از انتخاب، وضعیت کارت به «انتخاب\u200Cشده» تغییر می\u200Cکند و شیفت به برنامه کاری شما اضافه می\u200Cشود",
                imageRes = R.drawable.img_shift_onboarding_step78,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = 40.dp, y = 30.dp),
                        shape = TargetShape.Circle(radiusDp = 120.dp)
                    )
                ),
                groupId = "2",
                autoAdvanceDurationMillis = 2000L,
                isLooping = true,
                isAutoAdvance = true,
                progressMode = ProgressMode.GROUP_STATIC
            ),
            GuideStep(
                title = "بخش آموزش",
                description = " برای توضیحات کامل\u200Cتر و تماشای ویدیوهای مربوط به شیفت هم به پروفایل بخش «آموزش» بروید.",
                imageRes = R.drawable.img_profile_onboarding_step9,
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(x = centerX, y = 450.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = fullWidth,
                            heightDp = 67.dp,
                            cornerRadiusDp = 999.dp
                        )
                    )
                )
            ),
            GuideStep(
                title = "صفحه پروفایل - کارت آموزش",
                description = "این مرحله روی کارت واقعی آموزش در صفحه پروفایل قرار دارد.",
                targetRect = targetRects["profile_education_card"],
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(0.dp, 0.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = 300.dp,
                            heightDp = 80.dp,
                            cornerRadiusDp = 20.dp
                        )
                    )
                )
            )
        )
    }
}
