package com.spotly.onboarding.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.spotly.onboarding.model.GuideStep
import com.spotly.onboarding.model.ProgressMode
import com.spotly.onboarding.model.SpotlightShape
import com.spotly.onboarding.model.TargetShape
import com.spotly.onboarding.model.SpotlyBottomContentData
import com.spotly.onboarding.theme.SpotlyColors
import com.spotly.onboarding.ui.MorphingMultiSpotlightOverlay

import kotlinx.coroutines.delay


enum class OnboardingAction {
    PAGE_VIEW,
    NEXT_CLICK,
    PREVIOUS_CLICK,
    SKIP_CLICK,
    FINISH_CLICK
}

@Composable
fun SpotlyGuideManager(
    steps: List<GuideStep>,
    currentStepIndex: Int,
    onNextStep: () -> Unit,
    onSkipOrFinish: () -> Unit,
    onPreviousStep: () -> Unit,

    modifier: Modifier = Modifier,

    colors: SpotlyColors = SpotlyColors(),
    onTrackingEvent: ((trackingTag: String, action: OnboardingAction) -> Unit)? = null,
    bottomContent: @Composable (SpotlyBottomContentData) -> Unit = { data ->
        SpotlyBottomContent(
            title = data.step.title,
            description = data.step.description,
            progress = data.progress,
            isFirstStep = data.isFirstStep,
            isLastStep = data.isLastStep,
            showNavigationButtons = true,
            onNext = data.onNext,
            onSkipOrFinish = data.onSkipOrFinish,
            onPreviousStep = data.onPrevious,
            colors = data.colors
        )
    }
) {

    /*
     * ---------------------------------------------------------
     * SAFETY
     * ---------------------------------------------------------
     */
    if (
        steps.isEmpty() ||
        currentStepIndex !in steps.indices
    ) {
        return
    }


    /*
     * ---------------------------------------------------------
     * CURRENT STEP
     * ---------------------------------------------------------
     */
    val currentStep =
        steps[currentStepIndex]


    val isFirstStep =
        currentStepIndex == 0


    val isLastStep =
        currentStepIndex == steps.lastIndex


    /*
     * ---------------------------------------------------------
     * BOTTOM CONTENT VISIBILITY
     * ---------------------------------------------------------
     */
    var showBottomContent by remember {
        mutableStateOf(
            currentStep.showDescription
        )
    }


    /*
     * هر بار step عوض شد،
     * visibility مربوط به step جدید را تنظیم کن.
     */
    LaunchedEffect(
        currentStepIndex,
        currentStep.showDescription
    ) {

        showBottomContent =
            currentStep.showDescription

        if (currentStep.trackingTag.isNotEmpty()) {
            onTrackingEvent?.invoke(currentStep.trackingTag, OnboardingAction.PAGE_VIEW)
        }
    }


    /*
     * ---------------------------------------------------------
     * AUTO ADVANCE
     * ---------------------------------------------------------
     */
    LaunchedEffect(
        currentStepIndex,
        currentStep.isAutoAdvance,
        currentStep.autoAdvanceDurationMillis
    ) {

        if (!currentStep.isAutoAdvance) {
            return@LaunchedEffect
        }


        /*
         * صبر برای تایمر step فعلی
         */
        delay(
            currentStep.autoAdvanceDurationMillis
                .coerceAtLeast(1L)
        )


        /*
         * اگر آخرین step هستیم،
         * onboarding تمام شود.
         */
        if (currentStepIndex == steps.lastIndex) {
            onSkipOrFinish()
        } else {
            /*
             * بررسی برای لوپ شدن گروه
             */
            if (currentStep.isLooping && currentStep.groupId != null) {
                val nextStep = steps.getOrNull(currentStepIndex + 1)
                if (nextStep?.groupId != currentStep.groupId) {
                    // رسیدیم به انتهای گروه و باید لوپ کنیم
                    val firstIndexInGroup = steps.indexOfFirst { it.groupId == currentStep.groupId }
                    if (firstIndexInGroup != -1) {
                        // پرش به ایندکس شروع گروه (تکرار)
                        val stepsToGoBack = currentStepIndex - firstIndexInGroup
                        repeat(stepsToGoBack) {
                            onPreviousStep()
                        }
                    } else {
                        onNextStep()
                    }
                } else {
                    onNextStep()
                }
            } else {
                onNextStep()
            }
        }
    }


    /*
     * ---------------------------------------------------------
     * SCROLL
     * ---------------------------------------------------------
     */
    val scrollState =
        rememberScrollState()


    val density =
        LocalDensity.current


    var imageSize by remember {
        mutableStateOf(
            IntSize.Zero
        )
    }


    /*
     * ---------------------------------------------------------
     * IMAGE SCROLL
     * ---------------------------------------------------------
     *
     * تشخیص هوشمند ارتفاع اسپات‌لایت:
     * اگر ارتفاع اسپات‌لایت/بخش مورد نظر بزرگ باشد (مثلاً کل صفحه یا بیشتر را بگیرد)،
     * صفحه در ابتدای اسپات‌لایت (سر اسپات‌لایت) قرار می‌گیرد تا کاربر خودش با اسکرول انتهای آن را ببیند.
     * اگر ارتفاع کوچک یا معمولی باشد، طبق روال قبل مرکز‌چین می‌شود.
     */
    LaunchedEffect(
        currentStepIndex,
        currentStep.imageScrollYRatio,
        currentStep.spotlights,
        imageSize
    ) {
        val targetScrollPx = if (currentStep.spotlights.isNotEmpty() && imageSize.width > 0) {
            val baseWidthPx = with(density) { 360.dp.toPx() }
            val scale = imageSize.width.toFloat() / baseWidthPx
            val firstSpotlight = currentStep.spotlights.first()
            
            // مختصات Y در فایل نمونه، مرکز مستطیل است (یا مرکز شکل)
            val centerYPx = with(density) { firstSpotlight.offsetDp.y.toPx() } * scale
            
            // محاسبه تقریبی ارتفاع اسپات‌لایت
            val spotlightHeightPx = when (val shape = firstSpotlight.shape) {
                is TargetShape.RoundedRect -> with(density) { shape.heightDp.toPx() } * scale
                is TargetShape.Rectangle -> with(density) { shape.heightDp.toPx() } * scale
                is TargetShape.Circle -> with(density) { (shape.radiusDp * 2).toPx() } * scale
                is TargetShape.Oval -> with(density) { shape.heightDp.toPx() } * scale
                is TargetShape.Capsule -> with(density) { shape.heightDp.toPx() } * scale
                else -> 100f * scale
            }

            // از آنجا که offsetDp.y در مدل شما معمولاً مرکز شکل (Center) را نشان می‌دهد،
            // سر واقعی اسپات‌لایت (بالای کادر) برابر است با: مرکز منهای نصف ارتفاع
            val topYPx = centerYPx - (spotlightHeightPx / 2f)

            val viewportHeightPx = scrollState.viewportSize.toFloat()
            
            if (viewportHeightPx > 0f) {
                // اگر ارتفاع اسپات‌لایت بزرگ است، صفحه را دقیقاً روی سر اسپات‌لایت (بالای آن) تنظیم می‌کنیم
                if (spotlightHeightPx > (viewportHeightPx * 0.4f)) {
                    (topYPx - 32f).toInt() // کمی پدینگ در بالا برای زیبایی
                } else {
                    // در غیر این صورت طبق روال قبل مرکز‌چین می‌شود
                    (centerYPx - (viewportHeightPx / 3f)).toInt()
                }
            } else {
                (scrollState.maxValue * currentStep.imageScrollYRatio.coerceIn(0f, 1f)).toInt()
            }
        } else {
            (scrollState.maxValue * currentStep.imageScrollYRatio.coerceIn(0f, 1f)).toInt()
        }

        if (scrollState.maxValue > 0) {
            scrollState.animateScrollTo(
                value = targetScrollPx.coerceIn(0, scrollState.maxValue),
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            )
        }
    }


    /*
     * ---------------------------------------------------------
     * SPOTLIGHT CALCULATION
     * ---------------------------------------------------------
     */
    val calculatedSpotlights =
        remember(
            currentStep.spotlights,
            imageSize,
            scrollState.value,
            density,
            currentStep.targetRect
        ) {

            if (currentStep.targetRect != null) {
                // Live UI Mode: Relative to targetRect
                val rect = currentStep.targetRect
                if (currentStep.spotlights.isEmpty()) {
                    listOf(
                        SpotlightShape(
                            offsetDp = DpOffset(
                                x = with(density) { rect.center.x.toDp() },
                                y = with(density) { rect.center.y.toDp() }
                            ),
                            shape = TargetShape.RoundedRect(
                                widthDp = with(density) { rect.width.toDp() },
                                heightDp = with(density) { rect.height.toDp() },
                                cornerRadiusDp = 12.dp
                            )
                        )
                    )
                } else {
                    currentStep.spotlights.map { item ->
                        item.copy(
                            offsetDp = DpOffset(
                                x = with(density) { (rect.center.x + item.offsetDp.x.toPx()).toDp() },
                                y = with(density) { (rect.center.y + item.offsetDp.y.toPx()).toDp() }
                            )
                        )
                    }
                }
            } else if (imageSize.width == 0) {

                currentStep.spotlights

            } else {

                /*
                 * Base design width
                 */
                val baseWidthPx =
                    with(density) {
                        360.dp.toPx()
                    }


                /*
                 * Scale image
                 */
                val scale =
                    imageSize.width.toFloat() /
                            baseWidthPx


                /*
                 * Current scroll
                 */
                val scrollYPx =
                    scrollState.value.toFloat()


                currentStep.spotlights.map { item ->

                    val xPx =
                        with(density) {
                            item.offsetDp.x.toPx()
                        } * scale


                    val yPx =
                        (
                                with(density) {
                                    item.offsetDp.y.toPx()
                                } * scale
                                ) - scrollYPx


                    SpotlightShape(

                        offsetDp =
                            DpOffset(

                                x =
                                    with(density) {
                                        xPx.toDp()
                                    },

                                y =
                                    with(density) {
                                        yPx.toDp()
                                    }
                            ),

                        shape =
                            scaleTargetShape(
                                shape = item.shape,
                                scale = scale,
                                density = density
                            )
                    )
                }
            }
        }


    /*
     * ---------------------------------------------------------
     * MAIN UI
     * ---------------------------------------------------------
     */
    Box(
        modifier =
            modifier.fillMaxSize()
    ) {


        /*
         * -----------------------------------------------------
         * ONBOARDING IMAGE
         * -----------------------------------------------------
         */
        if (currentStep.targetRect == null) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clipToBounds()
                        .verticalScroll(
                            state = scrollState,
                            enabled = true
                        )
            ) {

                CustomImage(

                    id =
                        currentStep.imageRes,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned {
                                    coordinates ->

                                imageSize =
                                    coordinates.size
                            },

                    contentScale =
                        ContentScale.FillWidth
                )
            }
        }


        /*
         * -----------------------------------------------------
         * SPOTLIGHT OVERLAY
         * -----------------------------------------------------
         */
        MorphingMultiSpotlightOverlay(

            spotlights =
                calculatedSpotlights,

            overlayColor =
                colors.overlayColor,

            modifier =
                Modifier.fillMaxSize()
        )


        /*
         * -----------------------------------------------------
         * BOTTOM CONTENT
         * -----------------------------------------------------
         */
        AnimatedVisibility(

            visible =
                showBottomContent,

            enter =
                fadeIn(
                    animationSpec =
                        tween(
                            durationMillis =
                                BOTTOM_CONTENT_ANIMATION_DURATION
                        )
                ),

            exit =
                fadeOut(
                    animationSpec =
                        tween(
                            durationMillis =
                                BOTTOM_CONTENT_ANIMATION_DURATION
                        )
                ),

            modifier =
                Modifier.fillMaxSize()
        ) {

            val mainSteps = remember(steps) { getMainSteps(steps) }
            val currentMainGroup = mainSteps.find { currentStepIndex in it.startIndex..it.endIndex }
            val currentMainIndex = mainSteps.indexOf(currentMainGroup).coerceAtLeast(0)

            val progressValue = when (currentStep.progressMode) {
                ProgressMode.GROUP_STATIC -> {
                    (currentMainIndex + 1f) / mainSteps.size
                }
                ProgressMode.GROUP_INCREMENTAL -> {
                    val groupSteps = currentMainGroup?.steps ?: listOf(currentStep)
                    val indexInGroup = groupSteps.indexOf(currentStep).coerceAtLeast(0)
                    (indexInGroup + 1f) / groupSteps.size
                }
                else -> {
                    (currentMainIndex + 1f) / mainSteps.size
                }
            }

            val contentData = SpotlyBottomContentData(
                title = currentStep.title,
                description = currentStep.description,
                step = currentStep,
                progress = progressValue,
                isFirstStep = isFirstStep,
                isLastStep = isLastStep,
                onNext = {
                    if (currentStep.trackingTag.isNotEmpty()) {
                        onTrackingEvent?.invoke(currentStep.trackingTag, OnboardingAction.NEXT_CLICK)
                    }
                    handleManualStepChange(
                        showBottomContent = {
                            showBottomContent = false
                        },
                        action = {
                            val currentMainGroup = mainSteps.find { currentStepIndex in it.startIndex..it.endIndex }
                            val currentMainIndex = mainSteps.indexOf(currentMainGroup)

                            if (currentMainGroup?.groupId != null) {
                                if (currentMainIndex < mainSteps.size - 1) {
                                    val nextMainGroup = mainSteps[currentMainIndex + 1]
                                    val stepsToAdvance = nextMainGroup.startIndex - currentStepIndex
                                    repeat(stepsToAdvance) {
                                        onNextStep()
                                    }
                                } else {
                                    onSkipOrFinish()
                                }
                            } else {
                                if (currentStepIndex >= steps.lastIndex) {
                                    onSkipOrFinish()
                                } else {
                                    onNextStep()
                                }
                            }
                        }
                    )
                },
                onPrevious = {
                    if (currentStep.trackingTag.isNotEmpty()) {
                        onTrackingEvent?.invoke(currentStep.trackingTag, OnboardingAction.PREVIOUS_CLICK)
                    }
                    handleManualStepChange(
                        showBottomContent = {
                            showBottomContent = false
                        },
                        action = {
                            val previousIndex =
                                findPreviousStepIndex(
                                    steps = steps,
                                    currentIndex = currentStepIndex
                                )

                            val stepsToGoBack =
                                currentStepIndex - previousIndex

                            repeat(stepsToGoBack) {
                                onPreviousStep()
                            }
                        }
                    )
                },
                onSkipOrFinish = {
                    val eventType = if (isLastStep) OnboardingAction.FINISH_CLICK else OnboardingAction.SKIP_CLICK
                    if (currentStep.trackingTag.isNotEmpty()) {
                        onTrackingEvent?.invoke(currentStep.trackingTag, eventType)
                    }
                    handleManualStepChange(
                        showBottomContent = {
                            showBottomContent = false
                        },
                        action = {
                            onSkipOrFinish()
                        }
                    )
                },
                colors = colors
            )

            bottomContent(contentData)
        }
    }
}


/*
 * =============================================================
 * FIND PREVIOUS STEP
 * =============================================================
 *
 * هدف:
 *
 * اگر step قبلی Auto باشد،
 * ابتدای گروه Auto را پیدا کن.
 *
 *
 * مثال:
 *
 * 0 normal
 * 1 normal
 * 2 auto
 * 3 auto
 * 4 auto
 * 5 auto
 * 6 normal
 *
 *
 * نتیجه:
 *
 * current 6 -> 2
 * current 5 -> 2
 * current 4 -> 2
 * current 3 -> 2
 * current 2 -> 1
 * current 1 -> 0
 */
private data class MainStepGroup(
    val steps: List<GuideStep>,
    val startIndex: Int,
    val endIndex: Int,
    val groupId: String?
)

private fun getMainSteps(steps: List<GuideStep>): List<MainStepGroup> {
    val mainSteps = mutableListOf<MainStepGroup>()
    var i = 0
    while (i < steps.size) {
        val step = steps[i]
        if (step.groupId != null) {
            val groupSteps = mutableListOf<GuideStep>()
            val startIdx = i
            val gid = step.groupId
            while (i < steps.size && steps[i].groupId == gid) {
                groupSteps.add(steps[i])
                i++
            }
            mainSteps.add(MainStepGroup(groupSteps, startIdx, i - 1, gid))
        } else {
            mainSteps.add(MainStepGroup(listOf(step), i, i, null))
            i++
        }
    }
    return mainSteps
}

private fun findPreviousStepIndex(
    steps: List<GuideStep>,
    currentIndex: Int
): Int {
    if (currentIndex <= 0) {
        return 0
    }
    val mainSteps = getMainSteps(steps)
    val currentMainGroupIdx = mainSteps.indexOfFirst { currentIndex in it.startIndex..it.endIndex }
    if (currentMainGroupIdx <= 0) {
        return 0
    }

    return mainSteps[currentMainGroupIdx - 1].startIndex
}


/*
 * =============================================================
 * MANUAL STEP CHANGE
 * =============================================================
 */
private fun handleManualStepChange(
    showBottomContent: () -> Unit,
    action: () -> Unit
) {

    /*
     * اول fade out
     */
    showBottomContent()


    /*
     * بعد تغییر step
     */
    action()
}


/*
 * =============================================================
 * CONSTANT
 * =============================================================
 */
private const val
        BOTTOM_CONTENT_ANIMATION_DURATION =
    300


/*
 * =============================================================
 * SCALE TARGET SHAPE
 * =============================================================
 */
private fun scaleTargetShape(
    shape: TargetShape,
    scale: Float,
    density: androidx.compose.ui.unit.Density
): TargetShape {


    fun Dp.scale(): Dp {

        return with(density) {

            (
                    toPx() * scale
                    ).toDp()
        }
    }


    return when (shape) {


        is TargetShape.Circle -> {

            TargetShape.Circle(

                radiusDp =
                    shape.radiusDp.scale()
            )
        }


        is TargetShape.RoundedRect -> {

            TargetShape.RoundedRect(

                widthDp =
                    shape.widthDp.scale(),

                heightDp =
                    shape.heightDp.scale(),

                cornerRadiusDp =
                    shape.cornerRadiusDp.scale()
            )
        }


        is TargetShape.Rectangle -> {

            TargetShape.Rectangle(

                widthDp =
                    shape.widthDp.scale(),

                heightDp =
                    shape.heightDp.scale(),

                cornerRadiusDp =
                    shape.cornerRadiusDp.scale()
            )
        }


        is TargetShape.Oval -> {

            TargetShape.Oval(

                widthDp =
                    shape.widthDp.scale(),

                heightDp =
                    shape.heightDp.scale()
            )
        }


        is TargetShape.Capsule -> {

            TargetShape.Capsule(

                widthDp =
                    shape.widthDp.scale(),

                heightDp =
                    shape.heightDp.scale()
            )
        }


        is TargetShape.CutCornerRect -> {

            TargetShape.CutCornerRect(

                widthDp =
                    shape.widthDp.scale(),

                heightDp =
                    shape.heightDp.scale(),

                cutSizeDp =
                    shape.cutSizeDp.scale()
            )
        }


        is TargetShape.Star -> {

            TargetShape.Star(

                radiusDp =
                    shape.radiusDp.scale(),

                innerRadiusRatio =
                    shape.innerRadiusRatio,

                points =
                    shape.points
            )
        }


        is TargetShape.Triangle -> {

            TargetShape.Triangle(

                widthDp =
                    shape.widthDp.scale(),

                heightDp =
                    shape.heightDp.scale()
            )
        }


        is TargetShape.Polygon -> {

            TargetShape.Polygon(

                radiusDp =
                    shape.radiusDp.scale(),

                sides =
                    shape.sides
            )
        }
    }
}

@Composable
fun CustomImage(modifier: Modifier = Modifier, id: Int, contentScale: ContentScale = ContentScale.FillBounds) {
    if (id != 0) {
        Image(
            painter = painterResource(id = id),
            contentDescription = "",
            modifier = modifier,
            contentScale = contentScale
        )
    }
}